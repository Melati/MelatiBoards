package org.paneris.melati.boards.receivemail.nntp;

import java.io.BufferedReader;
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
import org.melati.poem.PoemTask;
import org.paneris.melati.boards.model.Attachment;
import org.paneris.melati.boards.model.Board;
import org.paneris.melati.boards.model.BoardsDatabase;
import org.paneris.melati.boards.model.Message;
import org.paneris.melati.boards.model.User;
import org.paneris.melati.boards.receivemail.DotTerminatedInputStream;
import org.paneris.melati.boards.receivemail.Log;

abstract class BoardPoemTask implements PoemTask {

  public boolean result = false;

  public void run() {
    result = execute();
  }

  public abstract boolean execute();

}

//todo: internal article pointer should be maintained
//todo: implement LAST & NEXT commands 

public class NNTPSession extends Thread {

  public static SimpleDateFormat nntpDateFormat =
    new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

  public static final DateFormat longRFC977DateFormat =
    new SimpleDateFormat("yyyyMMdd HHmmss", Locale.ENGLISH);

  public static final DateFormat shortRFC977DateFormat =
    new SimpleDateFormat("yyMMdd HHmmss", Locale.ENGLISH);

  static {
    nntpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
  }

  private String nntpIdentifier;
  private Socket socket;
  private BufferedReader reader;
  private Properties config;
  private PrintWriter writer;
  private Log log = null;
  private String prefix;

  private NNTPStore store = null;

  private String selectedGroup = null;
  private AuthInfo authInfo = null;
  private BoardsDatabase db = null;

  public class AuthInfo {
    public String login = null;
    public String password = null;
    public User user = null;

    {
      user = (User)store.db.getUserTable().guestUser();
    }

    public String getLogin() {
      return login;
    }

    public String getPassword() {
      return password;
    }
  }

  NNTPSession(String nntpIdentifier, Socket socket, Properties config, Log log)
    throws IOException {
    this.nntpIdentifier = nntpIdentifier;
    this.socket = socket;
    this.config = config;
    this.log = log;
    this.prefix = config.getProperty("prefix");

    try {
      store =
        new NNTPStore(
          config.getProperty("database"),
          config.getProperty("prefix"),
          nntpIdentifier);
      authInfo = new AuthInfo();
    } catch (Exception e) {
      e.printStackTrace();
    }

    reader =
      new BufferedReader(new InputStreamReader(socket.getInputStream())) {
      public String readLine() throws IOException {
        String s = super.readLine();
        if (s.trim().length() == 0)
          System.err.println("C: [empty line]");
        System.err.println("C: " + s);
        return s;
      }
    };

    writer =
      new PrintWriter(new OutputStreamWriter(socket.getOutputStream())) {
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

  public void run() {
    try {
      System.err.println("Connection from " + socket.getInetAddress());
      writer.println(
        "200 " + nntpIdentifier + " server ready - posting allowed");
      while (!socket.isClosed()) {
        String command = reader.readLine();
        if (!handleCommand(command))
          break;
      }
    } catch (Exception e) {
      writer.println("503 program fault - command not performed");
    } finally {
      try {
        writer.println("205 closing connection");
        reset();
        writer.close();
        reader.close();
        socket.close();
      } catch (Exception e) {}
    }
  }

  private boolean handleCommand(final String rawCommand) {
    BoardPoemTask t = new BoardPoemTask() {
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
        else if (
          command.equalsIgnoreCase("MODE")
            && tokens.hasMoreTokens()
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
    store.db.inSession(authInfo.user, t);
    return t.result;
  }

  private void authinfo(StringTokenizer tok) {
    String command = tok.nextToken();
    if (command.equalsIgnoreCase("USER")) {
      authInfo.login = tok.nextToken();
      writer.println("381 More authentication information required");
    } else if (command.equalsIgnoreCase("PASS")) {
      authInfo.password = tok.nextToken();
      //establish user
      store.establishUser(authInfo);
      if (authInfo.user != null)
        writer.println("281 Authentication accepted");
      else
        writer.println("482 Authentication rejected");
    }
  }

  private void post() {
    if (authInfo != null) {
      writer.println("340 send article to be posted. End with <CR-LF>.<CR-LF>");
      try {
        MimeMessage message =
          new MimeMessage(
            Session.getDefaultInstance(System.getProperties(), null),
            new DotTerminatedInputStream(
              new PushbackInputStream(socket.getInputStream())));
        //saving
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
      writer.println("440 posting not allowed");
    }
  }

  private void modereader() {
    writer.println("200 Posting Permitted");
  }

  private void help() {
    writer.println("100 Help text follows");
    writer.println(".");
  }

  private void xover(String range) {
    over(range);
  }
  private void over(String range) {
    if (selectedGroup == null) {
      writer.println("412 No newsgroup selected");
      return;
    }
    store.printOverview(range, writer, selectedGroup);
  }

  private void article(String id) {
    //todo: handle all id representations (rfc0977)
    try {
      Message message = store.getArticle(Integer.decode(id).intValue() - 1);
      int attachmentCount = message.getAttachmentCount();
      String boundary = "------------070200030503000409090500";
      if (message != null) {
        writer.println(
          "220 "
            + id
            + " <"
            + id
            + store.msgIDSuffix
            + "> article retrieved and follows");
        writer.flush();
        String references = store.getReferences(message, store.prefix);
        writer.println("From: " + message.getAuthor().getEmail() + "\t");
        writer.println(
          "Newsgroups: "
            + store.prefix
            + "."
            + message.getBoard().getName()
            + "\t");
        writer.println("Subject: " + message.getSubject());
        writer.println("Message-ID: <" + id + store.msgIDSuffix + ">\t");
        if (references.length() > 0) {
          writer.println("References: " + references + "\t");
        }
        writer.println(
          "Date: " + nntpDateFormat.format(message.getDate()) + "\t");

        if (attachmentCount > 0) {
          writer.println(
            "Content-Type: multipart/mixed; boundary=\"" + boundary + "\"\t");
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
          int i = 1;
          while (attachments.hasMoreElements()) {
            writer.println();
            Attachment element = (Attachment)attachments.nextElement();
            //todo: determine content type more accurately (using JAF or Servlet API)
            String contentType = "application/octet-stream";
            String transferEncoding = "base64";
            if (element.getType().getType().equalsIgnoreCase("truncation")) {
              contentType = "text/plain";
              transferEncoding = "8bit";
            }
            writer.println("--" + boundary);
            writer.println(
              "Content-Type: "
                + contentType
                + "; name=\""
                + element.getFilename()
                + "\"\t");
            writer.println(
              "Content-Transfer-Encoding: " + transferEncoding + "\t");
            writer.println(
              "Content-Disposition: inline; filename=\""
                + element.getFilename()
                + "\"\t");
            writer.println();
            writer.flush();
            //todo: use buffering?
            OutputStream out =
              MimeUtility.encode(socket.getOutputStream(), transferEncoding);
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
    }

  }

  private void group(String name) {
    try {
      Board board = store.getBoard(name);
      int count = board.getMessageCount();
      //seems like BUG in M$ Outlook: it cannot handle properly when id < 1, 
      //Mozilla works OK with 0 identifier
      int first = board.getFirstMessageId() + 1;
      int last = board.getLastMessageId() + 1;
      if (count == 0) {
        first = 0;
        last = 0;
      }
      writer.println(
        "211 "
          + count
          + " "
          + first
          + " "
          + last
          + " "
          + name
          + " group selected");
      selectedGroup = name;
    } catch (AccessPoemException e) {
      writer.println("411 no such newsgroup");
      //writer.println("502 no permission");
    } catch (Exception e) {
      writer.println("411 no such newsgroup");
    }
  }

  private void quit() {
    writer.println("205 closing connection");
    try {
      socket.close();
      socket = null;
    } catch (Exception e) {}
    reset();
  }

  private Date getDateFrom(StringTokenizer tok) {
    try {
      String dateStr = tok.nextToken() + " " + tok.nextToken();
      boolean utc = (tok.hasMoreTokens());
      Date d = new Date();
      Date dt = null;
      if (dateStr.indexOf(' ') == 6) {
        dt = shortRFC977DateFormat.parse(dateStr);
      } else {
        dt = longRFC977DateFormat.parse(dateStr);
      }
      if (utc)
        dt =
          new Date(
            dt.getTime() + Calendar.getInstance().get(Calendar.ZONE_OFFSET));
      return dt;
    } catch (ParseException pe) {
      return null;
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  private void newgroups(StringTokenizer tok) {
    writer.println("231 list of new newsgroups follows");
    Enumeration boards = store.getBoards(getDateFrom(tok), null);
    if (boards != null) {
      while (boards.hasMoreElements()) {
        Board board = (Board)boards.nextElement();
        writer.println(
          store.prefix + "." + board.getName() + " " + board.getPurpose());
      }
    }
    writer.println(".");
  }

  private void list(StringTokenizer tok) {
    String wildmat = "*";
    if (tok.hasMoreTokens()) {
      String param = tok.nextToken();
      String[] unsupported =
        { "ACTIVE.TIMES", "DISTRIBUTIONS", "DISTRIB.PATS" };
      for (int i = 0; i < unsupported.length; i++) {
        if (unsupported[i].equalsIgnoreCase(param)) {
          writer.println("503 program error, function not performed");
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
            Board board = (Board)boards.nextElement();
            writer.println(
              store.prefix + "." + board.getName() + " " + board.getPurpose());
          }
        }
      } else if (param.equalsIgnoreCase("ACTIVE")) {
        writer.println("215 list of newsgroups follows");
        Enumeration boards = store.getBoards(null, wildmat);
        if (boards != null) {
          while (boards.hasMoreElements()) {
            Board board = (Board)boards.nextElement();
            writer.println(
              store.prefix
                + "."
                + board.getName()
                + " "
                + (board.getModeratedposting().booleanValue() ? "y" : "n"));
          }
        }
      }
    } else {
      writer.println("215 list of newsgroups follows");
      Enumeration boards = store.getBoards(null, null);
      if (boards != null) {
        while (boards.hasMoreElements()) {
          Board board = (Board)boards.nextElement();
          writer.println(
            store.prefix
              + "."
              + board.getName()
              + " "
              + (board.getModeratedposting().booleanValue() ? "y" : "n"));
          /*
          writer.println(
            store.prefix
              + "."
              + board.getName()
              + " "
              + (board.canPost(authInfo.user) ? "y" : "n"));
              */
        }
      }
    }
    writer.println(".");
  }

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

}