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
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Properties;

import javax.servlet.ServletException;

import org.paneris.melati.boards.receivemail.Log;

/**
 * A News Server which enables read and write access to a 
 * Board.
 * Messages posted through the NNTP interface are distributed 
 * to the subscribed users by email in the normal way. 
 * 
 * @author Vasily Pozhidaev <vasilyp@paneris.org>
 *
 */
public class NNTPServer implements Runnable {

  private String identifier = null;
  private int port = 119;
  private Properties config = null;
  private int buffSize = 65536;
  private Log log = null;
  private Thread myThread;
  private ServerSocket serverSocket = null;

  /**
   * Constructor.
   * 
   * @param identifier  News group identifier eg org.paneris
   * @param port        Socket port to listen on
   * @param config      A properties file containing configuration
   * @param bufSize     The Buffer size to use (not used yet)
   * @param log         A Log object
   * @throws ServletException if anything goes wrong
   */
  public NNTPServer(String identifier, int port, Properties config,
                    int bufSize, Log log)
      throws ServletException {
    myThread = Thread.currentThread();
    this.identifier = identifier;
    this.port = port;
    this.config = config;
    this.buffSize = bufSize;
    this.log = log;
  }

  /**
   * Run it.
   * 
   * @see java.lang.Runnable#run()
   */
  public void run() {

    try {
      serverSocket = new ServerSocket(port);
      while (!myThread.isInterrupted()) {
        (new NNTPSession(identifier, serverSocket.accept(), config, log))
          .start();
      }
    } catch (SocketException e) {
      // Happens when stop() called
    } catch (Exception e) {
      //log.exception(e);
      e.printStackTrace();
    } finally {
      if (!serverSocket.isClosed())
        try {
          serverSocket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      myThread = null;      
    }
  }

  /**
   * Stop the server, after finishing established connections.
   * 
   * @throws IllegalStateException if the server is not running.
   */
  public synchronized void stop() throws IllegalStateException {
    if (myThread == null)
      throw new IllegalStateException("Server is not running");
    try {
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    myThread.interrupt();
  }
}

