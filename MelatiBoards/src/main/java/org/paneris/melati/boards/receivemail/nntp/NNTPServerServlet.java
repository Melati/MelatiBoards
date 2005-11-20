/*
 * $Source$
 * $Revision$
 *
 * Copyright (C) 2003 Vasily Pozhidaev 
 *
 * Part of a Melati application. This application is free software;
 * Permission is granted to copy, distribute and/or modify this
 * software under the same terms as those set out for Melati below.
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
 *     Vasily Pozhidaev  <vasilyp@paneris.org>
 */

package org.paneris.melati.boards.receivemail.nntp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Properties;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.paneris.melati.boards.receivemail.Log;

/**
 * A servlet to run the NNTP Server.
 * 
 * @author vasilyp@paneris.org
 *
 */
public class NNTPServerServlet implements Servlet {

  private ServletConfig config = null;
  static Thread nntpServerThread = null;
  private static final String logPathDefault =
    "/var/log/messageboard-nntp.log";
  private static int port = 119;
  Log log = new Log("nntp");
  String identifier = null;
  Properties props = null;
  private static int buffSize = 65536;
  private static NNTPServer server;
  private String databaseName = null;
  private String prefix = null;
  private String logPath = null;
  public void init(ServletConfig configP) throws ServletException {
    this.config = configP;
    logPath = config.getInitParameter("log");
    if (logPath == null) {
      logPath = logPathDefault;
    }
    log.setTarget(logPath);
    databaseName = config.getInitParameter("database");
    prefix = config.getInitParameter("prefix");
    String portS = config.getInitParameter("port");
    if (portS != null) {
      NNTPServerServlet.port = Integer.parseInt(portS);
    }
    identifier = config.getInitParameter("identifier");
    String buffSizeS = config.getInitParameter("buffSize");
    if (buffSizeS != null) {
      NNTPServerServlet.buffSize = Integer.parseInt(buffSizeS);
    }
    try {
      if (identifier == null) {
        identifier =
          "messageboards." + InetAddress.getLocalHost().getHostName();
      }
      props = new Properties();
      props.setProperty("database", databaseName);
      props.setProperty("prefix", prefix);
      server = new NNTPServer(identifier, NNTPServerServlet.port, props, buffSize, log);
      if (nntpServerThread == null) {
        nntpServerThread =
          new Thread(
            server,
            "boards nntpserver");
        nntpServerThread.start();
        log.debug("Started NNTP server servlet");
      }
    } catch (Exception e) {
      log.exception(e);
    }
  }

  /* 
   * @see javax.servlet.Servlet#destroy()
   */
  public void destroy() {
    try {
      if (nntpServerThread != null) // if we have stopped it
        server.stop();
    } catch (Exception e) {
      log.exception(e);
    }
  }

  /* 
   * @see javax.servlet.Servlet#getServletConfig()
   */
  public ServletConfig getServletConfig() {
    return config;
  }

  /* 
   * @see javax.servlet.Servlet#getServletInfo()
   */
  public String getServletInfo() {
    return "NNTP Server launcher for Melati boards system";
  }

  /* 
   * @see javax.servlet.Servlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
   */
  public void service(ServletRequest req, ServletResponse resp)
    throws ServletException, IOException {
    // Give info about the server
    resp.setContentType("text/html");
    PrintWriter out = resp.getWriter();
    out.println("<html>\n<head>\n<title>");
    out.println("NNTP Server Status");
    out.println("</title>\n</head>");
    out.println("<body>");
    out.println("<h1>NNTP Server Status</h1>");
    out.println("<table border='1'>");
    out.println("<tr>");
    out.println("<th>Setting</th>");
    out.println("<th>Value</th>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td>logPath</td>");
    out.println("<td>" + logPath + "</td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td>port</td>");
    out.println("<td>" + port + "</td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td>identifier</td>");
    out.println("<td>" + identifier + "</td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td>prefix</td>");
    out.println("<td>" + prefix + "</td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td>buffSize</td>");
    out.println("<td>" + buffSize + "</td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td>databaseName</td>");
    out.println("<td>" + databaseName + "</td>");
    out.println("</tr>");
    
    out.println("</table>");
 
    String cmd = req.getParameter("cmd");
    if ("start".equalsIgnoreCase(cmd)) {
      log.debug("Starting server");
      if (nntpServerThread == null) {
        nntpServerThread =
          new Thread(
            server,
            "boards nntpserver");
        nntpServerThread.start();
        log.debug("Started NNTP server servlet");
      } else {
        out.println("<p>It aint null</p>");
      }
      
    } else if ("stop".equalsIgnoreCase(cmd)) {
      System.err.println("Stopping server");
      try {
        //nntpServerThread.interrupt();
        server.stop();
        nntpServerThread = null;
      } catch (Exception e) {
        log.exception(e);
      }
    }
    out.println(
      "The NNTP server is "
        + (nntpServerThread != null
          ? "running [" + "<a href='?cmd=stop'>stop</a>" + "]"
          : "not running [" + "<a href='?cmd=start'>start</a>" + "]"));
    out.println("</body>\n</html>");
    out.flush();
    out.close();
  }

}

