package org.paneris.melati.boards.receivemail.nntp;

import java.net.ServerSocket;
import java.util.Properties;

import javax.servlet.ServletException;

import org.paneris.melati.boards.receivemail.Log;

class RunHSQL implements Runnable {

  public void run() {
    String[] args = new String[2];
    args[0] = "-port";
    args[1] = "9002";
    org.hsqldb.Server.main(args);
  }
}

public class NNTPServer implements Runnable {

  private String identifier = null;
  private int port = 119;
  private Properties config = null;
  private int bufSize = 65536;
  private Log log = null;
  private NNTPStore _store = null;
  private Thread myThread = null;

  public NNTPServer(
    String identifier,
    int port,
    Properties config,
    int bufSize,
    Log log)
    throws ServletException {
    this.identifier = identifier;
    this.port = port;
    this.config = config;
    this.bufSize = bufSize;
    this.log = log;
  }

  public void run() {
    //new Thread(new RunHSQL()).start();
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

  public void stop() throws IllegalStateException {
    if (myThread == null)
      throw new IllegalStateException("Server is not running");
    myThread.interrupt();
    myThread = null;
  }
}