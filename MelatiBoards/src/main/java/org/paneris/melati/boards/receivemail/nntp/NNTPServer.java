/*
 * $Source$
 * $Revision$
 *
 * Copyright (C) 2003 Vasily Pozhidaev 
 *
 * Part of a Melati application. This application is free software;
 * Permission is granted to copy, distribute and/or modify this
 * software under the same terms as those set out for Melati at 
 * http://melati.org.
 *
 * Contact details for copyright holder:
 *
 *     Vasily Pozhidaev  <vasilyp@paneris.org>
 */

package org.paneris.melati.boards.receivemail.nntp;

import java.net.ServerSocket;
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
  private int bufSize = 65536;
  private Log log = null;
  private Thread myThread = null;

  /**
   * Constructor.
   * 
   * @param identifier  News group identifier eg org.paneris
   * @param port        Socket port to listen on
   * @param config      A properties file containing configuration
   * @param bufSize     The Buffer size to use
   * @param log         A Log object
   * @throws ServletException if anything goes wrong
   */
  public NNTPServer(String identifier, int port, Properties config,
                    int bufSize, Log log)
      throws ServletException {
    this.identifier = identifier;
    this.port = port;
    this.config = config;
    this.bufSize = bufSize;
    this.log = log;
  }

  /**
   * Run it.
   * 
   * @see java.lang.Runnable#run()
   */
  public void run() {
    myThread = Thread.currentThread();

    try {
      ServerSocket serverSocket = new ServerSocket(port);
      while (true) {
        (new NNTPSession(identifier, serverSocket.accept(), config, log))
          .start();
      }
    } catch (Exception e) {
      log.exception(e);
      e.printStackTrace();
    }
  }

  /**
   * Stop the server
   * 
   * @throws IllegalStateException if the server is not running.
   */
  public void stop() throws IllegalStateException {
    if (myThread == null)
      throw new IllegalStateException("Server is not running");
    myThread.interrupt();
    myThread = null;
  }
}