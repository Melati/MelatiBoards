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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import org.melati.poem.AccessPoemException;
import org.melati.poem.PoemDatabase;
import org.melati.poem.PoemTask;
import org.paneris.melati.boards.model.Attachment;
import org.paneris.melati.boards.model.Board;
import org.paneris.melati.boards.model.Message;
import org.paneris.melati.boards.model.User;
import org.paneris.melati.boards.receivemail.DotTerminatedInputStream;
import org.paneris.melati.boards.receivemail.Log;
/**
 * An NNTP session object.
 * 
 * 
 * @author Vasily Pozhidaev <vasilyp@paneris.org>
 * @todo internal article pointer should be maintained
 * @todo implement LAST and NEXT commands
 */
public class NNTPSession extends Thread {
  public static final SimpleDateFormat nntpDateFormat        = new SimpleDateFormat(
                                                                 "EEE, dd MMM yyyy HH:mm:ss z",
                                                                 Locale.US);
  public static final DateFormat       longRFC977DateFormat  = new SimpleDateFormat(
                                                                 "yyyyMMdd HHmmss",
                                                                 Locale.ENGLISH);
  public static final DateFormat       shortRFC977DateFormat = new SimpleDateFormat(
                                                                 "yyMMdd HHmmss",
                                                                 Locale.ENGLISH);
  static {
    nntpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
  }
  private String                       nntpIdentifier;
  private Socket                       socket;
  private BufferedReader               reader;
  private Properties                   config;
  private PrintWriter                  writer;
  private Log                          log                   = null;
  private String                       prefix;
  private NNTPStore                    store                 = null;
  private String                       selectedGroup         = null;
  private AuthInfo                     authInfo              = null;
  /**
   * An authorisation information object.
   */
  public class AuthInfo {
    private String login    = null;
    private String password = null;
    private User   user     = null;
    /**
     * 
     */
    public AuthInfo() {
      super();
      user = (User) store.getDb().getUserTable().guestUser();
    }
    public String getLogin() {
      return login;
    }
    public String getPassword() {
      return password;
    }
    /**
     * @return Returns the user.
     */
    public User getUser() {
      return user;
    }
    /**
     * @param user
     *          The user to set.
     */
    public void setUser(User user) {
      this.user = user;
    }
  }
  /**
   * Constructor.
   * 
   * @param nntpIdentifier
   *          eg org.paneris
   * @param socket
   *          the socket to listen on
   * @param config
   *          a Properties object to get configuration from
   * @param log
   *          a Log to log errors to
   * @throws IOException
   *           if something goes wrong with the File System
   */
  NNTPSession(String nntpIdentifier, Socket socket, Properties config, Log log)
      throws IOException {
    this.nntpIdentifier = nntpIdentifier;
    this.socket = socket;
    this.config = config;
    this.log = log;
    this.prefix = config.getProperty("prefix");
    try {
      Enumeration them = config.keys();
      while(them.hasMoreElements()) 
        System.err.println(them.nextElement());
      store = new NNTPStore(config.getProperty("database"), 
                            config.getProperty("prefix"), nntpIdentifier);
      authInfo = new AuthInfo();
    } catch (Exception e) {
      e.printStackTrace();
    }
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream())) {
      public String readLine() throws IOException {
        String s = super.readLine();
        if (s.trim().length() == 0)
          System.err.println("C: [empty line]");
        System.err.println("C: " + s);
        return s;
      }
    };
    writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream())) {
      public void println() {
        print("\r\n");
        flush();
      }
      public void println(String s) {
        super.println(s);
        System.err.println("S: " + s);
      }
    };
  }
  private void reset() {
    store = null;
  }
  /*
   * @see java.lang.Runnable#run()
   */
  public void run() {
    try {
      System.err.println("Connection from " + socket.getInetAddress());
      writer.println("200 " + nntpIdentifier
          + " server ready - posting allowed");
      while (socket != null && !socket.isClosed()) {
        String command = reader.readLine();
        if (!handleCommand(command))
          break;
      }
    } catch (Exception e) {
      writer.println("503 program fault - command not performed: "
          + e.toString());
      e.printStackTrace();
    } finally {
      try {
        if (socket != null && !socket.isClosed())
          quit();
        writer.close();
        reader.close();
      } catch (Exception e) {
        log.exception(e);
      }
    }
  }
  
  private boolean handleCommand(final String rawCommand) {
    BoardPoemTask task = new BoardPoemTask() {
      public boolean execute() {
        if (rawCommand == null)
          return false;
        StringTokenizer tokens = new StringTokenizer(rawCommand);
        if (!tokens.hasMoreTokens())
          return true;
        final String command = tokens.nextToken();
        if (command.equalsIgnoreCase("QUIT"))
          quit();
        else if (command.equalsIgnoreCase("HELP"))
          help();
        else if (command.equalsIgnoreCase("MODE") && tokens.hasMoreTokens()
            && tokens.nextToken().equalsIgnoreCase("READER"))
          modereader();
        else if (command.equalsIgnoreCase("LIST"))
          list(tokens);
        else if (command.equalsIgnoreCase("NEWGROUPS"))
          newgroups(tokens);
        else if (command.equalsIgnoreCase("GROUP"))
          group(tokens.hasMoreTokens() ? tokens.nextToken() : null);
        else if (command.equalsIgnoreCase("XOVER"))
          xover(tokens.hasMoreTokens() ? tokens.nextToken() : null);
        else if (command.equalsIgnoreCase("ARTICLE"))
          article(tokens.hasMoreTokens() ? tokens.nextToken() : null);
        else if (command.equalsIgnoreCase("POST"))
          post();
        else if (command.equalsIgnoreCase("XPAT"))
          xpat(tokens);
        else if (command.equalsIgnoreCase("AUTHINFO"))
          authinfo(tokens);
        return true;
      }
    };
    PoemDatabase db = (PoemDatabase) store.getDb();
    if (db == null)
      System.err.println("DB not initialised");
    db.inSession(authInfo.getUser(), task);
    return task.result;
  }
  
  /**
   * Handle the QUIT command, or closure.
   * 
   */
  private void quit() {
    writer.println("205 closing connection");
    try {
      socket.close();
      socket = null;
    } catch (Exception e) {
      log.exception(e);
    }
    reset();
  }

  /**
   * Handle the USER and PASS commands.
   * 
   * @param tokens
   *          to read commands from
   */
  private void authinfo(StringTokenizer tokens) {
    String command = tokens.nextToken();
    if (command.equalsIgnoreCase("USER")) {
      authInfo.login = tokens.nextToken();
      writer.println("381 More authentication information required");
    } else if (command.equalsIgnoreCase("PASS")) {
      authInfo.password = tokens.nextToken();
      // establish user
      store.establishUser(authInfo);
      // Vasily: User will never be null as it is set to guest
      if (authInfo.user != null)
        writer.println("281 Authentication accepted");
      else
        writer.println("482 Authentication rejected");
    } else
      throw new RuntimeException("Unexpected command:" + command);
  }
  /**
   * Handle the POST command.
   * 
   */
  private void post() {
    if (authInfo != null) {
      writer.println("340 send article to be posted. End with <CR-LF>.<CR-LF>");
      try {
        MimeMessage message = new MimeMessage(Session.getDefaultInstance(System
            .getProperties(), null), new DotTerminatedInputStream(
            new PushbackInputStream(socket.getInputStream())));
        // saving
        store.postMessage(message, authInfo.user);
        writer.println("240 article received ok");
      } catch (AccessPoemException e) {
        e.printStackTrace();
        writer.println("440 posting not allowed");
      } catch (Exception e) {
        writer.println("241 posting failed");
        log.exception(e);
      }
    } else {
      // We could work out the user from the email address
      writer.println("440 posting not allowed");
    }
  }
  /**
   * Handle the MODE command.
   * 
   * 
   */
  private void modereader() {
    writer.println("200 Posting Permitted");
  }
  /**
   * Handle the HELP command.
   * 
   */
  private void help() {
    writer.println("100 Help text follows");
    writer.println(".");
  }
  /**
   * Handle the XOVER command.
   * 
   * @param range
   */
  private void xover(String range) {
    if (selectedGroup == null) {
      writer.println("412 No newsgroup selected");
      return;
    }
    try {
      Board brd = store.resolveTargetBoard(selectedGroup);
      Enumeration messages = store.getRange(range, brd);
      if (messages == null) {
        writer.println("420 No article(s) selected");
      } else {
        writer.println("224 Overview information follows");
        while (messages.hasMoreElements()) {
          Message msg = (Message) messages.nextElement();
          long byteCount = msg.getBody().length();
          long lineCount = msg.getLines().length;
          NNTPMessage nntpMsg = new NNTPMessage(msg);
          int id = nntpMsg.getNntpIdInt();
          String msgID = store.getArticleId(id);
          String references = store.getReferences(msg);
          
          String reply = id + "\t";
          reply += msg.getSubject() + "\t";
          reply += nntpMsg.getFrom() + "\t";
          reply += nntpDateFormat.format(msg.getDate()) + "\t";
          reply += msgID + "\t";
          reply += references + "\t";
          reply += ((byteCount == -1) ? 300 : byteCount) + "\t";
          reply += lineCount + "";
          writer.println(reply);
        }
        writer.println(".");
      }
    } catch (AccessPoemException e) {
      writer.println("502 No permission");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  /**
   * Handle the ARTICLE command.
   * 
   * @param id
   *          the NNTP article id
   */
  private void article(String id) {
    // todo: handle all id representations (rfc0977)
    if (id == null)
      throw new RuntimeException("Article Id null");
    int nntpMessageId = 0;
    try {
      nntpMessageId = Integer.decode(id).intValue();
    } catch (NumberFormatException e) {
      nntpMessageId = NNTPMessage.parseId(id);
    }
    try {
      NNTPMessage nntpMessage = store.getArticle(nntpMessageId);
      Message message = nntpMessage.getMessage();
      int attachmentCount = message.getAttachmentCount();
      String boundary = "------------070200030503000409090500";
      if (message != null) {
        writer.println("220 " + id + " " + store.getArticleId(nntpMessageId)
            + " article retrieved and follows");
        writer.flush();
        String references = store.getReferences(message);
        writer.println("From: " + nntpMessage.getFrom() + "\t");
        writer.println("Newsgroups: " + store.prefix + "."
            + message.getBoard().getName() + "\t");
        writer.println("Subject: " + message.getSubject());
        writer.println("Message-ID: " + store.getArticleId(nntpMessageId)
            + "\t");
        if (references.length() > 0) {
          writer.println("References: " + references + "\t");
        }
        writer.println("Date: " + nntpDateFormat.format(message.getDate())
            + "\t");
        if (attachmentCount > 0) {
          writer.println("Content-Type: multipart/mixed; boundary=\""
              + boundary + "\"\t");
          writer.println();
          writer.println("This is a multi-part message in MIME format.");
          writer.println("--" + boundary);
          writer.println("Content-Type: text/plain\t");
          writer.println("Content-Transfer-Encoding: 8bit\t");
        }
        writer.println();
        writer.println(message.getBody());
        if (attachmentCount > 0) {
          Enumeration attachments = message.getAttachments();
          while (attachments.hasMoreElements()) {
            Attachment element = (Attachment) attachments.nextElement();
            File f = new File(element.getPath());
            if (f.exists()) {
              // todo: determine content type more accurately
              // (using JAF or Servlet API)
              // "application/octet-stream";
              String contentType = element.getType().getType();
              String transferEncoding = "base64";
              if (element.getType().getType().equalsIgnoreCase("truncation")) {
                contentType = "text/plain";
                transferEncoding = "8bit";
              }
              writer.println();
              writer.println("--" + boundary);
              writer.println("Content-Type: " + contentType + "; name=\""
                  + element.getFilename() + "\"\t");
              writer.println("Content-Transfer-Encoding: " + transferEncoding
                  + "\t");
              writer.println("Content-Disposition: inline; filename=\""
                  + element.getFilename() + "\"\t");
              writer.println();
              writer.flush();
              // todo: use buffering?
              OutputStream out = MimeUtility.encode(socket.getOutputStream(),
                  transferEncoding);
              FileInputStream in = new FileInputStream(element.getPath());
              byte[] buffer = new byte[512];
              while (true) {
                int c = in.read(buffer);
                if (c == -1) {
                  break;
                }
                out.write(buffer, 0, c);
              }
              out.flush();
            }
          }
          writer.println("\r\n--" + boundary + "--");
        }
        writer.println(".");
        writer.flush();
      } else {
        writer.println("430 no such article found");
      }
    } catch (AccessPoemException e) {
      writer.println("502 no permission");
    } catch (Exception e) {
      e.printStackTrace();
      writer.println("503 program error function not performed");
    }
  }
  /**
   * Handle the GROUP command.
   * 
   * @param name
   *          the Board name
   */
  private void group(String name) {
    try {
      Board board = store.getBoard(name);
      if (board == null) throw new UnknownNewsGroupException("Board " + name + "not found");
      int count = board.getMessageCount();
      // seems like BUG in M$ Outlook: it cannot handle properly when id < 1,
      // Mozilla works OK with 0 identifier
      int first = board.getFirstMessageId() + 1;
      int last = board.getLastMessageId() + 1;
      if (count == 0) {
        first = 0;
        last = 0;
      }
      writer.println("211 " + count + " " + first + " " + last + " " + name
          + " group selected");
      selectedGroup = name;
    } catch (AccessPoemException e) {
      writer.println("411 no such newsgroup");
      // writer.println("502 no permission");
    } catch (Exception e) {
      e.printStackTrace();
      writer.println("503 program fault - command not performed: "
          + e.toString());
    }
  }

  private Date getDateFrom(StringTokenizer tok) {
    try {
      String dateStr = tok.nextToken() + " " + tok.nextToken();
      boolean utc = (tok.hasMoreTokens());
      Date dt = null;
      if (dateStr.indexOf(' ') == 6) {
        dt = shortRFC977DateFormat.parse(dateStr);
      } else {
        dt = longRFC977DateFormat.parse(dateStr);
      }
      if (utc)
        dt = new Date(dt.getTime()
            + Calendar.getInstance().get(Calendar.ZONE_OFFSET));
      return dt;
    } catch (ParseException pe) {
      return null;
    } catch (NoSuchElementException e) {
      return null;
    }
  }
  /**
   * Handle NEWGROUPS command.
   * 
   * @param tok
   *          token stream to get tokens from
   */
  private void newgroups(StringTokenizer tok) {
    writer.println("231 list of new newsgroups follows");
    Enumeration boards = store.getBoards(getDateFrom(tok), null);
    if (boards != null) {
      while (boards.hasMoreElements()) {
        Board board = (Board) boards.nextElement();
        writer.println(store.prefix + "." + board.getName() + " "
            + board.getPurpose());
      }
    }
    writer.println(".");
  }
  /**
   * Handle LIST command.
   * 
   * @param tok
   *          token stream
   */
  private void list(StringTokenizer tok) {
    String wildmat = "*";
    if (tok.hasMoreTokens()) {
      String param = tok.nextToken();
      String[] unsupported = {"ACTIVE.TIMES", "DISTRIBUTIONS", "DISTRIB.PATS",
          "SUBSCRIPTIONS"};
      for (int i = 0; i < unsupported.length; i++) {
        if (unsupported[i].equalsIgnoreCase(param)) {
          writer.println("501 command not supported");
          return;
        }
      }
      if (tok.hasMoreTokens()) {
        wildmat = tok.nextToken();
      }
      if (param.equalsIgnoreCase("NEWSGROUPS")) {
        writer.println("215 list of newsgroups follows");
        Enumeration boards = store.getBoards(null, wildmat);
        if (boards != null) {
          while (boards.hasMoreElements()) {
            Board board = (Board) boards.nextElement();
            writer.println(store.prefix + "." + board.getName() + " "
                + board.getDisplayname() + " " + board.getPurpose());
          }
        }
      } else if (param.equalsIgnoreCase("ACTIVE")) {
        writer.println("215 list of newsgroups follows");
        Enumeration boards = store.getBoards(null, wildmat);
        if (boards != null) {
          while (boards.hasMoreElements()) {
            Board board = (Board) boards.nextElement();
            writer.println(store.prefix + "." + board.getName() + " "
                + (board.getModeratedposting().booleanValue() ? "n" : "y"));
          }
        }
      } else if (param.equalsIgnoreCase("OVERVIEW.FMT")) {
        writer.println("215 Order of fields in overview database");
        writer.println("Subject:");
        writer.println("From:");
        writer.println("Date:");
        writer.println("Message-ID:");
        writer.println("References:");
        writer.println("Bytes:");
        writer.println("Lines:");
        writer.println("Xref:full");
      } else {
        writer.println("501 command not supported");
        return;
      }
    } else {
      writer.println("215 list of newsgroups follows");
      Enumeration boards = store.getBoards(null, null);
      if (boards != null) {
        while (boards.hasMoreElements()) {
          Board board = (Board) boards.nextElement();
          writer.println(store.prefix + "." + board.getName() + " "
              + (board.getModeratedposting().booleanValue() ? "n" : "y"));
          /*
           * writer.println( store.prefix + "." + board.getName() + " " +
           * (board.canPost(authInfo.user) ? "y" : "n"));
           */
        }
      }
    }
    writer.println(".");
  }
  /**
   * Handle XPAT command.
   * 
   * @param tok
   *          token stream
   */
  private void xpat(StringTokenizer tok) {
    String header = "SUBJECT";
    header = tok.nextToken();
    String range = tok.nextToken();
    String wildmat = "";
    while (tok.hasMoreTokens()) {
      wildmat += " " + tok.nextToken();
    }
    wildmat = wildmat.trim();
    store.xpat(header, range, wildmat, selectedGroup, writer);
  }
  abstract class BoardPoemTask implements PoemTask {
    public boolean result = false;
    public void run() {
      result = execute();
    }
    public abstract boolean execute();
  }
}
