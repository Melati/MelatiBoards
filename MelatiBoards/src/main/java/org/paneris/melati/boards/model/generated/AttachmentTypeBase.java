// Do not edit this file!  It was generated by Melati POEM's DSD preprocessor.

package org.paneris.melati.boards.model.generated;


import org.melati.poem.AccessPoemException;
import org.melati.poem.Column;
import org.melati.poem.Field;
import org.melati.poem.Persistent;
import org.melati.poem.ValidationPoemException;
import org.paneris.melati.boards.model.AttachmentTypeTable;
import org.paneris.melati.boards.model.BoardsDatabaseTables;


/**
 * Melati POEM generated base class for persistent AttachmentType.
 * Field summary for SQL table attachmenttype:
 *   id
 *   type
 *
 */
public abstract class AttachmentTypeBase extends Persistent {

  public BoardsDatabaseTables getBoardsDatabaseTables() {
    return (BoardsDatabaseTables)getDatabase();
  }

  public AttachmentTypeTable getAttachmentTypeTable() {
    return (AttachmentTypeTable)getTable();
  }

  private AttachmentTypeTable _getAttachmentTypeTable() {
    return (AttachmentTypeTable)getTable();
  }

  protected Integer id;
  protected String type;

  public Integer getId_unsafe() {
    return id;
  }

  public void setId_unsafe(Integer cooked) {
    id = cooked;
  }

  public Integer getId()
      throws AccessPoemException {
    readLock();
    return getId_unsafe();
  }

  public void setId(Integer cooked)
      throws AccessPoemException, ValidationPoemException {
    _getAttachmentTypeTable().getIdColumn().getType().assertValidCooked(cooked);
    writeLock();
    setId_unsafe(cooked);
  }

  public final void setId(int cooked)
      throws AccessPoemException, ValidationPoemException {
    setId(new Integer(cooked));
  }

  public Field getIdField() throws AccessPoemException {
    Column c = _getAttachmentTypeTable().getIdColumn();
    return new Field(c.getRaw(this), c);
  }

  public String getType_unsafe() {
    return type;
  }

  public void setType_unsafe(String cooked) {
    type = cooked;
  }

  public String getType()
      throws AccessPoemException {
    readLock();
    return getType_unsafe();
  }

  public void setType(String cooked)
      throws AccessPoemException, ValidationPoemException {
    _getAttachmentTypeTable().getTypeColumn().getType().assertValidCooked(cooked);
    writeLock();
    setType_unsafe(cooked);
  }

  public Field getTypeField() throws AccessPoemException {
    Column c = _getAttachmentTypeTable().getTypeColumn();
    return new Field(c.getRaw(this), c);
  }
}
