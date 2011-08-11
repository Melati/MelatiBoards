// Do not edit this file!  It was generated by Melati POEM's DSD preprocessor.

package org.paneris.melati.boards.model.generated;


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
import org.melati.poem.TroidPoemType;
import org.melati.poem.ValidationPoemException;
import org.paneris.melati.boards.model.Board;
import org.paneris.melati.boards.model.BoardsDatabaseTables;
import org.paneris.melati.boards.model.BoardsTable;
import org.paneris.melati.boards.model.MembershipStatus;
import org.paneris.melati.boards.model.Subscription;
import org.paneris.melati.boards.model.User;


/**
 * Melati POEM generated base class for 
<code>Table</code> <code>Subscription</code>.
 *
 * @see org.melati.poem.prepro.TableDef#generateTableBaseJava 
 */

public class SubscriptionTableBase extends BoardsTable {

  private Column<Integer> col_id = null;
  private Column<Integer> col_user = null;
  private Column<Integer> col_board = null;
  private Column<Integer> col_status = null;
  private Column<Boolean> col_ismanager = null;
  private Column<Boolean> col_approved = null;

 /**
  * Constructor. 
  * 
  * @see org.melati.poem.prepro.TableDef#generateTableBaseJava 
  * @param database          the POEM database we are using
  * @param name              the name of this <code>Table</code>
  * @param definitionSource  which definition is being used
  * @throws PoemException    if anything goes wrong
  */

  public SubscriptionTableBase(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }


 /**
  * Get the database tables.
  *
  * @see org.melati.poem.prepro.TableDef#generateTableBaseJava 
  * @return the database tables
  */
  public BoardsDatabaseTables getBoardsDatabaseTables() {
    return (BoardsDatabaseTables)getDatabase();
  }

  public void init() throws PoemException {
    super.init();
    defineColumn(col_id =
        new Column<Integer>(this, "id",
                   new TroidPoemType(),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Subscription)g).getId();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Subscription)g).setId((Integer)cooked);
          }

          public Field<Integer> asField(Persistent g) {
            return ((Subscription)g).getIdField();
          }

          protected boolean defaultUserEditable() {
            return false;
          }

          protected boolean defaultUserCreateable() {
            return false;
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.primary;
          }

          protected int defaultDisplayOrder() {
            return 0;
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Subscription)g).getId_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Subscription)g).setId_unsafe((Integer)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Subscription)g).getId();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Subscription)g).setId((Integer)raw);
          }
        });

    defineColumn(col_user =
        new Column<Integer>(this, "user",
                   new ReferencePoemType(getBoardsDatabaseTables().
                                             getUserTable(), false),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Subscription)g).getUser();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Subscription)g).setUser((User)cooked);
          }

          public Field<Integer> asField(Persistent g) {
            return ((Subscription)g).getUserField();
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.summary;
          }

          protected Searchability defaultSearchability() {
            return Searchability.yes;
          }

          protected int defaultDisplayOrder() {
            return 1;
          }

          protected String defaultDescription() {
            return "The user";
          }

          protected boolean defaultIndexed() {
            return true;
          }

          protected String defaultRenderinfo() {
            return "SelectionWindow";
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Subscription)g).getUser_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Subscription)g).setUser_unsafe((Integer)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Subscription)g).getUserTroid();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Subscription)g).setUserTroid((Integer)raw);
          }

          public StandardIntegrityFix defaultIntegrityFix() {
            return StandardIntegrityFix.delete;
          }
        });

    defineColumn(col_board =
        new Column<Integer>(this, "board",
                   new ReferencePoemType(getBoardsDatabaseTables().
                                             getBoardTable(), false),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Subscription)g).getBoard();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Subscription)g).setBoard((Board)cooked);
          }

          public Field<Integer> asField(Persistent g) {
            return ((Subscription)g).getBoardField();
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.summary;
          }

          protected Searchability defaultSearchability() {
            return Searchability.primary;
          }

          protected int defaultDisplayOrder() {
            return 2;
          }

          protected String defaultDescription() {
            return "The board to which this user belongs";
          }

          protected boolean defaultIndexed() {
            return true;
          }

          protected String defaultRenderinfo() {
            return "SelectionWindow";
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Subscription)g).getBoard_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Subscription)g).setBoard_unsafe((Integer)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Subscription)g).getBoardTroid();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Subscription)g).setBoardTroid((Integer)raw);
          }

          public StandardIntegrityFix defaultIntegrityFix() {
            return StandardIntegrityFix.delete;
          }
        });

    defineColumn(col_status =
        new Column<Integer>(this, "status",
                   new ReferencePoemType(getBoardsDatabaseTables().
                                             getMembershipStatusTable(), false),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Subscription)g).getStatus();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Subscription)g).setStatus((MembershipStatus)cooked);
          }

          public Field<Integer> asField(Persistent g) {
            return ((Subscription)g).getStatusField();
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.summary;
          }

          protected Searchability defaultSearchability() {
            return Searchability.yes;
          }

          protected int defaultDisplayOrder() {
            return 3;
          }

          protected String defaultDescription() {
            return "How users would like to receive emails from this board";
          }

          protected boolean defaultIndexed() {
            return true;
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Subscription)g).getStatus_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Subscription)g).setStatus_unsafe((Integer)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Subscription)g).getStatusTroid();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Subscription)g).setStatusTroid((Integer)raw);
          }
        });

    defineColumn(col_ismanager =
        new Column<Boolean>(this, "ismanager",
                   new BooleanPoemType(false),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Subscription)g).getIsmanager();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Subscription)g).setIsmanager((Boolean)cooked);
          }

          public Field<Boolean> asField(Persistent g) {
            return ((Subscription)g).getIsmanagerField();
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.summary;
          }

          protected Searchability defaultSearchability() {
            return Searchability.yes;
          }

          protected String defaultDisplayName() {
            return "Is A Manager";
          }

          protected int defaultDisplayOrder() {
            return 4;
          }

          protected String defaultDescription() {
            return "Can the user administrator the board with manager priviledges?";
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Subscription)g).getIsmanager_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Subscription)g).setIsmanager_unsafe((Boolean)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Subscription)g).getIsmanager();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Subscription)g).setIsmanager((Boolean)raw);
          }
        });

    defineColumn(col_approved =
        new Column<Boolean>(this, "approved",
                   new BooleanPoemType(false),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Subscription)g).getApproved();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Subscription)g).setApproved((Boolean)cooked);
          }

          public Field<Boolean> asField(Persistent g) {
            return ((Subscription)g).getApprovedField();
          }

          protected boolean defaultUserCreateable() {
            return false;
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.summary;
          }

          protected Searchability defaultSearchability() {
            return Searchability.yes;
          }

          protected String defaultDisplayName() {
            return "Approved";
          }

          protected int defaultDisplayOrder() {
            return 5;
          }

          protected String defaultDescription() {
            return "Has this user's subscription to this board been approved by a manager (if the board has moderated subscription)";
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Subscription)g).getApproved_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Subscription)g).setApproved_unsafe((Boolean)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Subscription)g).getApproved();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Subscription)g).setApproved((Boolean)raw);
          }
        });
  }


 /**
  * Retrieves the <code>Id</code> <code>Column</code> for this 
  * <code>Subscription</code> <code>Table</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the id <code>Column</code>
  */
  public final Column<Integer> getIdColumn() {
    return col_id;
  }


 /**
  * Retrieves the <code>User</code> <code>Column</code> for this 
  * <code>Subscription</code> <code>Table</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the user <code>Column</code>
  */
  public final Column<Integer> getUserColumn() {
    return col_user;
  }


 /**
  * Retrieves the <code>Board</code> <code>Column</code> for this 
  * <code>Subscription</code> <code>Table</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the board <code>Column</code>
  */
  public final Column<Integer> getBoardColumn() {
    return col_board;
  }


 /**
  * Retrieves the <code>Status</code> <code>Column</code> for this 
  * <code>Subscription</code> <code>Table</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the status <code>Column</code>
  */
  public final Column<Integer> getStatusColumn() {
    return col_status;
  }


 /**
  * Retrieves the <code>Ismanager</code> <code>Column</code> for this 
  * <code>Subscription</code> <code>Table</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the ismanager <code>Column</code>
  */
  public final Column<Boolean> getIsmanagerColumn() {
    return col_ismanager;
  }


 /**
  * Retrieves the <code>Approved</code> <code>Column</code> for this 
  * <code>Subscription</code> <code>Table</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the approved <code>Column</code>
  */
  public final Column<Boolean> getApprovedColumn() {
    return col_approved;
  }


 /**
  * Retrieve the <code>Subscription</code> as a <code>Subscription</code>.
  *
  * @see org.melati.poem.prepro.TableDef#generateTableBaseJava 
  * @param troid a Table Row Oject ID
  * @return the <code>Persistent</code> identified by the <code>troid</code>
  */
  public Subscription getSubscriptionObject(Integer troid) {
    return (Subscription)getObject(troid);
  }


 /**
  * Retrieve the <code>Subscription</code> 
  * as a <code>Subscription</code>.
  *
  * @see org.melati.poem.prepro.TableDef#generateTableBaseJava 
  * @param troid a Table Row Object ID
  * @return the <code>Persistent</code> identified   */
  public Subscription getSubscriptionObject(int troid) {
    return (Subscription)getObject(troid);
  }

  protected JdbcPersistent _newPersistent() {
    return new Subscription();
  }
  protected String defaultDisplayName() {
    return "Subscription";
  }

  protected String defaultDescription() {
    return "Which users are members of which boards with what settings";
  }

  protected String defaultCategory() {
    return "Boards";
  }

  protected int defaultDisplayOrder() {
    return 1040;
  }
}

