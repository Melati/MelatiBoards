// Do not edit this file!  It was generated by Melati POEM's DSD preprocessor.

package org.paneris.melati.boards.model.generated;


import org.melati.poem.Database;
import org.melati.poem.DefinitionSource;
import org.melati.poem.Column;
import org.melati.poem.Persistent;
import org.melati.poem.Field;
import org.melati.poem.PoemException;
import org.melati.poem.AccessPoemException;
import org.melati.poem.ValidationPoemException;
import org.melati.poem.Table;
import org.paneris.melati.boards.model.BoardsDatabaseTables;

import org.paneris.melati.boards.model.Message;
import java.sql.Timestamp;
import org.paneris.melati.boards.model.Board;
import org.melati.poem.ReferencePoemType;
import org.melati.poem.StringPoemType;
import org.paneris.melati.boards.model.User;
import org.melati.poem.TimestampPoemType;
import org.melati.poem.StandardIntegrityFix;
import org.melati.poem.BooleanPoemType;
import org.melati.poem.Searchability;
import org.melati.poem.DisplayLevel;
import org.melati.poem.TroidPoemType;


/**
 * Melati POEM generated base class for table Message.
 * Field summary for SQL table message:
 *   id
 *   board
 *   date
 *   subject
 *   author
 *   parent
 *   body
 *   deleted
 *   approved
 *
 */
public class MessageTableBase extends Table {

  private Column col_id = null;
  private Column col_board = null;
  private Column col_date = null;
  private Column col_subject = null;
  private Column col_author = null;
  private Column col_parent = null;
  private Column col_body = null;
  private Column col_deleted = null;
  private Column col_approved = null;

  public MessageTableBase(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  public MessageTableBase(
      Database database, String name) throws PoemException {
    this(database, name, DefinitionSource.dsd);
  }

  public BoardsDatabaseTables getBoardsDatabaseTables() {
    return (BoardsDatabaseTables)getDatabase();
  }

  protected void init() throws PoemException {
    super.init();
    defineColumn(col_id =
        new Column(this, "id",
                   new TroidPoemType(),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Message)g).getId();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Message)g).setId((Integer)cooked);
          }

          public Field asField(Persistent g) {
            return ((Message)g).getIdField();
          }

          protected boolean defaultUserEditable() {
            return false;
          }

          protected boolean defaultUserCreateable() {
            return false;
          }

          protected int defaultDisplayOrder() {
            return 0;
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getId_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setId_unsafe((Integer)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getId();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setId((Integer)raw);
          }
        });

    defineColumn(col_board =
        new Column(this, "board",
                   new ReferencePoemType(getBoardsDatabaseTables().
                                             getBoardTable(), false),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Message)g).getBoard();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Message)g).setBoard((Board)cooked);
          }

          public Field asField(Persistent g) {
            return ((Message)g).getBoardField();
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.summary;
          }

          protected Searchability defaultSearchability() {
            return Searchability.primary;
          }

          protected Integer defaultDisplayOrderPriority() {
            return new Integer(0);
          }

          protected int defaultDisplayOrder() {
            return 1;
          }

          protected String defaultDescription() {
            return "The board this message belongs to";
          }

          protected boolean defaultIndexed() {
            return true;
          }

          protected String defaultRenderinfo() {
            return "SelectionWindow";
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getBoard_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setBoard_unsafe((Integer)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getBoardTroid();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setBoardTroid((Integer)raw);
          }

          public StandardIntegrityFix defaultIntegrityFix() {
            return StandardIntegrityFix.delete;
          }
        });

    defineColumn(col_date =
        new Column(this, "date",
                   new TimestampPoemType(false),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Message)g).getDate();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Message)g).setDate((Timestamp)cooked);
          }

          public Field asField(Persistent g) {
            return ((Message)g).getDateField();
          }

          protected boolean defaultUserEditable() {
            return false;
          }

          protected boolean defaultUserCreateable() {
            return false;
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.summary;
          }

          protected Searchability defaultSearchability() {
            return Searchability.no;
          }

          protected Integer defaultDisplayOrderPriority() {
            return new Integer(1);
          }

          protected boolean defaultSortDescending() {
            return true;
          }

          protected int defaultDisplayOrder() {
            return 2;
          }

          protected String defaultDescription() {
            return "The date and time at which this message was posted";
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getDate_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setDate_unsafe((Timestamp)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getDate();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setDate((Timestamp)raw);
          }
        });

    defineColumn(col_subject =
        new Column(this, "subject",
                   new StringPoemType(false, -1),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Message)g).getSubject();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Message)g).setSubject((String)cooked);
          }

          public Field asField(Persistent g) {
            return ((Message)g).getSubjectField();
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.primary;
          }

          protected Searchability defaultSearchability() {
            return Searchability.yes;
          }

          protected int defaultDisplayOrder() {
            return 3;
          }

          protected String defaultDescription() {
            return "The subject line of this message";
          }

          protected int defaultWidth() {
            return 40;
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getSubject_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setSubject_unsafe((String)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getSubject();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setSubject((String)raw);
          }
        });

    defineColumn(col_author =
        new Column(this, "author",
                   new ReferencePoemType(getBoardsDatabaseTables().
                                             getUserTable(), false),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Message)g).getAuthor();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Message)g).setAuthor((User)cooked);
          }

          public Field asField(Persistent g) {
            return ((Message)g).getAuthorField();
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.summary;
          }

          protected Searchability defaultSearchability() {
            return Searchability.yes;
          }

          protected int defaultDisplayOrder() {
            return 4;
          }

          protected String defaultDescription() {
            return "Author of this message";
          }

          protected String defaultRenderinfo() {
            return "SelectionWindow";
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getAuthor_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setAuthor_unsafe((Integer)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getAuthorTroid();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setAuthorTroid((Integer)raw);
          }

          public StandardIntegrityFix defaultIntegrityFix() {
            return StandardIntegrityFix.delete;
          }
        });

    defineColumn(col_parent =
        new Column(this, "parent",
                   new ReferencePoemType(getBoardsDatabaseTables().
                                             getMessageTable(), true),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Message)g).getParent();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Message)g).setParent((Message)cooked);
          }

          public Field asField(Persistent g) {
            return ((Message)g).getParentField();
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.record;
          }

          protected Searchability defaultSearchability() {
            return Searchability.yes;
          }

          protected String defaultDisplayName() {
            return "Parent";
          }

          protected int defaultDisplayOrder() {
            return 5;
          }

          protected String defaultDescription() {
            return "The message to which this message is a follow-up";
          }

          protected boolean defaultIndexed() {
            return true;
          }

          protected String defaultRenderinfo() {
            return "SelectionWindow";
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getParent_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setParent_unsafe((Integer)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getParentTroid();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setParentTroid((Integer)raw);
          }
        });

    defineColumn(col_body =
        new Column(this, "body",
                   new StringPoemType(false, -1),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Message)g).getBody();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Message)g).setBody((String)cooked);
          }

          public Field asField(Persistent g) {
            return ((Message)g).getBodyField();
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.record;
          }

          protected Searchability defaultSearchability() {
            return Searchability.yes;
          }

          protected int defaultDisplayOrder() {
            return 6;
          }

          protected String defaultDescription() {
            return "The main content of this message";
          }

          protected int defaultWidth() {
            return 70;
          }

          protected int defaultHeight() {
            return 20;
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getBody_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setBody_unsafe((String)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getBody();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setBody((String)raw);
          }
        });

    defineColumn(col_deleted =
        new Column(this, "deleted",
                   new BooleanPoemType(false),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Message)g).getDeleted();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Message)g).setDeleted((Boolean)cooked);
          }

          public Field asField(Persistent g) {
            return ((Message)g).getDeletedField();
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.record;
          }

          protected Searchability defaultSearchability() {
            return Searchability.no;
          }

          protected String defaultDisplayName() {
            return "Deleted";
          }

          protected int defaultDisplayOrder() {
            return 7;
          }

          protected String defaultDescription() {
            return "A deleted message cannot be viewed or displayed on lists";
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getDeleted_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setDeleted_unsafe((Boolean)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getDeleted();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setDeleted((Boolean)raw);
          }
        });

    defineColumn(col_approved =
        new Column(this, "approved",
                   new BooleanPoemType(false),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Message)g).getApproved();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Message)g).setApproved((Boolean)cooked);
          }

          public Field asField(Persistent g) {
            return ((Message)g).getApprovedField();
          }

          protected boolean defaultUserCreateable() {
            return false;
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.record;
          }

          protected Searchability defaultSearchability() {
            return Searchability.no;
          }

          protected String defaultDisplayName() {
            return "Approved";
          }

          protected int defaultDisplayOrder() {
            return 8;
          }

          protected String defaultDescription() {
            return "A message must be approved by a manager of the board before it can be viewed (if the board has moderated postings)";
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getApproved_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setApproved_unsafe((Boolean)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Message)g).getApproved();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Message)g).setApproved((Boolean)raw);
          }
        });
  }

  public final Column getIdColumn() {
    return col_id;
  }

  public final Column getBoardColumn() {
    return col_board;
  }

  public final Column getDateColumn() {
    return col_date;
  }

  public final Column getSubjectColumn() {
    return col_subject;
  }

  public final Column getAuthorColumn() {
    return col_author;
  }

  public final Column getParentColumn() {
    return col_parent;
  }

  public final Column getBodyColumn() {
    return col_body;
  }

  public final Column getDeletedColumn() {
    return col_deleted;
  }

  public final Column getApprovedColumn() {
    return col_approved;
  }

  public Message getMessageObject(Integer troid) {
    return (Message)getObject(troid);
  }

  public Message getMessageObject(int troid) {
    return (Message)getObject(troid);
  }

  protected Persistent _newPersistent() {
    return new Message();
  }
  protected String defaultDescription() {
    return "A message posted to a message board";
  }

  protected String defaultCategory() {
    return "Boards";
  }

  protected int defaultDisplayOrder() {
    return 1050;
  }
}
