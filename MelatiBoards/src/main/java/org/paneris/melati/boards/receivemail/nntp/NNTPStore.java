/*
 * NNTPStore.java
 *
 * Created on Nov 3, 2002, 6:56 PM
 */

package org.paneris.melati.boards.receivemail.nntp;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;

import org.apache.oro.text.GlobCompiler;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Matcher;
import org.melati.LogicalDatabase;
import org.melati.poem.AccessPoemException;
import org.melati.poem.Initialiser;
import org.melati.poem.Persistent;
import org.melati.poem.ValidationPoemException;
import org.melati.util.ExceptionUtils;
import org.melati.util.IoUtils;
import org.paneris.melati.boards.BoardAdmin;
import org.paneris.melati.boards.model.Attachment;
import org.paneris.melati.boards.model.AttachmentType;
import org.paneris.melati.boards.model.AttachmentTypeTable;
import org.paneris.melati.boards.model.Board;
import org.paneris.melati.boards.model.BoardsDatabase;
import org.paneris.melati.boards.model.Message;
import org.paneris.melati.boards.model.MessageTable;
import org.paneris.melati.boards.model.User;
import org.paneris.melati.boards.receivemail.Log;

/**
 * Boards database broker for particular session
 * @author  Vasily Pozhidaev <vasilyp@paneris.org>
 * @todo generalize and make an interface (like BoardStore)
 */
public class NNTPStore {

  private Log log;
  public BoardsDatabase db = null;
  String prefix = null;
  String msgIDSuffix = null;

  public NNTPStore(String database, String prefix, String nntpIdentifier)
    throws Exception {
    db = (BoardsDatabase)LogicalDatabase.getDatabase(database);
    db.setLogSQL(false);
    this.prefix = prefix;
    msgIDSuffix = "$msg@" + nntpIdentifier;
  }

  /**
   *  Return a {@link Board} given a newsgroup name.
   * 
   * @param name
   * @return a board object or null
   * @throws AccessPoemException
   */
  public Board getBoard(String name) throws AccessPoemException {
    try {
      return resolveTargetBoard(name);
    } catch (AccessPoemException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private String formatTimestamp(Date date) {
    String dbmsClassName = db.getDbms().getClass().getName();
    if (dbmsClassName.indexOf("Postgresql") != -1) {
      return "'"
        + new SimpleDateFormat("MM-dd-yyyy HH:mm:ss Z").format(date)
        + "'";
    } else if (dbmsClassName.indexOf("SQLServer") != -1) {
      return "'"
        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(date)
        + "'";
    }
    return "'" + new java.sql.Timestamp(date.getTime()).toString() + "'";
  }

  /**
   *  Return boards : either all boards, those containing messages more 
   * recent than the <code>from</code> date or whose names match the 
   * regexp psssed in.  
   * 
   * @param from
   * @param wildmat
   * @return
   * @todo there should be a way to display newly created empty boards
   * 
   */
  public Enumeration getBoards(Date from, String wildmat) {
    try {
      String criteria = null;
      Enumeration result = null;
      if (from != null) {
        String boardColName =
          db.getMessageTable().getBoardColumn().quotedName();
        String msgTableName = db.quotedName(db.getMessageTable().getName());
        String dateColName = db.getMessageTable().getDateColumn().quotedName();
        String troidColName = db.getBoardTable().troidColumn().quotedName();
        criteria =
          troidColName
            + " in (SELECT "
            + boardColName
            + " FROM "
            + msgTableName
            + " WHERE "
            + dateColName
            + " >= "
            + formatTimestamp(from)
            + ")";
        result = db.getBoardTable().selection(criteria);
      } else {
        result = db.getBoardTable().selection();
      }

      if (wildmat != null) {
        Pattern p = new GlobCompiler().compile(wildmat);
        Perl5Matcher matcher = new Perl5Matcher();
        Vector boards = new Vector();
        while (result.hasMoreElements()) {
          Board element = (Board)result.nextElement();
          if (matcher.matches(prefix + "." + element.getName(), p)) {
            boards.add(element);
          }
        }
        result = boards.elements();
      }
      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

 /**
  * @todo implement ranges starting with the character "&lt;" 
  */
  private Enumeration getRange(String range, Board selectedBoard)
    throws SQLException {
    // check for msg id
    if (selectedBoard == null)
      return null;
    if (range == null)
      range = "" + selectedBoard.getFirstMessageId();
    //implement this
    if (range.startsWith("<")) {
      return null;
    }
    int start = -1;
    int end = -1;
    int idx = range.indexOf('-');
    if (idx == -1) {
      start = end = Integer.parseInt(range) - 1;
    } else {
      start = Integer.parseInt(range.substring(0, idx)) - 1;
      if (idx + 1 == range.length())
        end = selectedBoard.getLastMessageId();
      else
        end = Integer.parseInt(range.substring(idx + 1)) - 1;
    }
    Enumeration result =
      selectedBoard.getDatabase().getTable("message").selection(
        "\"id\" BETWEEN "
          + start
          + " AND "
          + end
          + " AND \"board\"="
          + selectedBoard.getTroid());
    return result;
  }

  String getReferences(Message msg, String suffix) {
    String references = "";
    /*
    assembling list of references, seems not actually required...
    Stack refs = new Stack();
    //assembling reference list
    Message parent = msg;
    while((parent=parent.getParent())!=null) {
      refs.push("<"+String.valueOf(parent.getTroid().intValue()+1)+suffix+">");
    }
    while(!refs.empty()) {
      references += " "+(String)refs.pop();
    }
    references.trim();
    */
    //but simply do:
    Message parent = msg.getParent();
    if (parent != null) {
      references =
        "<" + String.valueOf(parent.getTroid().intValue() + 1) + suffix + ">";
    }
    return references;
  }

 /**
  * @todo move to NNTPSession
  */
  public void printOverview(
    String range,
    PrintWriter writer,
    String selectedBoard) {
    try {
      Board brd = resolveTargetBoard(selectedBoard);
      Enumeration messages = getRange(range, brd);
      if (messages != null) {
        writer.println("224 Overview information follows");
        while (messages.hasMoreElements()) {
          Message msg = (Message)messages.nextElement();
          long byteCount = -1;
          long lineCount = -1;
          int id = (msg.getTroid().intValue() + 1);
          String msgID = "<" + id + msgIDSuffix + ">";
          String references = getReferences(msg, msgIDSuffix);
          String reply = id + "\t";
          reply += msg.getSubject() + "\t";
          reply += msg.getAuthor().getName()
            + "<"
            + msg.getAuthor().getEmail()
            + ">\t";
          reply += NNTPSession.nntpDateFormat.format(msg.getDate()) + "\t";
          reply += msgID + "\t";
          reply += references + "\t";
          reply += ((byteCount == -1) ? 300 : byteCount) + "\t";
          reply += lineCount + "";
          writer.println(reply);
        }
        writer.println(".");
      } else {
        writer.println("420 No article(s) selected");
      }
    } catch (AccessPoemException e) {
      writer.println("502 No permission");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * sending 430 response (no such article) seems not required (checked with 
   * Mozilla and news4.fido7.ru)
   * Mozilla shows error message, but if no headers has been founded Mozilla 
   * and 221 response sent Mozilla says that no messages found
   *
   * @todo check with other clients
   * @todo move to NNTPSession
   */
  public void xpat(
    String header,
    String range,
    String wildmat,
    String selectedGroup,
    PrintWriter writer) {
    try {
      Pattern p = new GlobCompiler().compile(wildmat);
      Perl5Matcher matcher = new Perl5Matcher();
      Board brd = resolveTargetBoard(selectedGroup);
      Enumeration messages = getRange(range, brd);
      writer.println("221 " + header + " matches follow");
      if (messages != null) {
        boolean hasMatches = false;
        while (messages.hasMoreElements()) {
          Message msg = (Message)messages.nextElement();
          boolean matched = false;
          String matchedHeader = null;
          if ("subject".equalsIgnoreCase(header)) {
            matchedHeader = msg.getSubject();
            matched = matcher.matches(matchedHeader, p);
          } else if ("from".equalsIgnoreCase(header)) {
            matchedHeader =
              msg.getAuthor().getName() + " " + msg.getAuthor().getEmail();
            matched = matcher.matches(matchedHeader, p);
          } else {
            matched = false;
          }
          if (matched) {
            /*if(!hasMatches) {
              writer.println("221 "+header+" matches follow");
              hasMatches = true;
            }*/
            writer.println(
              (msg.getTroid().intValue() + 1) + " " + matchedHeader);
          }
        }
        /*if(hasMatches)
          writer.println(".");
        else
          writer.println("430 no such article");
         */
      }
      /*else {
        writer.println("430 no such article");
      }*/
      writer.println(".");
    } catch (AccessPoemException e) {
      writer.println("502 no permission");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Board resolveTargetBoard(String newsGroup) {
    String boardName = newsGroup.substring(prefix.length() + 1);
    return (Board)db.getTable("board").getColumn("name").firstWhereEq(
      boardName);
  }

  public void establishUser(NNTPSession.AuthInfo authInfo) {
    User user = null;
    try {
      user =
        (User)db.getTable("user").getColumn("login").firstWhereEq(
          authInfo.getLogin());
      if (user != null) {
        if (user.getPassword_unsafe().equals(authInfo.getPassword())) {
          authInfo.user = user;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

 /**
  * @todo output attachments
  */
  public Message getArticle(int id) throws AccessPoemException {
    Message message = null;
    try {
      message = (Message)db.getTable("message").firstSelection("\"id\"=" + id);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return message;
  }

  /**
   * @see org.paneris.melati.boards.receivemail.BoardStoreImpl#attachmentWrite()
   */
  protected String attachmentWrite(
    final Message message,
    final String fileName,
    String contentType,
    final byte[] content)
    throws Exception {

    if (!((Board)db.getTable("board").getObject(message.getBoard_unsafe()))
      .getAttachmentsallowed()
      .booleanValue())
      return "[[ Attachments are not allowed on this board ]]\n";

    // sort out the MIME type

    int semicolonIndex = contentType.indexOf(';');
    if (semicolonIndex != -1)
      contentType = contentType.substring(0, semicolonIndex);

    final AttachmentType type =
      ((AttachmentTypeTable)db.getTable("attachmenttype")).ensure(contentType);

    Attachment att =
      (Attachment)db.getTable("attachment").create(new Initialiser() {
      public void init(Persistent object)
        throws AccessPoemException, ValidationPoemException {
        object.setRaw("message", message.troid());
        ((Attachment)object).setFilename_unique(fileName);
        object.setRaw("size", new Integer(content.length));
        object.setRaw("type", type.troid());
      }
    });

    try {
      att.writeData_unsafe(content);
    } catch (Exception e) {
      log.error(
        "Exception trying to store an attachment:"
          + ExceptionUtils.stackTrace(e));
      att.delete_unsafe();
      return "[[ Attachment `"
        + fileName
        + "'could not be saved: "
        + e.toString()
        + " ]]\n";
    }
    return "";
  }

  /**
   * @see org.paneris.melati.boards.receivemail.BoardStoreImpl#messageAccept()
   */
  public void postMessage(final MimeMessage message, final User sender)
    throws Exception {
    String newsgroups = message.getHeader("newsgroups")[0];
    if (newsgroups == null) 
       throw new Exception("newsgroups header missing");
      
    StringTokenizer strtok = new StringTokenizer(newsgroups, ", ", false);
    final Board board = resolveTargetBoard(strtok.nextToken());
    try {
      //need to get it according to encoding rules.
      final String subject = message.getSubject();
      Message m =
        (Message)
          ((MessageTable)db.getTable("message")).create(new Initialiser() {
        public void init(Persistent object)
          throws AccessPoemException, ValidationPoemException {
          object.setRaw("subject", subject);
          object.setRaw("board", board.getTroid());
          Integer parent = null;
          try {
            String[] refs = message.getHeader("references");
            if (refs != null) {
              StringTokenizer refsTok =
                new StringTokenizer(refs[0], "<> \t", false);
              //last reference is reference to original (parent) message
              int count = refsTok.countTokens();
              for (int i = 1; i <= count; i++) {
                String ref = refsTok.nextToken();
                if (i == count) {
                  parent =
                    new Integer(
                      Integer
                        .decode(ref.substring(0, ref.indexOf('$')))
                        .intValue()
                        - 1);
                }
              }
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
          object.setRaw("parent", parent);
          object.setRaw("author", sender.getTroid());
          object.setRaw("deleted", Boolean.FALSE);
          object.setRaw("body", "");
        }
      });
      try {
        System.err.println(message.getContentType());
        Object content = message.getContent();
        StringBuffer bodyText = new StringBuffer(100);
        if (content instanceof String) {
          bodyText.append((String)content);
          System.err.println("content is string");
        } else if (content instanceof Multipart) {
          System.err.println("content is multipart");
          Multipart parts = (Multipart)content;
          for (int p = 0; p < parts.getCount(); ++p) {
            Part part = parts.getBodyPart(p);
            Object partContent = part.getContent();
            if (partContent instanceof String && part.getFileName() == null)
              bodyText.append((String)partContent);
            else
              bodyText.append(
                attachmentWrite(
                  m,
                  part.getFileName(),
                  part.getContentType(),
                  IoUtils.slurp(part.getInputStream(), 1024)));
          }
        } else {
          System.err.println(content.getClass().getName());
          System.err.println("one nontext item");
          bodyText.append(
            new String(IoUtils.slurp(message.getInputStream(), 100)));
        }

        // write the message down and email it out
        // put a 7k limit on message size
        String messageText = bodyText.toString();
        if (messageText.length() > 7168) {
          String attachmentString =
            attachmentWrite(m, "", "truncation", messageText.getBytes());
          messageText = messageText.substring(0, 7168);
          messageText += "\n\nThis message has been truncated, the complete "
            + "message has been stored as an attachment.\n"
            + attachmentString;
        }

        m.setBody(messageText);

        if (m.getApproved().booleanValue() == true) {
          m.distribute();
        } else {
          //todo: should this be done here?
          BoardAdmin.emailNotification(
            m.getBoard(),
            (org.paneris.melati.boards.model.User)sender,
            "MessageReceived");
        }
        //BUG: M$ Outlook doesn't send used encoding in headers
        //todo: should be an interface to inject heuristic encoding detector or
        //simple by-contract detector
        /*
        String enc = "KOI8_R";
        BufferedReader reader = new BufferedReader(
        new InputStreamReader(message.getInputStream(), enc));
        StringBuffer sb = new StringBuffer();
        String str = null;
        while((str=reader.readLine()) != null) {
          sb.append(str);
        }
        String body = sb.toString();
        */
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    } catch (AccessPoemException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

/*
  public static void main(String[] args) {
    Date date = new Date(2003, 7, 9);
    System.err.println(date.getTime());
    System.err.println(new java.sql.Date(date.getTime()).getTime());
  }
*/
}
