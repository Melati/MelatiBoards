/*
 * $Source$
 * $Revision$
 *
 * Copyright (C) 2003 Tim Pizey 
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
 *     Tim Pizey  <timp@paneris.org>
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
   * @return the Troid + 1
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
   * @return the parent NNTP message
   */
  public NNTPMessage getNntpParent() {
    return new NNTPMessage(m.getParent());
  }

  /**
   * Return an id from a message Id assumed to be of the format 
   * <14$msg@paneris.org>.
   * 
   * @return the message Id number
   */
  static int parseId (String messageId) {
    char[] s = messageId.toCharArray();
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
