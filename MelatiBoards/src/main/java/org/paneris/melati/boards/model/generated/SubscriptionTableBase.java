// Do not edit this file!  It was generated by Melati POEM's DSD preprocessor.

package org.paneris.melati.boards.model.generated;

import org.paneris.melati.boards.model.User;
import org.paneris.melati.boards.model.UserTable;
import org.paneris.melati.boards.model.Setting;
import org.paneris.melati.boards.model.SettingTable;
import org.paneris.melati.boards.model.*;
import java.util.*;
import java.sql.Date;
import java.sql.Timestamp;
import org.melati.poem.*;

public class SubscriptionTableBase extends Table {

  private Column col_id = null;
  private Column col_user = null;
  private Column col_board = null;
  private Column col_status = null;
  private Column col_ismanager = null;
  private Column col_approved = null;

  public SubscriptionTableBase(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  public SubscriptionTableBase(
      Database database, String name) throws PoemException {
    this(database, name, DefinitionSource.dsd);
  }

  public BoardsDatabaseTables getBoardsDatabaseTables() {
    return (BoardsDatabaseTables)getDatabase();
  }

  protected void init() throws PoemException {
    super.init();
    defineColumn(col_id =
        new Column(this, "id", new TroidPoemType(), DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Subscription)g).getId();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Subscription)g).setId((Integer)cooked);
          }

          public Field asField(Persistent g) {
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
        new Column(this, "user", new ReferencePoemType(getBoardsDatabaseTables().getUserTable(), false), DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Subscription)g).getUser();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Subscription)g).setUser((User)cooked);
          }

          public Field asField(Persistent g) {
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
        new Column(this, "board", new ReferencePoemType(getBoardsDatabaseTables().getBoardTable(), false), DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Subscription)g).getBoard();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Subscription)g).setBoard((Board)cooked);
          }

          public Field asField(Persistent g) {
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
        new Column(this, "status", new ReferencePoemType(getBoardsDatabaseTables().getMembershipStatusTable(), false), DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Subscription)g).getStatus();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Subscription)g).setStatus((MembershipStatus)cooked);
          }

          public Field asField(Persistent g) {
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
        new Column(this, "ismanager", new BooleanPoemType(false), DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Subscription)g).getIsmanager();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Subscription)g).setIsmanager((Boolean)cooked);
          }

          public Field asField(Persistent g) {
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
        new Column(this, "approved", new BooleanPoemType(false), DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Subscription)g).getApproved();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Subscription)g).setApproved((Boolean)cooked);
          }

          public Field asField(Persistent g) {
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

  public final Column getIdColumn() {
    return col_id;
  }

  public final Column getUserColumn() {
    return col_user;
  }

  public final Column getBoardColumn() {
    return col_board;
  }

  public final Column getStatusColumn() {
    return col_status;
  }

  public final Column getIsmanagerColumn() {
    return col_ismanager;
  }

  public final Column getApprovedColumn() {
    return col_approved;
  }

  public Subscription getSubscriptionObject(Integer troid) {
    return (Subscription)getObject(troid);
  }

  public Subscription getSubscriptionObject(int troid) {
    return (Subscription)getObject(troid);
  }

  protected Persistent _newPersistent() {
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
