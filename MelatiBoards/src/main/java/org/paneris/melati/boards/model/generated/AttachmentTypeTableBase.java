// Do not edit this file!  It was generated by Melati POEM's DSD preprocessor.

package org.paneris.melati.boards.model.generated;

import org.melati.poem.AccessPoemException;
import org.melati.poem.Column;
import org.melati.poem.Database;
import org.melati.poem.DefinitionSource;
import org.melati.poem.DisplayLevel;
import org.melati.poem.Field;
import org.melati.poem.JdbcPersistent;
import org.melati.poem.Persistent;
import org.melati.poem.PoemException;
import org.melati.poem.Searchability;
import org.melati.poem.StringPoemType;
import org.melati.poem.TroidPoemType;
import org.melati.poem.ValidationPoemException;
import org.paneris.melati.boards.model.AttachmentType;
import org.paneris.melati.boards.model.BoardsDatabaseTables;
import org.paneris.melati.boards.model.BoardsTable;


/**
 * Melati POEM generated base class for 
<code>Table</code> <code>AttachmentType</code>.
 *
 * @generator org.melati.poem.prepro.TableDef#generateTableBaseJava 
 */

public class AttachmentTypeTableBase extends BoardsTable {

  private Column col_id = null;
  private Column col_type = null;

 /**
  * Constructor. 
  * 
  * @generator org.melati.poem.prepro.TableDef#generateTableBaseJava 
  * @param database          the POEM database we are using
  * @param name              the name of this <code>Table</code>
  * @param definitionSource  which definition is being used
  * @throws PoemException    if anything goes wrong
  */

  public AttachmentTypeTableBase(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }


 /**
  * Get the database tables.
  *
  * @generator org.melati.poem.prepro.TableDef#generateTableBaseJava 
  * @return the database tables
  */
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
            return ((AttachmentType)g).getId();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((AttachmentType)g).setId((Integer)cooked);
          }

          public Field asField(Persistent g) {
            return ((AttachmentType)g).getIdField();
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
            return ((AttachmentType)g).getId_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((AttachmentType)g).setId_unsafe((Integer)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((AttachmentType)g).getId();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((AttachmentType)g).setId((Integer)raw);
          }
        });

    defineColumn(col_type =
        new Column(this, "type",
                   new StringPoemType(false, -1),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((AttachmentType)g).getType();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((AttachmentType)g).setType((String)cooked);
          }

          public Field asField(Persistent g) {
            return ((AttachmentType)g).getTypeField();
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
            return ((AttachmentType)g).getType_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((AttachmentType)g).setType_unsafe((String)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((AttachmentType)g).getType();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((AttachmentType)g).setType((String)raw);
          }
        });
  }


 /**
  * Retrieves the <code>Id</code> <code>Column</code> for this 
  * <code>AttachmentType</code> <code>Table</code>.
  * 
  * @generator org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the id <code>Column</code>
  */
  public final Column getIdColumn() {
    return col_id;
  }


 /**
  * Retrieves the <code>Type</code> <code>Column</code> for this 
  * <code>AttachmentType</code> <code>Table</code>.
  * 
  * @generator org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the type <code>Column</code>
  */
  public final Column getTypeColumn() {
    return col_type;
  }


 /**
  * Retrieve the <code>AttachmentType</code> as a <code>AttachmentType</code>.
  *
  * @generator org.melati.poem.prepro.TableDef#generateTableBaseJava 
  * @param troid a Table Row Oject ID
  * @return the <code>Persistent</code> identified by the <code>troid</code>
  */
  public AttachmentType getAttachmentTypeObject(Integer troid) {
    return (AttachmentType)getObject(troid);
  }


 /**
  * Retrieve the <code>AttachmentType</code> 
  * as a <code>AttachmentType</code>.
  *
  * @generator org.melati.poem.prepro.TableDef#generateTableBaseJava 
  * @param troid a Table Row Object ID
  * @return the <code>Persistent</code> identified   */
  public AttachmentType getAttachmentTypeObject(int troid) {
    return (AttachmentType)getObject(troid);
  }

  protected JdbcPersistent _newPersistent() {
    return new AttachmentType();
  }
  protected String defaultDisplayName() {
    return "Attachment Type";
  }

  protected String defaultDescription() {
    return "A type of an attachment";
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
    return 1060;
  }
}

