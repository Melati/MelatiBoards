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
 *     Myles Chippendale <mylesc@paneris.org>
 *     http://paneris.org/
 *     29 Stanley Road, Oxford, OX4 1QY, UK
 */

package org.paneris.melati.boards.receivemail;

import java.net.ServerSocket;
import java.util.Properties;

import javax.servlet.ServletException;

public class SMTPServer implements Runnable {

  private SMTPServerServlet launcher = null;
  private String smtpIdentifier = null;
  private int port = 1615;
  private Properties databaseNameOfDomain = null;
  private int bufSize = 65536;
  private Log log = null;

  /**
   * A handler for a session with <TT>sendmail</TT>
   *
   * @param launcher 	        A handle to the servlet which started us.
   *
   * @param smtpIdentifier	what we should report our hostname as:
   *                            this must be different from what
   *                            <TT>sendmail</TT> thinks it is called,
   *                            or it will fail with a loopback error
   *
   * @param databaseNameOfDomain the mapping of each mail domain we handle
   *                            to the database in which the
   *                            corresponding messageboard lives (see
   *                            <TT>smtpServer.properties.example</TT>)
   *
   * @param bufSize             how much to buffer the IPC stream coming from
   *                            <TT>sendmail</TT> (non-critical, say 64k)
   *
   * @param log                 where to report errors
   */

  public SMTPServer(SMTPServerServlet launcher,
                    String smtpIdentifier,
                    int port,
                    Properties databaseNameOfDomain,
                    int bufSize,
                    Log log) throws ServletException {
      this.launcher = launcher;
      this.smtpIdentifier = smtpIdentifier;
      this.port = port;
      this.databaseNameOfDomain = databaseNameOfDomain;
      this.bufSize = bufSize;
      this.log = log;
  }

  /**
   * Thread run method. Creates a server socket on the specified port and
   * passes any incoming connections to SMTPSession
   */
  public void run() {

    final Thread myThread = Thread.currentThread();

    try {
        ServerSocket serverSocket = new ServerSocket(port);

        // die when SMTPServerServlet is 'destroy'ed
        // (and sets its smtpserver to null)

        while (launcher.smtpserver == myThread)
          (new SMTPSession(smtpIdentifier,
                           serverSocket.accept(),
                           databaseNameOfDomain,
                           bufSize,
                           log)).start();
    }
    catch (Exception e) {
      log.exception(e);
      e.printStackTrace();
      System.exit(1);
    }
  }
}
