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

import java.util.Properties;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import org.melati.*;
import org.melati.poem.*;
import org.melati.util.*;


/**
 * An SMTP session for receiving one or more incoming emails from a
 * client (in fact typically <TT>sendmail</TT>).
 *
 * One of these threads is spawned each time <TT>sendmail</TT>
 * connects to our server socket to forward some message(s) to us.
 */

class SMTPSession extends Thread {

  private String smtpIdentifier;
  private Socket withClient;
  private PushbackInputStream fromClientPushBack;
  private BufferedReader fromClient;
  private Properties databaseNameOfDomain;
  private int bufSize;
  private PrintWriter toClient;
  private Log log;

  private String sender = null;
  private Database database = null;
  private BoardStore store = null;


  /**
   * A handler for a session with <TT>sendmail</TT>
   *
   * @param smtpIdentifier  what we should report our hostname as:
   *                            this must be different from what
   *                            <TT>sendmail</TT> thinks it is called,
   *                            or it will fail with a loopback error
   *
   * @param withClient          the socket on which <TT>sendmail</TT>
   *                            has just connected
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

  SMTPSession(String smtpIdentifier,
              Socket withClient,
              Properties databaseNameOfDomain,
              int bufSize,
              Log log)
      throws IOException {
    this.smtpIdentifier = smtpIdentifier;
    this.withClient = withClient;
    this.databaseNameOfDomain = databaseNameOfDomain;
    this.bufSize = bufSize;
    this.log = log;

    fromClientPushBack =
      new PushbackInputStream(new BufferedInputStream(
                                    withClient.getInputStream(),
                                    bufSize));

    fromClient = new BufferedReader(new InputStreamReader(fromClientPushBack));

    toClient =
      new PrintWriter(new OutputStreamWriter(withClient.getOutputStream(),
                                             "8859_1"),
                      true);
  }

  /**
   * Handle an SMTP <TT>RSET</TT> command, or generally otherwise tidy up
   */

  private void reset() {
    sender = null;
    database = null;
    store = null;
  }

  /**
   * Handle an SMTP <TT>MAIL FROM:</TT> command
   *
   * @param address     the address after the colon
   */

  private void mailFrom(String address) {
    if (address.charAt(0) == '<') {
      // hmm, not sure this actually happens
      address = address.substring(1, address.length() - 1);
    }

    // at this stage we don't have a database name so we can't verify
    sender = address;
    toClient.println("250 " + address + "... Sender provisionally OK");
  }

  /**
   * The board associated with a given email address
   *
   * @param address     the email address in question; the domain
   *                            part after the <TT>@</TT> is looked up in
   *                            the domain-to-database properties map
   */

  private Database databaseForAddress(String address)
                          throws MessagingException, IOException {
    int atIndex = address.indexOf('@');
    if (atIndex == -1)
      throw new MessagingException("`" + address + "': missing domain, " +
                                   "so can't determine which database to use");

    String domain = address.substring(atIndex + 1);
    String propertyName =
        "org.paneris.melati.boards.receivemail.database." + domain;

    String databaseName = databaseNameOfDomain.getProperty(propertyName);

    if (databaseName == null)
      throw new MessagingException(
          "`" + domain + "' is not a board mail domain " +
          "(no entry `" + propertyName + "' in properties)");

    try {
       return LogicalDatabase.getDatabase(databaseName);
    }
    catch (DatabaseInitException e) {
      throw new IOException(
          "failed to open database `" + domain + "' -> `" + databaseName +
          "': " + e);
    }
  }

  /**
   * Handle an SMTP <TT>RCPT TO:</TT> command
   *
   * @param address     the address after the colon
   */

  private void rcptTo(String address) throws Exception {
    if (sender == null)
      toClient.println("503 Need MAIL before RCPT");

    else if (database != null) {
      // FIXME actually the logic of this could be worked out, but for now ...

      toClient.println("553 a message can only appear on one board, " +
                       "but this one was copied to several");
    }

    else {
      if (address.charAt(0) == '<') {
        // hmm, not sure this actually happens
        address = address.substring(1, address.length() - 1);
      }

      final String address1 = address;
      final String sender1 = sender;

      try {
        database = databaseForAddress(address1);
      }
      catch (MessagingException e) {
        toClient.println("550 " + // RFC 821: "not found"
                         StringUtils.tr(e.getMessage(), "\n\r", "  "));
        log.warning("board address `" + address1 + "' rejected: " + e);
        return;
      }

      database.inSession(AccessToken.root,
                       new PoemTask () {
                         public void run() {

      try {

    	// now we know which database we are concerned with, we can
	    // validate the sender and recipient
        /*
         store = new BoardStore(database, log,
                               new InternetAddress(sender1),
                               new InternetAddress(address1));
        */      
        String boardStoreImpl=databaseNameOfDomain.
                                getProperty("org.paneris.melati.boards.receivemail.BoardStoreImpl");
        Class clazz=null;
        try {
          clazz=Class.forName(boardStoreImpl);
        } catch (Exception e) {
          log.exception(e);
          clazz=Class.forName("org.paneris.melati.boards.receivemail.BoardStoreImpl");
        }
        store = (BoardStore)clazz.newInstance();
        store.init(database, log, new InternetAddress(sender1), new InternetAddress(address1));
        
        toClient.println("250 Recipient OK");
      }
      catch (MessagingException e) {
        toClient.println("550 " + // RFC 821: "not found"
                             StringUtils.tr(e.getMessage(), "\n\r", "  "));
        log.warning("board address `" + address1 + "' rejected: " + e);
        database = null;
      }
      catch (Exception e) {
        toClient.println("554 Sorry: something is wrong with this server---" +
	                      StringUtils.tr(e.toString(), "\n\r", "  "));
        log.error("post of message from `" + sender1 + "' failed:\n" +
                      ExceptionUtils.stackTrace(e));
        database = null;
      }

	                     }
	                   }); // end of database.inSession
      
    }
  }

  /**
   * Handle an SMTP <TT>DATA</TT> command---post a message
   */

  private void data() {
    if (store == null)
      toClient.println("503 Need MAIL command");
    else {
      toClient.println("354 Enter mail, end with \".\" on a line by itself");

    database.inSession(store.getSender(),
                       new PoemTask () {
                         public void run() {

     try {
        Integer messageID = store.messageAccept(
                             new DotTerminatedInputStream(fromClientPushBack));
        toClient.println("250 " +messageID+ " Message accepted for delivery");
      }
      catch (SQLException e) {
        if (e.getMessage().startsWith("SQL Statement too long")) {
          toClient.println("552 Your message message is too long---" +
		            	   "can you split it up?");
          reset();
        }
	    else {
          toClient.println("554 Sorry: something is wrong with this server---" +
	                      StringUtils.tr(e.toString(), "\n\r", "  "));
          log.error("Exception trying to store a message:" +
    	            ExceptionUtils.stackTrace(e));
          reset();
	    }
      }
      catch (Exception e) {
        toClient.println("554 Sorry!!!: something is wrong with this server---" +
	                      StringUtils.tr(e.toString(), "\n\r", "  "));
          log.error("Exception trying to store a message:" +
    	            ExceptionUtils.stackTrace(e));
          reset();
      }

                        }
                      }); // end of database.inSession
      reset();
    }
  }

  /**
   * Do the business
   */

  public void run () {
    try {
      toClient.println("220 " + smtpIdentifier + " SMTP");
      for (;;) {
        String command = fromClient.readLine().trim();

        if (command.regionMatches(true, 0, "HELO", 0, 4))
          toClient.println("250 " + smtpIdentifier);

        else if (command.regionMatches(true, 0, "MAIL FROM:", 0, 10))
          mailFrom(command.substring(10).trim());

        else if (command.regionMatches(true, 0, "RCPT TO:", 0, 8))
          rcptTo(command.substring(8).trim());

        else if (command.regionMatches(true, 0, "DATA", 0, 4))
          data();

        else if (command.regionMatches(true, 0, "RSET", 0, 4)) {
          reset();
          toClient.println("250 Reset state");
        }

        else if (command.regionMatches(true, 0, "QUIT", 0, 4)) {
          toClient.println("221 " + smtpIdentifier + " closing connection");
          break;
        }

        // does it matter that we don't do VRFY?

        else
          toClient.println("500 Command unrecognized: \"" + command + "\"");
      }
    }
    catch (Exception e) {
      toClient.println("554 Sorry: something is wrong with this server---" +
                       StringUtils.tr(e.toString(), "\n\r", "  "));
      log.error("post of message from `" + sender + "' failed:\n" +
                    ExceptionUtils.stackTrace(e));
    }
    finally {
      try {
        reset();
        withClient.close();
      }
      catch (Exception e) { }
    }
  }
}

