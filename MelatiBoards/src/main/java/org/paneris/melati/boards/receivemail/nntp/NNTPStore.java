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
 *     Vasily Pozhidaev  <vasilyp At paneris.org>
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
import org.melati.poem.PoemDatabase;
import org.melati.poem.ValidationPoemException;
import org.melati.util.ExceptionUtils;
import org.melati.util.IoUtils;
import org.paneris.melati.boards.BoardAdmin;
import org.paneris.melati.boards.model.Attachment;
import org.paneris.melati.boards.model.AttachmentType;
import org.paneris.melati.boards.model.Board;
import org.paneris.melati.boards.model.BoardsDatabaseTables;
import org.paneris.melati.boards.model.Message;
import org.paneris.melati.boards.model.MessageTable;
import org.paneris.melati.boards.model.User;
import org.paneris.melati.boards.receivemail.Log;

/**
 * Boards database broker for a particular session.
 *
 * @author  Vasily Pozhidaev <vasilyp@paneris.org>
 * TODO generalize and make an interface (like BoardStore)
 */
public class NNTPStore {

  private Log log;
  private BoardsDatabaseTables db = null;
  String prefix = null;
  String msgIdSuffix = null;

  private NNTPStore() {
  }
  
  /**
   * Constructor.
   */
  public NNTPStore(String database, String prefix, String nntpIdentifier)
    throws Exception {
      db = (BoardsDatabaseTables)LogicalDatabase.getDatabase(database);
      ((PoemDatabase)db).setLogSQL(false);
    this.prefix = prefix;
    msgIdSuffix = "$msg@" + nntpIdentifier;
  }

  /**
   * Return the DB. 
   * 
   * @return Returns the db.
   */
  public BoardsDatabaseTables getDb() {
    return db;
  }

  /**
   *  Return a {@link Board} given a newsgroup name.
   * 
   * @param name
   * @return a board object or null
   * @throws AccessPoemException
   * @throws UnknownNewsGroupException 
   */
  public Board getBoard(String name) 
      throws AccessPoemException, UnknownNewsGroupException {
    try {
      return resolveTargetBoard(name);
    } catch (AccessPoemException e) {
      throw e;
    }
  }

   
  /**
   * Format a date taking into account DB peculiarities.
   * 
   * @param date A Date to format
   * @return the formatted string
   */
  private String formatTimestamp(Date date) {
    String dbmsClassName = ((PoemDatabase)db).getDbms().getClass().getName();
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
   * Return boards : either all boards, those containing messages more 
   * recent than the <code>from</code> date or whose names match the 
   * regexp passed in.  
   * 
   * @param from     Earliest date
   * @param wildmat  Regular Expression to match
   * @return an Enumeration of Board objects
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
        String msgTableName = db.getMessageTable().getName();
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
  * @return an Enumeration of Messages 
  */
  protected Enumeration getRange(String range, Board selectedBoard)
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
      start = Integer.parseInt(range) - 1;
      end = start; 
    } else {
      start = Integer.parseInt(range.substring(0, idx)) - 1;
      if (idx + 1 == range.length())
        end = selectedBoard.getLastMessageId();
      else
        end = Integer.parseInt(range.substring(idx + 1)) - 1;
    }
    MessageTable messageTable = (MessageTable)selectedBoard.
                                    getDatabase().getTable("message");
    Enumeration result =
      messageTable.selection(
          messageTable.troidColumn().quotedName() // "id"
          + " BETWEEN "
          + start
          + " AND "
          + end
          + " AND "
          + messageTable.getBoardColumn().quotedName()
          +" = "
          + selectedBoard.getTroid());
    

    return result;
  }

  /**
   * Retrieve the messages References.
   * 
   * @param msg the Message 
   * @return a string of references in NNTP format
   */
  String getReferences(Message msg) {
    String references = "";
    /*
    assembling list of references, seems not actually required...
    Stack refs = new Stack();
    //assembling reference list
    NNTPMessage parent = new NNTPMessage(msg);
    while((parent = parent.getParent())!=null) {
      refs.push(parent.getArticleId());
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
          getArticleId(new NNTPMessage(parent).getNntpIdInt());
    }
    return references;
  }

  /**
   * 
   * 
   * Sending 430 response (no such article) seems not to be 
   * required (checked with Mozilla and news4.fido7.ru)
   * Mozilla shows error message, but if no headers have been 
   * found Mozilla sends a 221 response.
   * Mozilla says that no messages found.
   *
   * @todo check with other clients
   * @todo move to NNTPSession
   * @see NNTPSession#xpat
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
//        boolean hasMatches = false;
        while (messages.hasMoreElements()) {
          Message msg = (Message)messages.nextElement();
          NNTPMessage nntpMsg = new NNTPMessage(msg);
          boolean matched = false;
          String matchedHeader = null;
          if ("subject".equalsIgnoreCase(header)) {
            matchedHeader = msg.getSubject();
            matched = matcher.matches(matchedHeader, p);
          } else if ("from".equalsIgnoreCase(header)) {
            matchedHeader =
              nntpMsg.getFrom();
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
              nntpMsg.getNntpId() + " " + matchedHeader);
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

  /**
   * Retrieve a Board given a newsgroup name for it.
   * 
   * @param newsGroup name in org.paneris.test format
   * @return the corresponding Board
   * @throws UnknownNewsGroupException
   */
  protected Board resolveTargetBoard(String newsGroup) 
      throws UnknownNewsGroupException {
    if (!newsGroup.startsWith(prefix)) 
      throw new UnknownNewsGroupException("Expecting newsgroup name to start with:'" + prefix + "'");
    String boardName = newsGroup.substring(prefix.length() + 1).trim();
    return (Board)db.getBoardTable().getColumn("name").firstWhereEq(
      boardName);
  }

 /**
  * Fill in the <code>User</code>.
  * Note that the User is already initialised to Guest, 
  * so will bever be null. 
  * 
  * @param authInfo the initialised authinfo collected from commands
  */
  public void establishUser(NNTPSession.AuthInfo authInfo) {
    User user = null;
    try {
      user =
        (User)db.getUserTable().getColumn("login").firstWhereEq(
          authInfo.getLogin());
      if (user != null) {
        if (user.getPassword_unsafe().equals(authInfo.getPassword())) {
          authInfo.setUser(user);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

 /**
  * Return a NNTPMessage given its NNTP id.
  * 
  * @todo output attachments
  */
  public NNTPMessage getArticle(int id) throws AccessPoemException {
    int troid = id - 1;
    Message message = null;
    try {
      message = (Message)db.getMessageTable().firstSelection(
          db.getMessageTable().troidColumn().quotedName() // "id"
          + "=" 
          + troid);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new NNTPMessage(message);
  }

  /**
   * Return a full Article Id given its NNTP id.
   * 
   * @return the NNTP Article Id
   */
  public String getArticleId(int id) {
    return "<" + id + msgIdSuffix + ">";

  }


  /**
   * @see org.paneris.melati.boards.receivemail.BoardStoreImpl#attachmentWrite
   */
  protected String attachmentWrite(final Message message,
                                   final String fileName, 
                                   String contentType, 
                                   final byte[] content)
    throws Exception {

    if (!((Board)db.getBoardTable().getObject(message.getBoard_unsafe()))
      .getAttachmentsallowed()
      .booleanValue())
      return "[[ Attachments are not allowed on this board ]]\n";

    // sort out the MIME type

    int semicolonIndex = contentType.indexOf(';');
    if (semicolonIndex != -1)
      contentType = contentType.substring(0, semicolonIndex);

    final AttachmentType type =
      db.getAttachmentTypeTable().ensure(contentType);

    Attachment att =
      (Attachment)db.getAttachmentTable().create(new Initialiser() {
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
   * @see org.paneris.melati.boards.receivemail.BoardStoreImpl#messageAccept
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
          db.getMessageTable().create(new Initialiser() {
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
            sender,
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
