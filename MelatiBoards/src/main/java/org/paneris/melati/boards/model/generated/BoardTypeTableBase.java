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

public class BoardTypeTableBase extends Table {

  private Column col_id = null;
  private Column col_type = null;
  private Column col_description = null;

  public BoardTypeTableBase(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  public BoardTypeTableBase(
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
            return ((BoardType)g).getId();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((BoardType)g).setId((Integer)cooked);
          }

          public Field asField(Persistent g) {
            return ((BoardType)g).getIdField();
          }

          protected boolean defaultUserEditable() {
            return false;
          }

          protected boolean defaultUserCreateable() {
            return false;
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.record;
          }

          protected Integer defaultDisplayOrderPriority() {
            return new Integer(0);
          }

          protected int defaultDisplayOrder() {
            return 0;
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((BoardType)g).getId_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((BoardType)g).setId_unsafe((Integer)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((BoardType)g).getId();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((BoardType)g).setId((Integer)raw);
          }
        });

    defineColumn(col_type =
        new Column(this, "type", new StringPoemType(false, -1), DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((BoardType)g).getType();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((BoardType)g).setType((String)cooked);
          }

          public Field asField(Persistent g) {
            return ((BoardType)g).getTypeField();
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.primary;
          }

          protected Searchability defaultSearchability() {
            return Searchability.yes;
          }

          protected int defaultDisplayOrder() {
            return 1;
          }

          protected String defaultDescription() {
            return "The name of a type";
          }

          protected boolean defaultUnique() {
            return true;
          }

          protected int defaultWidth() {
            return 40;
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((BoardType)g).getType_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((BoardType)g).setType_unsafe((String)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((BoardType)g).getType();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((BoardType)g).setType((String)raw);
          }
        });

    defineColumn(col_description =
        new Column(this, "description", new StringPoemType(false, -1), DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((BoardType)g).getDescription();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((BoardType)g).setDescription((String)cooked);
          }

          public Field asField(Persistent g) {
            return ((BoardType)g).getDescriptionField();
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.record;
          }

          protected Searchability defaultSearchability() {
            return Searchability.yes;
          }

          protected int defaultDisplayOrder() {
            return 2;
          }

          protected String defaultDescription() {
            return "The description of the type";
          }

          protected boolean defaultUnique() {
            return true;
          }

          protected int defaultWidth() {
            return 40;
          }

          protected int defaultHeight() {
            return 6;
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((BoardType)g).getDescription_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((BoardType)g).setDescription_unsafe((String)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((BoardType)g).getDescription();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((BoardType)g).setDescription((String)raw);
          }
        });
  }

  public final Column getIdColumn() {
    return col_id;
  }

  public final Column getTypeColumn() {
    return col_type;
  }

  public final Column getDescriptionColumn() {
    return col_description;
  }

  public BoardType getBoardTypeObject(Integer troid) {
    return (BoardType)getObject(troid);
  }

  public BoardType getBoardTypeObject(int troid) {
    return (BoardType)getObject(troid);
  }

  protected Persistent _newPersistent() {
    return new BoardType();
  }
  protected String defaultDisplayName() {
    return "Board Type";
  }

  protected String defaultDescription() {
    return "A type of a board";
  }

  protected boolean defaultRememberAllTroids() {
    return true;
  }

  protected Integer defaultCacheLimit() {
    return new Integer(999999999);
  }

  protected String defaultCategory() {
    return "Boards";
  }

  protected int defaultDisplayOrder() {
    return 1010;
  }
}
