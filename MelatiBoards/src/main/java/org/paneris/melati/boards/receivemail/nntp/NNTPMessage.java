/*
 *
 * Created on 28-Jul-2003
 *
 * @author timp@paneris.org
 *
 */
package org.paneris.melati.boards.receivemail.nntp;

import org.melati.poem.AccessPoemException;
import org.paneris.melati.boards.model.Message;

/**
 * An NNTP specific wrapper around a Message.
 * 
 * @author timp@paneris.org
 *
 */
class NNTPMessage {

  private Message m = null;
  
  // Ensure we can't get created accidently
  private NNTPMessage() {}
  /**
   * Constructor.
   * 
   * @param m a {@link Message} to wrap
   */
  NNTPMessage(Message m) {
    this.m = m;
  }

  /**
   * Retrieve the {@link Message}
   * 
   * @return the Message
   */
  Message getMessage() {
    return m;
  }

  /**
   * Get the NNTP message id, which may not be zero, so is the 
   * Troid + 1.
   *
   *@return the Troid + 1
   */
  public String getNntpId()
      throws AccessPoemException {
    return String.valueOf(m.getTroid().intValue() + 1);
  }

  /**
   * Get the NNTP message id, which may not be zero, so is the 
   * Troid + 1.
   *
   *@return the Troid + 1
   */
  public int getNntpIdInt()
      throws AccessPoemException {
    return m.getTroid().intValue() + 1;
  }


  /**
   * Get the parent NNTP message id.
   * Note we cannot override getParent() as it returns the superclass.
   *@return the parent NNTP message
   */
  public NNTPMessage getNntpParent() {
    return new NNTPMessage(m.getParent());
  }

  /**
   * Return an id from a message Id assumed to be of the format 
   * <14$msg@paneris.org>.
   * 
   *@return the message Id number
   */
  static int parseId (String messageId) {
    char s[] = messageId.toCharArray();
    StringBuffer n = new StringBuffer();
    for (int i = 1; i < s.length && s[i] != '$'; i++){
      n.append(s[i]);
    }
    
    return Integer.parseInt(n.toString());
  }

  /**
   * Return the From field.
   * 
   * @return Name and email.
   */
  String getFrom() {
    return m.getAuthor().getName() 
           + " <" + m.getAuthor().getEmail() + ">";
  }
}
