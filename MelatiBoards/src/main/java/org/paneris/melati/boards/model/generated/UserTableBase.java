// Do not edit this file!  It was generated by Melati POEM's DSD preprocessor.

package org.paneris.melati.boards.model.generated;

import org.melati.poem.AccessPoemException;
import org.melati.poem.Column;
import org.melati.poem.Database;
import org.melati.poem.DefinitionSource;
import org.melati.poem.DisplayLevel;
import org.melati.poem.Field;
import org.melati.poem.Persistent;
import org.melati.poem.PoemException;
import org.melati.poem.Searchability;
import org.melati.poem.StringPoemType;
import org.melati.poem.UserTable;
import org.melati.poem.ValidationPoemException;
import org.paneris.melati.boards.model.BoardsDatabaseTables;
import org.paneris.melati.boards.model.User;


/**
 * Melati POEM generated base class for table User.
 * Field summary for SQL table user:
 *   email
 *
 */

public class UserTableBase extends UserTable {

  private Column col_email = null;

  public UserTableBase(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  public UserTableBase(
      Database database, String name) throws PoemException {
    this(database, name, DefinitionSource.dsd);
  }

  public BoardsDatabaseTables getBoardsDatabaseTables() {
    return (BoardsDatabaseTables)getDatabase();
  }

  protected void init() throws PoemException {
    super.init();
    defineColumn(col_email =
        new Column(this, "email",
                   new StringPoemType(false, -1),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((User)g).getEmail();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((User)g).setEmail((String)cooked);
          }

          public Field asField(Persistent g) {
            return ((User)g).getEmailField();
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.summary;
          }

          protected Searchability defaultSearchability() {
            return Searchability.yes;
          }

          protected int defaultDisplayOrder() {
            return 50;
          }

          protected String defaultDescription() {
            return "The user's email address";
          }

          protected int defaultWidth() {
            return 40;
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((User)g).getEmail_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((User)g).setEmail_unsafe((String)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((User)g).getEmail();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((User)g).setEmail((String)raw);
          }
        });
  }

  public final Column getEmailColumn() {
    return col_email;
  }

  public org.melati.poem.User getUserObject(Integer troid) {
    return (org.melati.poem.User)getObject(troid);
  }

  public org.melati.poem.User getUserObject(int troid) {
    return (org.melati.poem.User)getObject(troid);
  }

  protected Persistent _newPersistent() {
    return new User();
  }
  protected String defaultDescription() {
    return "A board user (with an email address)";
  }

  protected String defaultCategory() {
    return "User";
  }

  protected int defaultDisplayOrder() {
    return 2010;
  }
}
