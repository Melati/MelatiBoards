package org.paneris.melati.boards.model;

import org.paneris.melati.boards.model.generated.*;
import java.util.*;
import java.sql.Date;
import java.sql.Timestamp;
import org.melati.poem.*;

public class MessageTable extends MessageTableBase {

  public MessageTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  public void create(Persistent persistent)
      throws AccessPoemException, ValidationPoemException,
             InitialisationPoemException {

    Board b = getBoardsDatabase().getBoardTable().
                getBoardObject(((Message)persistent).getBoard_unsafe());
    AccessToken token = PoemThread.accessToken();

    Timestamp now = new Timestamp(new java.util.Date().getTime());
    persistent.setRaw("date", now);
    persistent.setRaw("author", ((User)token).troid());
    persistent.setRaw("approved",
                 new Boolean(
                   !b.getModeratedposting_unsafe().booleanValue() ||
                   b.canManage((User)token)));

    super.create(persistent);

    System.err.println("About to add this new message to the board");
    
    // Update the message trees for the relevant board
    Integer parent = ((Message)persistent).getParent_unsafe();
    if (parent == null)
      b.addThread((Message)persistent, true);
    else
      b.addToParent((Message)persistent, getMessageObject(parent));
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
