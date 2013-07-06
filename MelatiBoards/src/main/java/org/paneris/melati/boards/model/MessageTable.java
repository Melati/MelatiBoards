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

import java.sql.Timestamp;

import org.melati.poem.AccessPoemException;
import org.melati.poem.CachedCount;
import org.melati.poem.CachedSelection;
import org.melati.poem.Database;
import org.melati.poem.DefinitionSource;
import org.melati.poem.InitialisationPoemException;
import org.melati.poem.Persistent;
import org.melati.poem.PoemException;
import org.melati.poem.ValidationPoemException;
import org.paneris.melati.boards.model.generated.MessageTableBase;


/**
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>MessageTable</code> object.
 * <p>
 * Description: 
 *   A message posted to a message board. 
 * </p>
 *
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
 * see  org.melati.poem.prepro.TableDef#generateTableJava 
 */
public class MessageTable<T extends Message> extends MessageTableBase<Message> {

 /**
  * Constructor.
  * 
  * see org.melati.poem.prepro.TableDef#generateTableJava 
  * @param database          the POEM database we are using
  * @param name              the name of this <code>Table</code>
  * @param definitionSource  which definition is being used
  * @throws PoemException    if anything goes wrong
  */
  public MessageTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  // programmer's domain-specific code here

  /**
   * Write down a message.
   *
   * @param persistent  a persistent object to write down
   */
   public void create(Persistent persistent)
       throws AccessPoemException, ValidationPoemException,
              InitialisationPoemException {

     Board b = getBoardsDatabaseTables().getBoardTable().
                 getBoardObject(((Message)persistent).getBoard_unsafe());

     if ("".equals(((Message)persistent).getSubject_unsafe())) {
       persistent.setRaw("subject", "(no subject)");
     }
     Timestamp now = new Timestamp(new java.util.Date().getTime());
     persistent.setRaw("date", now);
     
     // you don't need to be logged in to author a message!
     // persistent.setRaw("author", ((User)token).troid());
     
     boolean approved = !b.getModeratedposting_unsafe().booleanValue() ||
                        b.canManage(((Message)persistent).getAuthor());
     persistent.setRaw("approved", new Boolean(approved));

     super.create(persistent);

     if (approved) {    
       // Update the message trees for the relevant board
       Integer parent = ((Message)persistent).getParent_unsafe();
       if (parent == null)
         b.addThread((Message)persistent, true);
       else
         b.addToParent((Message)persistent, getMessageObject(parent));
     }
   }

  /**
   * SQL to get the messages in this board.
   */
   public String messageInBoardSQL(Board board, boolean approved) {
     return getBoardColumn().eqClause(board.troid()) + " AND " +
            getApprovedColumn().eqClause(new Boolean(approved)) + " AND " +
            getDeletedColumn().eqClause(Boolean.FALSE);
   }

   /**
    * @return a CachedSelection of the roots
    */
   public CachedSelection<?> cachedBoardRoots(Board board) {
     return cachedSelection(messageInBoardSQL(board, true) + " AND " +
                              getParentColumn().eqClause(null),
                            null);
   }

   /**
    * @return a CahcedCount of the number of messages in the board
    */
   public CachedCount cachedMessageCount(Board board) {
     return cachedCount(messageInBoardSQL(board, true));
   }

   /**
    * @return a CachedSelection of the messages in the board
    */
   public CachedSelection<?> cachedMessages(Board board) {
     return cachedSelection(messageInBoardSQL(board, false), null);
   }

   /**
    * @return a CachedCount of the messages in the board
    */
   public CachedCount cachedPendingMessageCount(Board board) {
     return cachedCount(messageInBoardSQL(board, false));
   }

   /**
    * @return a CachedSelection of the pending messages in the board
    */
   public CachedSelection<?> cachedPendingMessages(Board board) {
     return cachedSelection(messageInBoardSQL(board, false), null);
   }

}

