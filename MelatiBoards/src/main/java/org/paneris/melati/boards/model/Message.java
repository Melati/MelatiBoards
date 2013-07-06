/*
 * $Source$
 * $Revision$
 *
 * Copyright (C) 2000 Myles Chippendale
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
 *     Mylesc Chippendale <mylesc At paneris.org>
 *     http://paneris.org/
 *     29 Stanley Road, Oxford, OX4 1QY, UK
 */

package org.paneris.melati.boards.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.TimeZone;
import java.util.Vector;

import org.melati.poem.AccessPoemException;
import org.melati.poem.AccessToken;
import org.melati.poem.CachedCount;
import org.melati.poem.Capability;
import org.melati.poem.CreationAccessPoemException;
import org.melati.poem.PoemTask;
import org.melati.poem.ValidationPoemException;
import org.melati.util.ChildrenDrivenMutableTree;
import org.melati.poem.util.EnumUtils;
import org.melati.poem.util.StringUtils;
import org.melati.poem.Treeable;
import org.paneris.melati.boards.model.generated.MessageBase;

/**
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>Persistent</code> <code>Message</code> object.
 * 
 * <p> 
 * Description: 
 *   A message posted to a message board. 
 * </p>
 * 
 * <table> 
 * <tr><th colspan='3'>
 * Field summary for SQL table <code>Message</code>
 * </th></tr>
 * <tr><th>Name</th><th>Type</th><th>Description</th></tr>
 * <tr><td> id </td><td> Integer </td><td> &nbsp; </td></tr> 
 * <tr><td> board </td><td> Board </td><td> The board this message belongs to 
 * </td></tr> 
 * <tr><td> date </td><td> Timestamp </td><td> The date and time at which 
 * this message was posted </td></tr> 
 * <tr><td> subject </td><td> String </td><td> The subject line of this 
 * message </td></tr> 
 * <tr><td> author </td><td> User </td><td> Author of this message </td></tr> 
 * <tr><td> parent </td><td> Message </td><td> The message to which this 
 * message is a follow-up </td></tr> 
 * <tr><td> body </td><td> String </td><td> The main content of this message 
 * </td></tr> 
 * <tr><td> deleted </td><td> Boolean </td><td> A deleted message cannot be 
 * viewed or displayed on lists </td></tr> 
 * <tr><td> approved </td><td> Boolean </td><td> A message must be approved 
 * by a manager of the board before it can be viewed (if the board has 
 * moderated postings) </td></tr> 
 * </table> 
 * 
 * see org.melati.poem.prepro.TableDef#generatePersistentJava 
 */
public class Message extends MessageBase implements Treeable {

 /**
  * Constructor 
  * for a <code>Persistent</code> <code>Message</code> object.
  * <p>
  * Description: 
  *   A message posted to a message board. 
  * </p>
  * 
  * see org.melati.poem.prepro.TableDef#generatePersistentJava 
  */
  public Message() { 
    super();
}

  // programmer's domain-specific code here
  

  private static DateFormat localeFormatter =
    new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
  static {
    localeFormatter.setTimeZone(TimeZone.getTimeZone("Europe/London"));
  }

  /**
   * @return the localised date
   */
  public String getLondonDate() {
    return localeFormatter.format(getDate_unsafe());
  }

  /**
   * Distribute the message
   */
  public void distribute() {
    (new DistributeThread(this)).start();
  }

  /**
   * Approve the message and add it to thread.
   */
  public void approve() {
    setApproved(Boolean.TRUE);
    if (getParent() == null)
      getBoard().addThread(this, true);
    else
      getBoard().addToParent(this, getParent());
  }

  String[] lines = null;
  /**
   * @return the lines in an Array
   */
  public String[] getLines() {
    if (lines == null)
      lines = StringUtils.split(getBody_unsafe(), '\n');
    return lines; 
  }
  /**
   * @param indent the characters to indent with
   * @return the indented body
   */
  public String IndentBody(String indent) {
    StringBuffer ret = new StringBuffer(
                    getBody_unsafe().length() +
                    getLines().length * indent.length());
    for(int i = 0; i < getLines().length; i++) {
      ret.append(indent);
      ret.append(lines[i]);
    }
    return ret.toString();
  }

  /**
   * @return the indented body
   */
  public String IndentBody() {
    return IndentBody(">");
  }

  /**
   * @return the attachments
   */
  public Enumeration getAttachments() {
    return getBoardsDatabaseTables().getAttachmentTable().getColumn("message").
             referencesTo(this);
  }

  CachedCount attachments = null;

  /**
   * @return the number of attachments
   */
  public int getAttachmentCount() {

    if (attachments == null)
      attachments = getBoardsDatabaseTables().getAttachmentTable().
         cachedCount(getBoardsDatabaseTables().getAttachmentTable().
           getMessageColumn().eqClause(this.troid()));
    return attachments.count();
  }

  /**
   * @return whether there are attachments
   */
  public boolean hasAttachments() {
    return getAttachmentCount() > 0;
  }

  /** 
   * {@inheritDoc}
   * @see org.melati.util.Treeable#getChildren()
   */
  public Treeable[] getChildren() {
    Enumeration kidsEnum = getTable().
      selection(getMessageTable().getParentColumn().eqClause(troid())
                + " AND " +
                getMessageTable().getApprovedColumn().eqClause(Boolean.TRUE)
                + " AND " +
                getMessageTable().getDeletedColumn().eqClause(Boolean.FALSE));

    Vector kidsVector = EnumUtils.vectorOf(kidsEnum);
    Treeable[] kidsArray = new Treeable[kidsVector.size()];
    kidsVector.copyInto(kidsArray);
    // cater for deleted children
    return kidsArray;
  }

  /**
   * @return the root of the Thread tree
   */
  public Message getThreadRoot() {
    Message current = this;
    while (current.getParent() != null)
      current = current.getParent();
    return current;
  }

  /**
   * @return the thread as a tree
   */
  public ChildrenDrivenMutableTree getThread() {
    return getBoard().threadWithRoot(getThreadRoot());
  }

  
  /**
   * If we delete a message, recompute the messages for the board. 
   * {@inheritDoc}
   * @see org.paneris.melati.boards.model.generated.MessageBase#setDeleted(java.lang.Boolean)
   */
  public void setDeleted(Boolean cooked)
      throws AccessPoemException, ValidationPoemException {
    if (getDeleted() == Boolean.FALSE && cooked == Boolean.TRUE) {
      getBoard().removeAndSquash(this);
      
      // sort out the kids!
      Enumeration kidsEnum = getTable().
         selection(getMessageTable().getParentColumn().eqClause(troid()));
      while (kidsEnum.hasMoreElements()) {
        Message child = (Message)kidsEnum.nextElement();
        child.setParent(getParent());
      }
    }
    super.setDeleted(cooked);
  }

  /**
   * A user can read a message if they can read messages on the
   * relevant board
   */

  public void assertCanRead(AccessToken token)
     throws AccessPoemException {
    if (token == AccessToken.root) return;
    try {
      Board b = getBoardsDatabaseTables().getBoardTable().
                   getBoardObject(getBoard_unsafe());
      if (!b.canViewMessages((User)token))
        throw new AccessPoemException(token, new Capability("Member"));
    } catch (ClassCastException e) {
        throw new AccessPoemException(token, new Capability("Member"));
    }
  }

  /**
   * A user can update a message if they are a manager for the
   * relevant board
   */
  public void assertCanWrite(AccessToken token)
      throws AccessPoemException {
    if (token == AccessToken.root) return;
    try {
      Board b = getBoardsDatabaseTables().getBoardTable().
                   getBoardObject(getBoard_unsafe());
      if (!b.canManage((User)token) &&
          !getAuthor_unsafe().equals(((User)token).troid()))
        throw new AccessPoemException(token, new Capability("Logged In"));
    } catch (ClassCastException e) {
        throw new AccessPoemException(token, new Capability("Logged In"));
    }

  }

  /**
   * A user can create a message if they have post permissions for the
   * relevant board.
   */
  public void assertCanCreate(AccessToken token)
      throws AccessPoemException {
    if (token == AccessToken.root) return;
    try {
      Board b = getBoardsDatabaseTables().getBoardTable().
                    getBoardObject(getBoard_unsafe());
     if (!b.canPost((User)token))
        throw new CreationAccessPoemException(getTable(), token,
                                            new Capability("Logged in"));
     } catch (ClassCastException e) {
        throw new CreationAccessPoemException(getTable(), token,
                                            new Capability("Member"));
    }
  }

  /** 
   * {@inheritDoc}
   * @see org.paneris.melati.boards.model.generated.MessageBase#setParentTroid(java.lang.Integer)
   */
  public void setParentTroid(Integer raw)
      throws AccessPoemException {
    getMessageTable().getParentColumn().getType().assertValidRaw(raw);
    writeLock();


    // A message has to be on the same board as it's parent, if one is
    // set
    if (raw != null && !getBoard_unsafe().equals(
            getMessageTable().getMessageObject(raw).getBoard_unsafe()))
      throw new MessageAndParentOnDifferentBoardsException(
         "Attempted to set the parent of this message ("+this+
         ") to a message on a different board (message id:"+raw+")");
    setParent_unsafe(raw);
  }


  /**
   * This function is currently used by the admin suite
   * to label Treeable objects.  
   * It might make sense to change the admin suite at some point
   * to use a more specific function such as getTreeableNodeLabel
   */
  public String getName()
      throws AccessPoemException {
    return getSubject();
  }

}


/**
 * A daemon to redistribute emails to the messageboard's distribution list.
 */

class DistributeThread extends Thread {
  private Message message;
  
  /**
   * Constructor.
   * @param message message to distribute
   */
  public DistributeThread(Message message) {
    this.message = message;
  }

  public void run() {
    message.getDatabase().inSession(
      AccessToken.root,
      new PoemTask() {
        public void run() {
          try {
              message.getBoard().distribute(message);
          }
          catch (Exception e) {
            System.err.println("Some problem in the Distribution Thread:");
            e.printStackTrace();
          }
        }
        public String toString() {
          return "Distributing message (" + message + ") to board (" + message.getBoard() + ")";
        }
      });
  }
  
}

