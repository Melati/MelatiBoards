package org.paneris.melati.boards.model;

import org.melati.util.*;

import org.paneris.melati.boards.model.generated.*;
import java.util.*;
import java.sql.Date;
import java.sql.Timestamp;
import org.melati.poem.*;


public class Message extends MessageBase implements Treeable {
  public Message() {}

  public void distribute() {
    (new DistributeThread(this)).start();
  }


  public String IndentBody(String indent) {
    String[] lines = StringUtils.split(getBody_unsafe(), '\n');
    StringBuffer ret = new StringBuffer(
                    getBody_unsafe().length()+lines.length*indent.length());
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
    return getBoardsDatabase().getAttachmentTable().getColumn("message").
             referencesTo(this);
  }

  CachedCount attachments = null;

  public int getAttachmentCount() {
    if (attachments == null)
      attachments = getBoardsDatabase().getAttachmentTable().
         cachedCount(getBoardsDatabase().getAttachmentTable().
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

  /**
   * A user can read a message if they can read messages on the
   * relevant board
   */
  public void assertCanRead(AccessToken token)
      throws AccessPoemException {
    if (token == AccessToken.root) return;
    try {
      Board b = getBoardsDatabase().getBoardTable().
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
      Board b = getBoardsDatabase().getBoardTable().
                   getBoardObject(getBoard_unsafe());
      if (!b.canManage((User)token))
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
      Board b = getBoardsDatabase().getBoardTable().
                    getBoardObject(getBoard_unsafe());
      if (((User)token).isGuest())
        throw new CreationAccessPoemException(getTable(), token,
                                            new Capability("Logged in"));
      if (!b.canPost((User)token))
        throw new CreationAccessPoemException(getTable(), token,
                                            new Capability("Manager"));
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
      });
  }
}

