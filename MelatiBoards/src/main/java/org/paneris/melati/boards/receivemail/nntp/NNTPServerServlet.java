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
  static Thread nntpServer = null;
  private static final int port = 119;
  private static final String logPathDefault =
    "/usr/local/apache/log/messageboard-nntp.log";
  Log log = new Log("nntp");
  Properties props = null;
  String identifier = null;

  public void init(ServletConfig config) throws ServletException {
    this.config = config;
    String logPath = config.getInitParameter("log");
//    String pt = config.getInitParameter("port");
    String database = config.getInitParameter("database");
    String prefix = config.getInitParameter("prefix");
    identifier = config.getInitParameter("identifier");
    try {
      if (logPath == null) {
        log.setTarget(logPathDefault);
      }
      if (identifier == null) {
        identifier =
          "messageboards." + InetAddress.getLocalHost().getHostName();
      }
      props = new Properties();
      props.setProperty("database", database);
      props.setProperty("prefix", prefix);
      if (nntpServer == null) {
        nntpServer =
          new Thread(
            new NNTPServer(identifier, port, props, 65536, log),
            "boards nntpserver");
        nntpServer.start();
        log.debug("Started NNTP server servlet");
      }
    } catch (Exception e) {
      log.exception(e);
      e.printStackTrace();
    }
  }

  /* 
   * @see javax.servlet.Servlet#destroy()
   */
  public void destroy() {
    try {
      nntpServer.interrupt();
    } catch (Exception e) {
      log.exception(e);
      e.printStackTrace();
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
    String cmd = req.getParameter("cmd");
    if ("start".equalsIgnoreCase(cmd)) {
      if (nntpServer == null) {
        nntpServer =
          new Thread(
            new NNTPServer(identifier, port, props, 65536, log),
            "boards nntpserver");
        nntpServer.start();
        log.debug("Started NNTP server servlet");
      }
    } else if ("stop".equalsIgnoreCase(cmd)) {
      try {
        nntpServer.interrupt();
      } catch (Exception e) {
        log.exception(e);
        e.printStackTrace();
      }
    }
    out.println(
      "The NNTP server is "
        + (nntpServer != null
          ? "running [" + "<a href='cmd=stop'>stop</a>" + "]"
          : "not running [" + "<a href='cmd=start'>start</a>" + "]"));
    out.flush();
    out.close();
  }

}