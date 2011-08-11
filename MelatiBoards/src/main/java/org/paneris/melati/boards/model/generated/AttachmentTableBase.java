// Do not edit this file!  It was generated by Melati POEM's DSD preprocessor.

package org.paneris.melati.boards.model.generated;


import org.melati.poem.AccessPoemException;
import org.melati.poem.Column;
import org.melati.poem.Database;
import org.melati.poem.DefinitionSource;
import org.melati.poem.DisplayLevel;
import org.melati.poem.Field;
import org.melati.poem.IntegerPoemType;
import org.melati.poem.JdbcPersistent;
import org.melati.poem.Persistent;
import org.melati.poem.PoemException;
import org.melati.poem.ReferencePoemType;
import org.melati.poem.Searchability;
import org.melati.poem.StandardIntegrityFix;
import org.melati.poem.StringPoemType;
import org.melati.poem.TroidPoemType;
import org.melati.poem.ValidationPoemException;
import org.paneris.melati.boards.model.Attachment;
import org.paneris.melati.boards.model.AttachmentType;
import org.paneris.melati.boards.model.BoardsDatabaseTables;
import org.paneris.melati.boards.model.BoardsTable;
import org.paneris.melati.boards.model.Message;


/**
 * Melati POEM generated base class for 
<code>Table</code> <code>Attachment</code>.
 *
 * @see org.melati.poem.prepro.TableDef#generateTableBaseJava 
 */

public class AttachmentTableBase extends BoardsTable {

  private Column<Integer> col_id = null;
  private Column<Integer> col_message = null;
  private Column<String> col_filename = null;
  private Column<String> col_path = null;
  private Column<String> col_url = null;
  private Column<Integer> col_size = null;
  private Column<Integer> col_type = null;

 /**
  * Constructor. 
  * 
  * @see org.melati.poem.prepro.TableDef#generateTableBaseJava 
  * @param database          the POEM database we are using
  * @param name              the name of this <code>Table</code>
  * @param definitionSource  which definition is being used
  * @throws PoemException    if anything goes wrong
  */

  public AttachmentTableBase(
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
            return ((Attachment)g).getId();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Attachment)g).setId((Integer)cooked);
          }

          public Field<Integer> asField(Persistent g) {
            return ((Attachment)g).getIdField();
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
            return ((Attachment)g).getId_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Attachment)g).setId_unsafe((Integer)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Attachment)g).getId();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Attachment)g).setId((Integer)raw);
          }
        });

    defineColumn(col_message =
        new Column<Integer>(this, "message",
                   new ReferencePoemType(getBoardsDatabaseTables().
                                             getMessageTable(), false),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Attachment)g).getMessage();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Attachment)g).setMessage((Message)cooked);
          }

          public Field<Integer> asField(Persistent g) {
            return ((Attachment)g).getMessageField();
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.summary;
          }

          protected Searchability defaultSearchability() {
            return Searchability.yes;
          }

          protected Integer defaultDisplayOrderPriority() {
            return new Integer(0);
          }

          protected int defaultDisplayOrder() {
            return 1;
          }

          protected String defaultDescription() {
            return "The message this attachment belongs to";
          }

          protected boolean defaultIndexed() {
            return true;
          }

          protected String defaultRenderinfo() {
            return "SelectionWindow";
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Attachment)g).getMessage_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Attachment)g).setMessage_unsafe((Integer)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Attachment)g).getMessageTroid();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Attachment)g).setMessageTroid((Integer)raw);
          }

          public StandardIntegrityFix defaultIntegrityFix() {
            return StandardIntegrityFix.delete;
          }
        });

    defineColumn(col_filename =
        new Column<String>(this, "filename",
                   new StringPoemType(false, -1),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Attachment)g).getFilename();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Attachment)g).setFilename((String)cooked);
          }

          public Field<String> asField(Persistent g) {
            return ((Attachment)g).getFilenameField();
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

          protected Searchability defaultSearchability() {
            return Searchability.yes;
          }

          protected Integer defaultDisplayOrderPriority() {
            return new Integer(1);
          }

          protected int defaultDisplayOrder() {
            return 2;
          }

          protected String defaultDescription() {
            return "The filename of this attachment";
          }

          protected int defaultWidth() {
            return 40;
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Attachment)g).getFilename_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Attachment)g).setFilename_unsafe((String)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Attachment)g).getFilename();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Attachment)g).setFilename((String)raw);
          }
        });

    defineColumn(col_path =
        new Column<String>(this, "path",
                   new StringPoemType(false, -1),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Attachment)g).getPath();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Attachment)g).setPath((String)cooked);
          }

          public Field<String> asField(Persistent g) {
            return ((Attachment)g).getPathField();
          }

          protected boolean defaultUserCreateable() {
            return false;
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.record;
          }

          protected Searchability defaultSearchability() {
            return Searchability.yes;
          }

          protected int defaultDisplayOrder() {
            return 3;
          }

          protected String defaultDescription() {
            return "The path to this attachment";
          }

          protected int defaultWidth() {
            return 40;
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Attachment)g).getPath_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Attachment)g).setPath_unsafe((String)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Attachment)g).getPath();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Attachment)g).setPath((String)raw);
          }
        });

    defineColumn(col_url =
        new Column<String>(this, "url",
                   new StringPoemType(false, -1),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Attachment)g).getUrl();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Attachment)g).setUrl((String)cooked);
          }

          public Field<String> asField(Persistent g) {
            return ((Attachment)g).getUrlField();
          }

          protected boolean defaultUserCreateable() {
            return false;
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.record;
          }

          protected Searchability defaultSearchability() {
            return Searchability.yes;
          }

          protected int defaultDisplayOrder() {
            return 4;
          }

          protected String defaultDescription() {
            return "A url to this attachment";
          }

          protected int defaultWidth() {
            return 40;
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Attachment)g).getUrl_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Attachment)g).setUrl_unsafe((String)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Attachment)g).getUrl();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Attachment)g).setUrl((String)raw);
          }
        });

    defineColumn(col_size =
        new Column<Integer>(this, "size",
                   new IntegerPoemType(false),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Attachment)g).getSize();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Attachment)g).setSize((Integer)cooked);
          }

          public Field<Integer> asField(Persistent g) {
            return ((Attachment)g).getSizeField();
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

          protected int defaultDisplayOrder() {
            return 5;
          }

          protected String defaultDescription() {
            return "The size of the file in bytes";
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Attachment)g).getSize_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Attachment)g).setSize_unsafe((Integer)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Attachment)g).getSize();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Attachment)g).setSize((Integer)raw);
          }
        });

    defineColumn(col_type =
        new Column<Integer>(this, "type",
                   new ReferencePoemType(getBoardsDatabaseTables().
                                             getAttachmentTypeTable(), false),
                   DefinitionSource.dsd) { 
          public Object getCooked(Persistent g)
              throws AccessPoemException, PoemException {
            return ((Attachment)g).getType();
          }

          public void setCooked(Persistent g, Object cooked)
              throws AccessPoemException, ValidationPoemException {
            ((Attachment)g).setType((AttachmentType)cooked);
          }

          public Field<Integer> asField(Persistent g) {
            return ((Attachment)g).getTypeField();
          }

          protected DisplayLevel defaultDisplayLevel() {
            return DisplayLevel.summary;
          }

          protected Searchability defaultSearchability() {
            return Searchability.yes;
          }

          protected int defaultDisplayOrder() {
            return 6;
          }

          protected String defaultDescription() {
            return "The type of this attachment";
          }

          public Object getRaw_unsafe(Persistent g)
              throws AccessPoemException {
            return ((Attachment)g).getType_unsafe();
          }

          public void setRaw_unsafe(Persistent g, Object raw)
              throws AccessPoemException {
            ((Attachment)g).setType_unsafe((Integer)raw);
          }

          public Object getRaw(Persistent g)
              throws AccessPoemException {
            return ((Attachment)g).getTypeTroid();
          }

          public void setRaw(Persistent g, Object raw)
              throws AccessPoemException {
            ((Attachment)g).setTypeTroid((Integer)raw);
          }
        });
  }


 /**
  * Retrieves the <code>Id</code> <code>Column</code> for this 
  * <code>Attachment</code> <code>Table</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the id <code>Column</code>
  */
  public final Column<Integer> getIdColumn() {
    return col_id;
  }


 /**
  * Retrieves the <code>Message</code> <code>Column</code> for this 
  * <code>Attachment</code> <code>Table</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the message <code>Column</code>
  */
  public final Column<Integer> getMessageColumn() {
    return col_message;
  }


 /**
  * Retrieves the <code>Filename</code> <code>Column</code> for this 
  * <code>Attachment</code> <code>Table</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the filename <code>Column</code>
  */
  public final Column<String> getFilenameColumn() {
    return col_filename;
  }


 /**
  * Retrieves the <code>Path</code> <code>Column</code> for this 
  * <code>Attachment</code> <code>Table</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the path <code>Column</code>
  */
  public final Column<String> getPathColumn() {
    return col_path;
  }


 /**
  * Retrieves the <code>Url</code> <code>Column</code> for this 
  * <code>Attachment</code> <code>Table</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the url <code>Column</code>
  */
  public final Column<String> getUrlColumn() {
    return col_url;
  }


 /**
  * Retrieves the <code>Size</code> <code>Column</code> for this 
  * <code>Attachment</code> <code>Table</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the size <code>Column</code>
  */
  public final Column<Integer> getSizeColumn() {
    return col_size;
  }


 /**
  * Retrieves the <code>Type</code> <code>Column</code> for this 
  * <code>Attachment</code> <code>Table</code>.
  * 
  * @see org.melati.poem.prepro.FieldDef#generateColAccessor 
  * @return the type <code>Column</code>
  */
  public final Column<Integer> getTypeColumn() {
    return col_type;
  }


 /**
  * Retrieve the <code>Attachment</code> as a <code>Attachment</code>.
  *
  * @see org.melati.poem.prepro.TableDef#generateTableBaseJava 
  * @param troid a Table Row Oject ID
  * @return the <code>Persistent</code> identified by the <code>troid</code>
  */
  public Attachment getAttachmentObject(Integer troid) {
    return (Attachment)getObject(troid);
  }


 /**
  * Retrieve the <code>Attachment</code> 
  * as a <code>Attachment</code>.
  *
  * @see org.melati.poem.prepro.TableDef#generateTableBaseJava 
  * @param troid a Table Row Object ID
  * @return the <code>Persistent</code> identified   */
  public Attachment getAttachmentObject(int troid) {
    return (Attachment)getObject(troid);
  }

  protected JdbcPersistent _newPersistent() {
    return new Attachment();
  }
  protected String defaultDescription() {
    return "A file sent as an attachment to a message";
  }

  protected String defaultCategory() {
    return "Boards";
  }

  protected int defaultDisplayOrder() {
    return 1070;
  }
}

