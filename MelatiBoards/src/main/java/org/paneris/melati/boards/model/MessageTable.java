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

public class MessageTable extends MessageTableBase {

  public MessageTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

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
   * Get messages
   */

  public String messageInBoardSQL(Board board, boolean approved) {
    return getBoardColumn().eqClause(board.troid()) + " AND " +
           getApprovedColumn().eqClause(new Boolean(approved)) + " AND " +
           getDeletedColumn().eqClause(Boolean.FALSE);
  }

  public CachedSelection cachedBoardRoots(Board board) {
    return cachedSelection(messageInBoardSQL(board, true) + " AND " +
                             getParentColumn().eqClause(null),
                           null);
  }

  public CachedCount cachedMessageCount(Board board) {
    return cachedCount(messageInBoardSQL(board, true));
  }

  public CachedSelection cachedMessages(Board board) {
    return cachedSelection(messageInBoardSQL(board, false), null);
  }

  public CachedCount cachedPendingMessageCount(Board board) {
    return cachedCount(messageInBoardSQL(board, false));
  }

  public CachedSelection cachedPendingMessages(Board board) {
    return cachedSelection(messageInBoardSQL(board, false), null);
  }

}
