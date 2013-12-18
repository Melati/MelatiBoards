// Do not edit this file!  It was generated by Melati POEM's DSD preprocessor.

package org.paneris.melati.boards.model.generated;


import java.sql.Timestamp;
import org.melati.poem.AccessPoemException;
import org.melati.poem.BooleanPoemType;
import org.melati.poem.Column;
import org.melati.poem.Database;
import org.melati.poem.DefinitionSource;
import org.melati.poem.DisplayLevel;
import org.melati.poem.Field;
import org.melati.poem.JdbcPersistent;
import org.melati.poem.Persistent;
import org.melati.poem.PoemException;
import org.melati.poem.ReferencePoemType;
import org.melati.poem.Searchability;
import org.melati.poem.StandardIntegrityFix;
import org.melati.poem.StringPoemType;
import org.melati.poem.TimestampPoemType;
import org.melati.poem.TroidPoemType;
import org.melati.poem.ValidationPoemException;
import org.paneris.melati.boards.model.Board;
import org.paneris.melati.boards.model.BoardsDatabaseTables;
import org.paneris.melati.boards.model.BoardsTable;
import org.paneris.melati.boards.model.Message;
// Extended table 
import org.paneris.melati.boards.model.User;


/**
 * Melati POEM generated base class for <code>Table</code> <code>Message</code>.
 *
 * see org.melati.poem.prepro.TableDef#generateTableBaseJava 
 */

public class MessageTableBase<T extends Message> extends BoardsTable<T> {

  private Column<Integer> col_id = null;
  private Column<Integer> col_board = null;
  private Column<Timestamp> col_date = null;
  private Column<String> col_subject = null;
  private Column<Integer> col_author = null;
  private Column<Integer> col_parent = null;
  private Column<String> col_body = null;
  private Column<Boolean> col_deleted = null;
  private Column<Boolean> col_approved = null;

 /**
  * Constructor. 
  * 
  * see org.melati.poem.prepro.TableDef#generateTableBaseJava 
  * @param database          the POEM database we are using
  * @param name              the name of this <code>Table</code>
  * @param definitionSource  which definition is being used
  * @throws PoemException    if anything goes wrong
  */

  public MessageTableBase(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }


 /**
  * Get the database tables.
  *
  * see org.melati.poem.prepro.TableDef#generateTableBaseJava 
  * @return the database tables
  */
  public BoardsDatabaseTables getBoardsDatabaseTables() {
    return (BoardsDatabaseTables)getDatabase();
  }


 /**
  * Initialise this table by defining its columns.
  *
  * see org.melati.poem.prepro.TableDef#generateTableBaseJava 
  */
  public void init() throws PoemException {
    super.init();
    defineColumn(col_id =
        new Column<Integer>(this, "id",
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

          public Field<Integer> asField(Persistent g) {
            return ((Message)g).getIdField();
          }

          public boolean defaultUserEditable() {
            return false;
          }

          public boolean defaultUserCreateable() {
            return false;
          }

          public int defaultDisplayOrder() {
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
        new Column<Integer>(this, "board",
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

          public Field<Integer> asField(Persistent g) {
            return ((Message)g).getBoardField();
          }

          public DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.summary;
          }

          public Searchability defaultSearchability() {
            return Searchability.primary;
          }

          public Integer defaultDisplayOrderPriority() {
            return new Integer(0);
          }

          public int defaultDisplayOrder() {
            return 1;
          }

          public String defaultDescription() {
            return "The board this message belongs to";
          }

          public boolean defaultIndexed() {
            return true;
          }

          public String defaultRenderinfo() {
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
        new Column<Timestamp>(this, "date",
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

          public Field<Timestamp> asField(Persistent g) {
            return ((Message)g).getDateField();
          }

          public boolean defaultUserEditable() {
            return false;
          }

          public boolean defaultUserCreateable() {
            return false;
          }

          public DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.summary;
          }

          public Searchability defaultSearchability() {
            return Searchability.no;
          }

          public Integer defaultDisplayOrderPriority() {
            return new Integer(1);
          }

          public boolean defaultSortDescending() {
            return true;
          }

          public int defaultDisplayOrder() {
            return 2;
          }

          public String defaultDescription() {
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
        new Column<String>(this, "subject",
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

          public Field<String> asField(Persistent g) {
            return ((Message)g).getSubjectField();
          }

          public DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.primary;
          }

          public Searchability defaultSearchability() {
            return Searchability.yes;
          }

          public int defaultDisplayOrder() {
            return 3;
          }

          public String defaultDescription() {
            return "The subject line of this message";
          }

          public int defaultWidth() {
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
        new Column<Integer>(this, "author",
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

          public Field<Integer> asField(Persistent g) {
            return ((Message)g).getAuthorField();
          }

          public DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.summary;
          }

          public Searchability defaultSearchability() {
            return Searchability.yes;
          }

          public int defaultDisplayOrder() {
            return 4;
          }

          public String defaultDescription() {
            return "Author of this message";
          }

          public String defaultRenderinfo() {
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
        new Column<Integer>(this, "parent",
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

          public Field<Integer> asField(Persistent g) {
            return ((Message)g).getParentField();
          }

          public DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.record;
          }

          public Searchability defaultSearchability() {
            return Searchability.yes;
          }

          public String defaultDisplayName() {
            return "Parent";
          }

          public int defaultDisplayOrder() {
            return 5;
          }

          public String defaultDescription() {
            return "The message to which this message is a follow-up";
          }

          public boolean defaultIndexed() {
            return true;
          }

          public String defaultRenderinfo() {
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
        new Column<String>(this, "body",
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

          public Field<String> asField(Persistent g) {
            return ((Message)g).getBodyField();
          }

          public DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.record;
          }

          public Searchability defaultSearchability() {
            return Searchability.yes;
          }

          public int defaultDisplayOrder() {
            return 6;
          }

          public String defaultDescription() {
            return "The main content of this message";
          }

          public int defaultWidth() {
            return 70;
          }

          public int defaultHeight() {
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
        new Column<Boolean>(this, "deleted",
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

          public Field<Boolean> asField(Persistent g) {
            return ((Message)g).getDeletedField();
          }

          public DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.record;
          }

          public Searchability defaultSearchability() {
            return Searchability.no;
          }

          public String defaultDisplayName() {
            return "Deleted";
          }

          public int defaultDisplayOrder() {
            return 7;
          }

          public String defaultDescription() {
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
        new Column<Boolean>(this, "approved",
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

          public Field<Boolean> asField(Persistent g) {
            return ((Message)g).getApprovedField();
          }

          public boolean defaultUserCreateable() {
            return false;
          }

          public DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.record;
          }

          public Searchability defaultSearchability() {
            return Searchability.no;
          }

          public String defaultDisplayName() {
            return "Approved";
          }

          public int defaultDisplayOrder() {
            return 8;
          }

          public String defaultDescription() {
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


 /**
  * Retrieves the <code>Id</code> <code>Column</code> for this 
  * <code>Message</code> <code>Table</code>.
  * 
  * see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the id <code>Column</code>
  */
  public final Column<Integer> getIdColumn() {
    return col_id;
  }


 /**
  * Retrieves the <code>Board</code> <code>Column</code> for this 
  * <code>Message</code> <code>Table</code>.
  * 
  * see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the board <code>Column</code>
  */
  public final Column<Integer> getBoardColumn() {
    return col_board;
  }


 /**
  * Retrieves the <code>Date</code> <code>Column</code> for this 
  * <code>Message</code> <code>Table</code>.
  * 
  * see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the date <code>Column</code>
  */
  public final Column<Timestamp> getDateColumn() {
    return col_date;
  }


 /**
  * Retrieves the <code>Subject</code> <code>Column</code> for this 
  * <code>Message</code> <code>Table</code>.
  * 
  * see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the subject <code>Column</code>
  */
  public final Column<String> getSubjectColumn() {
    return col_subject;
  }


 /**
  * Retrieves the <code>Author</code> <code>Column</code> for this 
  * <code>Message</code> <code>Table</code>.
  * 
  * see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the author <code>Column</code>
  */
  public final Column<Integer> getAuthorColumn() {
    return col_author;
  }


 /**
  * Retrieves the <code>Parent</code> <code>Column</code> for this 
  * <code>Message</code> <code>Table</code>.
  * 
  * see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the parent <code>Column</code>
  */
  public final Column<Integer> getParentColumn() {
    return col_parent;
  }


 /**
  * Retrieves the <code>Body</code> <code>Column</code> for this 
  * <code>Message</code> <code>Table</code>.
  * 
  * see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the body <code>Column</code>
  */
  public final Column<String> getBodyColumn() {
    return col_body;
  }


 /**
  * Retrieves the <code>Deleted</code> <code>Column</code> for this 
  * <code>Message</code> <code>Table</code>.
  * 
  * see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the deleted <code>Column</code>
  */
  public final Column<Boolean> getDeletedColumn() {
    return col_deleted;
  }


 /**
  * Retrieves the <code>Approved</code> <code>Column</code> for this 
  * <code>Message</code> <code>Table</code>.
  * 
  * see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the approved <code>Column</code>
  */
  public final Column<Boolean> getApprovedColumn() {
    return col_approved;
  }


 /**
  * Retrieve the <code>Message</code> as a <code>Message</code>.
  *
  * see org.melati.poem.prepro.TableDef#generateTableBaseJava 
  * @param troid a Table Row Object ID
  * @return the <code>Persistent</code> identified by the <code>troid</code>
  */
  public Message getMessageObject(Integer troid) {
    return (Message)getObject(troid);
  }


 /**
  * Retrieve the <code>Message</code> 
  * as a <code>Message</code>.
  *
  * see org.melati.poem.prepro.TableDef#generateTableBaseJava 
  * @param troid a Table Row Object ID
  * @return the <code>Persistent</code> identified   */
  public Message getMessageObject(int troid) {
    return (Message)getObject(troid);
  }

  protected JdbcPersistent _newPersistent() {
    return new Message();
  }
  public String defaultDescription() {
    return "A message posted to a message board";
  }

  public Integer defaultCacheLimit() {
    return new Integer(2000);
  }

  public String defaultCategory() {
    return "Boards";
  }

  public int defaultDisplayOrder() {
    return 1050;
  }
}

