package org.paneris.melati.boards.receivemail;

import javax.servlet.*;
import java.util.Properties;
import java.io.*;
import java.net.ServerSocket;
import java.net.InetAddress;
import org.webmacro.util.*;
import org.melati.util.*;

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
   * @param bufSize             how much to buffer the IPC stream coming from <TT>sendmail</TT>
   *                            (non-critical, say 64k)
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
        {
          (new SMTPSession(smtpIdentifier,
                           serverSocket.accept(),
                           databaseNameOfDomain,
                           bufSize,
                           log)).start();
        }
    }
    catch (Exception e) {
      log.exception(e);
      e.printStackTrace();
      System.exit(1);
    }
  }

}
