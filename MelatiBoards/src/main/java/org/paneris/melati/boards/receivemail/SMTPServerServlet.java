/*

 * $Source$

 * $Revision$

 *

 * Copyright (C) 2000 Myles Chippendale

 *

 * Part of a Melati application. This application is free software;

 * Permission is granted to copy, distribute and/or modify this

 * software under the same terms as those set out for Melati, below.

 *

 * Melati (http://melati.org) is a framework for the rapid

 * development of clean, maintainable web applications.

 *

 * Melati is free software; Permission is granted to copy, distribute

 * and/or modify this software under the terms either:

 *

 * a) the GNU General Public License as published by the Free Software

 *    Foundation; either version 2 of the License, or (at your option)

 *    any later version,

 *

 *    or

 *

 * b) any version of the Melati Software License, as published

 *    at http://melati.org

 *

 * You should have received a copy of the GNU General Public License and

 * the Melati Software License along with this program;

 * if not, write to the Free Software Foundation, Inc.,

 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA to obtain the

 * GNU General Public License and visit http://melati.org to obtain the

 * Melati Software License.

 *

 * Feel free to contact the Developers of Melati (http://melati.org),

 * if you would like to work out a different arrangement than the options

 * outlined here.  It is our intention to allow Melati to be used by as

 * wide an audience as possible.

 *

 * This program is distributed in the hope that it will be useful,

 * but WITHOUT ANY WARRANTY; without even the implied warranty of

 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

 * GNU General Public License for more details.

 *

 * Contact details for copyright holder:

 *

 *     Mylesc Chippendale <mylesc@paneris.org>

 *     http://paneris.org/

 *     29 Stanley Road, Oxford, OX4 1QY, UK

 */





package org.paneris.melati.boards.receivemail;

import javax.servlet.*;
import java.util.Properties;
import java.io.*;
import java.net.ServerSocket;
import java.net.InetAddress;
import org.webmacro.util.*;
import org.melati.util.*;

/**
 * An SMTP server to handle mail coming in to messageboards.
 *
 * It is intended to be invoked from init scripts approximately thus:
 *
 * <PRE>
 *   java org.paneris.messageboard.receivemail.SMTPServer --port 1615 &
 * </PRE>
 *
 * A single server can handle mail for messageboards in several
 * separate databases; the name of the database to which it should
 * connect is determined from the recipient address given when the
 * mail arrives.  The program looks for a `properties' resource under
 * the name <TT>smtpServer.properties</TT> which specifies the mappings
 * from domain to database name, in the form
 *
 * <PRE>
 *   org.paneris.melati.boards.receivemail.database.DOMAIN=DATABASE
 * </PRE>
 *
 * for example
 *
 * <PRE>
 *   org.paneris.melati.boards.receivemail.database.messageboards.x.com=x
 * </PRE>
 *
 * (see also <TT>smtpServer.properties.example</TT>).
 *
 * Mail can be funelled into the server either by having it listen on
 * port 25 in place of sendmail, or by configuring the locally running
 * sendmail to forward appropriately addressed email to the server on
 * a different port.  The only way I can see to do the latter is to
 * edit <TT>sendmail.cf</TT> to define a new mailer.  Where it says
 *
 * <PRE>
 *   Msmtp,             P=[IPC], F=mDFMuX, S=11/31, R=21, E=\r\n, L=990,
 *                      T=DNS/RFC822/SMTP,
 *                      A=IPC $h
 * </PRE>
 *
 * insert also
 *
 * <PRE>
 *   Msmtp1615,         P=[IPC], F=mDFMuX, S=11/31, R=21, E=\r\n, L=990,
 *                      T=DNS/RFC822/SMTP,
 *                      A=IPC $h 1615
 * </PRE>
 *
 * Then use the standard <TT>mailertable</TT> feature to direct, for
 * example, all mail to <TT>messageboards.x.com</TT> on to
 * <TT>smtp1615:x.com</TT>.  You can do this very easily with
 * <TT>linuxconf</TT>'s <I>Special (domain) routing</I> menu: make an
 * entry with <I>Destination</I> set to <TT>messageboards.x.com</TT>,
 * <I>Forwarder</I> set to <TT>x.com</TT>, and <I>Mailer</I> set to
 * <TT>smtp1615</TT>.  But make sure to hack
 * <TT>/usr/lib/linuxconf/mailconf/smtpmailer.std.cf</TT> first, as
 * described above, to get it to define the special mailer in the
 * <TT>sendmail.cf</TT> it generates.  And of course for every mail
 * domain which sendmail is configured to forward to the messageboards
 * server, there should be an entry in <TT>smtpServer.properties</TT>
 * telling the latter which database to use.
 *
 * The servlet takes the following arguments:
 *
 * <TABLE>
 *   <TR>
 *     <TD><TT>port <I>number</I></TT></TD>
 *     <TD>the port on which to listen, if not 1615</TD>
 *   </TR>
 *   <TR>
 *     <TD><TT>properties <I>filename</I></TT></TD>
 *     <TD>
 *       the name of a file in <TT>CLASSPATH</TT> containing the
 *       domain-to-database mappings, if not
 *       <TT>smtpServer.properties</TT>
 *     </TD>
 *   </TR>
 *   <TR>
 *     <TD><TT>log <I>filename</I></TT></TD>
 *     <TD>
 *       the name of the log file (defaults to
 *       <TT>/usr/local/apache/log/messageboard-receivemail.log</TT>
 *     </TD>
 *   </TR>
 * </TABLE>
 */
public class SMTPServerServlet implements Servlet {

  private ServletConfig config;
  Thread smtpserver = null;

  private static final int portDefault = 1615;
  private static final String propertiesNameDefault = "smtpServer.properties";
  private static final String logPathDefault =
      "/usr/local/apache/log/messageboard-receivemail.log";

  private static final String usage =
      "Usage: ... [--port <port>] [--properties <resource>] [--log <file>]\n" +
      "       defaults --port " + portDefault + "\n" +
      "                --properties " + propertiesNameDefault + "\n" +
      "                --log " + logPathDefault;

  /**
   * Servlet initialisation
   * <p>
   * Get parameters from the config file and launch a Warden thread
   *
   * @see SMTPServer
   */
  public void init(ServletConfig config) throws ServletException {

    this.config = config;

    int port = portDefault;
    String propertiesName = propertiesNameDefault;
    String logPath = null;

    String pt = config.getInitParameter("port");
    try { port = new Integer(pt).intValue(); } catch (Exception e) {}
    String props = config.getInitParameter("properties");
    if (props != null && !props.equals(""))
      propertiesName = props;
    String logP = config.getInitParameter("log");
    if (logP != null && !logP.equals("")) {
      logPath = logP;
    }

    try {
      if (logPath == null) {
   	logPath = logPathDefault;
      }
      PrintWriter log = new PrintWriter(new FileWriter(logPath));

      Properties databaseNameOfDomain =
          PropertiesUtils.fromResource((new SMTPServerServlet()).getClass(),
                                       propertiesName);

      // Launch our smtpserver
      if (smtpserver == null) {
        smtpserver = new Thread(new SMTPServer(this,
                       "messageboards."+InetAddress.getLocalHost().getHostName(),
                       port,
                       databaseNameOfDomain,
                       65536, log), "board smtpserver");
        smtpserver.start();
	log.println("Started SMTP server servlet");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Servlet cleanup
   * 
   * Clean up our smtpserver. Its run method checks that it's own
   * thread is the one pointed to by smtpserver, so setting it
   * to null means it exits
   */
  public void destroy() {
    smtpserver = null;
  }

  public ServletConfig getServletConfig() {
    return config;
  }

  public String getServletInfo() {
    return "A servlet to launch an SMTP server which receives emails for the boards system.";
  }
    
  /**
   * Service the servlet's request. This should always report that
   * the thread is launched because the only way of stopping it
   * is through this servlet's destroy method
   */
  public void service(ServletRequest req, ServletResponse resp)
    throws ServletException, IOException {

    // Give info about the server
    resp.setContentType("text/html");
    PrintWriter out = resp.getWriter();
    out.println("The SMTP server is "+
                  (smtpserver != null ? "running" : "not running"));
    out.close();
  }

}
