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

import java.io.*;
import java.sql.*;
import javax.mail.Part;
import javax.mail.Multipart;
import javax.mail.MessagingException;
import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import org.paneris.melati.boards.model.*;
import org.melati.poem.*;
import org.melati.util.*;



/**
 * A wrapper to make a handy interface to the database tables comprising
 * a set of messageboards
 */

class BoardStore {

  private Database database;
  private Log log;

  private org.melati.poem.User sender = null;
  private Recipient recipient = null;

  /**
   * A wrapper for the messageboards in a given database
   *
   * @param connMgr            our handle to the DBMS
   * @param database            the name of the database
   * @param log                  where to log trouble
   */

  public BoardStore(Database database, Log log,
                    final InternetAddress senderAddr,
                    final InternetAddress messAddr)
      throws IOException, MessagingException {
    this.database = database;
    this.log = log;
    sender = senderOfAddress(senderAddr);
    recipient = recipientOfAddress(messAddr);

    if (!recipient.board.canPost((org.paneris.melati.boards.model.User)sender)) {
      throw new MessagingException(
            "user `" + sender + "' " +
            "not authorised to post on this message board");
    }

    if (recipient.parentID != null) {
        Persistent parent=null;
        try {
          parent = database.getTable("message").getObject(recipient.parentID);
        }
        catch (NoSuchRowPoemException e) {
          throw new MessagingException(
                "parent message `" + recipient.parentID + "' " + "not found");
        }

        if (!((Message)parent).getBoard_unsafe().equals(recipient.board.troid())) {
          throw new MessagingException(
                "parent message `" + recipient.parentID + "' " +
                "is not on this board `"+ recipient.board.getName_unsafe() +"'");
        }
    }
  }

  /**
   * Tidy up
   */

  public void close() {
  }

  protected void finalize() {
    close();
  }

  /**
   * Return the user (poster) ID with a given email address
   * <P>
   * This is called in the constructor, and need to be called within
   * database.inSession()
   *
   * @param email            their <TT>x@y.z</TT> address, case-insensitive
   *
   * @throws MessagingException e.g. if the user doesn't exist
   * @throws SQLException       if something fails
   *
   * @returns                   any object which will be recognised later by
   *                            <TT>messageAccept</TT>
   *
   * @see #messageAccept
   */

  private org.melati.poem.User senderOfAddress(InternetAddress email)
                                              throws MessagingException {

    sender = (org.melati.poem.User)database.getTable("user").
                getColumn("email").firstWhereEq(email.getAddress());

    if (sender == null)
      throw new MessagingException(
            "user `" + email + "' " +
            "not authorised to post on this message board");

    return sender;
  }

  public org.melati.poem.User getSender() {
    return sender;
  }

  class Recipient {
    Board board;
    Integer parentID;
  }

  /**
   * Return the message board (and parent message) ID with a given email
   * address
   * <P>
   * This is called in the constructor, and need to be called within
   * database.inSession()
   *
   * @param email            has the form
   *                            <I>[parentID.]boardName@domain</I>;
   *                            <I>domain</I> is ignored, and what is returned
   *                            identifies both the board and the parent message
   *
   * @throws MessagingException e.g. if the board doesn't exist
   * @throws SQLException       if something fails
   *
   * @returns                   any object which will be recognised later by
   *                            <TT>messageAccept</TT>
   *
   * @see #messageAccept
   */

  public Recipient recipientOfAddress(InternetAddress email)
                                  throws MessagingException {

      String to = email.getAddress();

      Recipient recipient = new Recipient();
      String boardName;

      int atIndex = to.indexOf('@');
      if (atIndex == -1) atIndex = to.length() - 1;
      int dotIndex = to.indexOf('.');
      if (dotIndex > 0 && dotIndex < atIndex) {
        boardName = to.substring(dotIndex + 1, atIndex);
        try {
          recipient.parentID = Integer.valueOf(to.substring(0, dotIndex));
        }
        catch (NumberFormatException e) {
          throw new MessagingException("illegal parent message ID `" +
                                       to.substring(0, dotIndex) + "'");
        }
      }
      else {
        boardName = to.substring(0, atIndex);
        recipient.parentID = null;
      }

      Persistent board = database.getTable("board").
                             getColumn("name").firstWhereEq(boardName);

      if (board == null)
        throw new MessagingException("unknown messageboard `" +
                                     boardName + "'");
      
      recipient.board = (Board)board;
      return recipient;
  }

  /**
   * Write an attachment into the attachment table and filesystem
   * <P>
   * This is called from within messageAccept, and so is within 
   * database.inSession()
   */

  private String attachmentWrite(final Message message, final String fileName,
                               String contentType,
                               final byte[] content)
          throws Exception {

    if (!((Board)database.getTable("board").
            getObject(message.getBoard_unsafe())).
                getAttachmentsallowed().booleanValue())
       return "[[ Attachments are not allowed on this board ]]\n";


    // sort out the MIME type

    int semicolonIndex = contentType.indexOf(';');
    if (semicolonIndex != -1)
      contentType = contentType.substring(0, semicolonIndex);

    final AttachmentType type =
        ((AttachmentTypeTable)database.getTable("attachmenttype")).
            ensure(contentType);

    Attachment att = (Attachment)database.getTable("attachment").create(
      new Initialiser() {
        public void init(Persistent object)
              throws AccessPoemException, ValidationPoemException {
          object.setRaw("message", message.troid());
          ((Attachment)object).setFilename_unique(fileName);
          object.setRaw("size", new Integer(content.length));
          object.setRaw("type", type.troid());
        }
      }
    );

    try {
      att.writeData_unsafe(content);
    }
    catch (Exception e) {
      log.error("Exception trying to store an attachment:" +
                ExceptionUtils.stackTrace(e));
      att.delete_unsafe();
      return "[[ Attachment `" + fileName + "'could not be saved: " +
              e.toString() + " ]]\n";
    }
    return "";
  }

  private Object getContent(MimeMessage message, Part part)
      throws MessagingException, IOException {
    if (part.getContentType().startsWith("text/plain")) {
      String[] mailer = message.getHeader("X-Mailer");
      if (mailer != null && mailer.length > 0 &&
          mailer[0].startsWith("Mozilla 4.6"))
        part.setHeader("Content-Transfer-Encoding", "8bit");
    }

    return part.getContent();
  }


  /**
   * Stick a message into the messageboards
   *
   * @param text            the raw (un-decoded) data of the message
   *
   * @throws Exception            because the routines it call do ...
   *
   * @returns                   a message ID for the message
   */

  public Integer messageAccept(final InputStream text) throws Exception {

    final MimeMessage message = new MimeMessage(
      Session.getDefaultInstance(System.getProperties(), null),
      text);

    // write the record straight away in order to get an id number for the
    // attachments (if any)

    final String subject = message.getSubject() == null ?
                                     "(no subject)" :
                                     message.getSubject();

    Message m = (Message)((MessageTable)database.getTable("message")).create(
      new Initialiser() {
        public void init(Persistent object)
              throws AccessPoemException, ValidationPoemException {
          object.setRaw("subject", subject);
          object.setRaw("board", recipient.board.getTroid());
          object.setRaw("parent", recipient.parentID);
          object.setRaw("author", sender.getTroid());
          object.setRaw("deleted", Boolean.FALSE);
          object.setRaw("body", ""); // can't be null
        }
      }
    );


    // process attachments

    StringBuffer bodyText = new StringBuffer(100);

    Object content = getContent(message, message);
    if (content instanceof String) {
      // it's text
      bodyText.append((String)content);
    }
    else if (content instanceof Multipart) {
      Multipart parts = (Multipart)content;
      for (int p = 0; p < parts.getCount(); ++p) {
        Part part = parts.getBodyPart(p);

        // can it go in line?

        Object partContent = getContent(message, part);

        if (partContent instanceof String && part.getFileName() == null)
          bodyText.append((String)partContent);
        else
          bodyText.append(attachmentWrite(m, part.getFileName(),
                                          part.getContentType(),
                             IoUtils.slurp(part.getInputStream(), 1024)));
      }
    }
    else {
      // message is one, non-text item
      bodyText.append(attachmentWrite(m, null, message.getContentType(),
                      IoUtils.slurp(message.getInputStream(), 100)));
    }

    // write the message down and email it out
    // put a 7k limit on message size
    String messageText = bodyText.toString();
    if (messageText.length() > 7168) {
      String attachmentString = attachmentWrite(m, "", "truncation",
                                                messageText.getBytes());
      messageText = messageText.substring(0,7168);
      messageText += "\n\nThis message has been truncated, the complete "+
                     "message has been stored as an attachment.\n" +
                     attachmentString;
    }

    m.setBody(messageText);
    m.distribute();

    return m.troid();
  }
}


