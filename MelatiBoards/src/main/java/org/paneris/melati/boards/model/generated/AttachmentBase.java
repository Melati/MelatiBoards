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

public abstract class AttachmentBase extends Persistent {

  public BoardsDatabaseTables getBoardsDatabaseTables() {
    return (BoardsDatabaseTables)getDatabase();
  }

  public AttachmentTable getAttachmentTable() {
    return (AttachmentTable)getTable();
  }

  private AttachmentTable _getAttachmentTable() {
    return (AttachmentTable)getTable();
  }

  protected Integer id;
  protected Integer message;
  protected String filename;
  protected String path;
  protected String url;
  protected Integer size;
  protected Integer type;

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
    _getAttachmentTable().getIdColumn().getType().assertValidCooked(cooked);
    writeLock();
    setId_unsafe(cooked);
  }

  public final void setId(int cooked)
      throws AccessPoemException, ValidationPoemException {
    setId(new Integer(cooked));
  }

  public Field getIdField() throws AccessPoemException {
    Column c = _getAttachmentTable().getIdColumn();
    return new Field(c.getRaw(this), c);
  }

  public Integer getMessage_unsafe() {
    return message;
  }

  public void setMessage_unsafe(Integer cooked) {
    message = cooked;
  }

  public Integer getMessageTroid()
      throws AccessPoemException {
    readLock();
    return getMessage_unsafe();
  }

  public void setMessageTroid(Integer raw)
      throws AccessPoemException {
    setMessage(raw == null ? null : 
        getBoardsDatabaseTables().getMessageTable().getMessageObject(raw));
  }

  public Message getMessage()
      throws AccessPoemException, NoSuchRowPoemException {
    Integer troid = getMessageTroid();
    return troid == null ? null :
        getBoardsDatabaseTables().getMessageTable().getMessageObject(troid);
  }

  public void setMessage(Message cooked)
      throws AccessPoemException {
    _getAttachmentTable().getMessageColumn().getType().assertValidCooked(cooked);
    writeLock();
    if (cooked == null)
      setMessage_unsafe(null);
    else {
      cooked.existenceLock();
      setMessage_unsafe(cooked.troid());
    }
  }

  public Field getMessageField() throws AccessPoemException {
    Column c = _getAttachmentTable().getMessageColumn();
    return new Field(c.getRaw(this), c);
  }

  public String getFilename_unsafe() {
    return filename;
  }

  public void setFilename_unsafe(String cooked) {
    filename = cooked;
  }

  public String getFilename()
      throws AccessPoemException {
    readLock();
    return getFilename_unsafe();
  }

  public void setFilename(String cooked)
      throws AccessPoemException, ValidationPoemException {
    _getAttachmentTable().getFilenameColumn().getType().assertValidCooked(cooked);
    writeLock();
    setFilename_unsafe(cooked);
  }

  public Field getFilenameField() throws AccessPoemException {
    Column c = _getAttachmentTable().getFilenameColumn();
    return new Field(c.getRaw(this), c);
  }

  public String getPath_unsafe() {
    return path;
  }

  public void setPath_unsafe(String cooked) {
    path = cooked;
  }

  public String getPath()
      throws AccessPoemException {
    readLock();
    return getPath_unsafe();
  }

  public void setPath(String cooked)
      throws AccessPoemException, ValidationPoemException {
    _getAttachmentTable().getPathColumn().getType().assertValidCooked(cooked);
    writeLock();
    setPath_unsafe(cooked);
  }

  public Field getPathField() throws AccessPoemException {
    Column c = _getAttachmentTable().getPathColumn();
    return new Field(c.getRaw(this), c);
  }

  public String getUrl_unsafe() {
    return url;
  }

  public void setUrl_unsafe(String cooked) {
    url = cooked;
  }

  public String getUrl()
      throws AccessPoemException {
    readLock();
    return getUrl_unsafe();
  }

  public void setUrl(String cooked)
      throws AccessPoemException, ValidationPoemException {
    _getAttachmentTable().getUrlColumn().getType().assertValidCooked(cooked);
    writeLock();
    setUrl_unsafe(cooked);
  }

  public Field getUrlField() throws AccessPoemException {
    Column c = _getAttachmentTable().getUrlColumn();
    return new Field(c.getRaw(this), c);
  }

  public Integer getSize_unsafe() {
    return size;
  }

  public void setSize_unsafe(Integer cooked) {
    size = cooked;
  }

  public Integer getSize()
      throws AccessPoemException {
    readLock();
    return getSize_unsafe();
  }

  public void setSize(Integer cooked)
      throws AccessPoemException, ValidationPoemException {
    _getAttachmentTable().getSizeColumn().getType().assertValidCooked(cooked);
    writeLock();
    setSize_unsafe(cooked);
  }

  public final void setSize(int cooked)
      throws AccessPoemException, ValidationPoemException {
    setSize(new Integer(cooked));
  }

  public Field getSizeField() throws AccessPoemException {
    Column c = _getAttachmentTable().getSizeColumn();
    return new Field(c.getRaw(this), c);
  }

  public Integer getType_unsafe() {
    return type;
  }

  public void setType_unsafe(Integer cooked) {
    type = cooked;
  }

  public Integer getTypeTroid()
      throws AccessPoemException {
    readLock();
    return getType_unsafe();
  }

  public void setTypeTroid(Integer raw)
      throws AccessPoemException {
    setType(raw == null ? null : 
        getBoardsDatabaseTables().getAttachmentTypeTable().getAttachmentTypeObject(raw));
  }

  public AttachmentType getType()
      throws AccessPoemException, NoSuchRowPoemException {
    Integer troid = getTypeTroid();
    return troid == null ? null :
        getBoardsDatabaseTables().getAttachmentTypeTable().getAttachmentTypeObject(troid);
  }

  public void setType(AttachmentType cooked)
      throws AccessPoemException {
    _getAttachmentTable().getTypeColumn().getType().assertValidCooked(cooked);
    writeLock();
    if (cooked == null)
      setType_unsafe(null);
    else {
      cooked.existenceLock();
      setType_unsafe(cooked.troid());
    }
  }

  public Field getTypeField() throws AccessPoemException {
    Column c = _getAttachmentTable().getTypeColumn();
    return new Field(c.getRaw(this), c);
  }
}
