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
import org.melati.util.EnumUtils;
import org.melati.util.StringUtils;
import org.melati.util.Treeable;
import org.paneris.melati.boards.model.generated.MessageBase;

public class Message extends MessageBase implements Treeable {

  public Message() {}

  private static DateFormat localeFormatter =
    new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
  static {
    localeFormatter.setTimeZone(TimeZone.getTimeZone("Europe/London"));
  }

  public String getLondonDate() {
    return localeFormatter.format(getDate_unsafe());
  }

  public void distribute() {
    (new DistributeThread(this)).start();
  }

  public void approve() {
    setApproved(Boolean.TRUE);
    if (getParent() == null)
      getBoard().addThread(this, true);
    else
      getBoard().addToParent(this, getParent());
  }

  String[] lines = null;
  public String[] getLines() {
    if (lines == null)
      lines = StringUtils.split(getBody_unsafe(), '\n');
    return lines; 
  }
  public String IndentBody(String indent) {
    StringBuffer ret = new StringBuffer(
                    getBody_unsafe().length() +
                    lines.length*indent.length());
    for(int i=0; i<lines.length; i++) {
      ret.append(indent);
      ret.append(lines[i]);
    }
    return ret.toString();
  }

  public String IndentBody() {
    return IndentBody(">");
  }

  public Enumeration getAttachments() {
    return getBoardsDatabaseTables().getAttachmentTable().getColumn("message").
             referencesTo(this);
  }

  CachedCount attachments = null;

  public int getAttachmentCount() {

    if (attachments == null)
      attachments = getBoardsDatabaseTables().getAttachmentTable().
         cachedCount(getBoardsDatabaseTables().getAttachmentTable().
           getMessageColumn().eqClause(this.troid()));
    return attachments.count();
  }

  public boolean hasAttachments() {
    return getAttachmentCount() > 0;
  }

  /**
   * Tree stuff
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

  public Message getThreadRoot() {
    Message current = this;
    while (current.getParent() != null)
      current = current.getParent();
    return current;
  }

  public ChildrenDrivenMutableTree getThread() {
    return getBoard().threadWithRoot(getThreadRoot());
  }

  // if we delete a message, recompute the messages for the board
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
          !getAuthor_unsafe().equals(((User)token).troid()) )
        throw new AccessPoemException(token, new Capability("Logged In"));
    } catch (ClassCastException e) {
        throw new AccessPoemException(token, new Capability("Logged In"));
    }

  }

  /**
   * A user can create a message if they have post permissions for the
   * relevant board
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
 * A daemon to redistribute emails to the messageboard's distribution list
 */

class DistributeThread extends Thread {
  private Message message;
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
