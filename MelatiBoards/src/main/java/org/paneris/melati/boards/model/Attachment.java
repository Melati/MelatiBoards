package org.paneris.melati.boards.model;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.melati.poem.AccessPoemException;
import org.melati.poem.ValidationPoemException;
import org.melati.util.UTF8URLEncoder;
import org.melati.util.UnexpectedExceptionException;
import org.paneris.melati.boards.model.generated.AttachmentBase;

/**
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>Persistent</code> <code>Attachment</code> object.
 * 
 * <p> 
 * Description: 
 *   A file sent as an attachment to a message. 
 * </p>
 * 
 * <table> 
 * <tr><th colspan='3'>
 * Field summary for SQL table <code>Attachment</code>
 * </th></tr>
 * <tr><th>Name</th><th>Type</th><th>Description</th></tr>
 * <tr><td> id </td><td> Integer </td><td> &nbsp; </td></tr> 
 * <tr><td> message </td><td> Message </td><td> The message this attachment 
 * belongs to </td></tr> 
 * <tr><td> filename </td><td> String </td><td> The filename of this 
 * attachment </td></tr> 
 * <tr><td> path </td><td> String </td><td> The path to this attachment 
 * </td></tr> 
 * <tr><td> url </td><td> String </td><td> A url to this attachment 
 * </td></tr> 
 * <tr><td> size </td><td> Integer </td><td> The size of the file in bytes 
 * </td></tr> 
 * <tr><td> type </td><td> AttachmentType </td><td> The type of this 
 * attachment </td></tr> 
 * </table> 
 * 
 * see org.melati.poem.prepro.TableDef#generatePersistentJava 
 */
public class Attachment extends AttachmentBase {

 /**
  * Constructor 
  * for a <code>Persistent</code> <code>Attachment</code> object.
  * <p>
  * Description: 
  *   A file sent as an attachment to a message. 
  * </p>
  * 
  * see org.melati.poem.prepro.TableDef#generatePersistentJava 
  */
  public Attachment() { 
    super();
}

  // programmer's domain-specific code here
  

  /**
   * @see org.paneris.melati.boards.model.generated.AttachmentBase#setFilename_unsafe(java.lang.String)
   */
  public void setFilename_unsafe(String cooked) {
    super.setFilename_unsafe(cooked);
    Message thisMessage = (Message)getDatabase().getTable("message").
                        getObject(getMessage_unsafe());
    Board board = (Board)getDatabase().getTable("board").
                        getObject(thisMessage.getBoard_unsafe());
    setPath_unsafe(board.getAttachmentspath_unsafe() +
                         File.separatorChar + cooked);
    setUrl_unsafe(board.getAttachmentsurl_unsafe() +
                         File.separatorChar + UTF8URLEncoder.encode(cooked));
  }

  /**
   * @see org.paneris.melati.boards.model.generated.AttachmentBase#setFilename(java.lang.String)
   */
  public void setFilename(String cooked) {
    super.setFilename(cooked);
    setPath(getMessage().getBoard().getAttachmentspath() +
                         File.separatorChar + cooked);
    setUrl(getMessage().getBoard().getAttachmentsurl() +
                         File.separatorChar + UTF8URLEncoder.encode(cooked));
  }

  /**
   * Ensure that duplicated filenames are made unique.
   * 
   * @param cooked the value to set
   */
  public void setFilename_unique_unsafe(String cooked) {
    cooked = makeUnique(cooked);
    setFilename_unsafe(cooked);
  }

  /**
   * @param cooked
   * @throws AccessPoemException
   * @throws ValidationPoemException
   */
  public void setFilename_unique(String cooked)
      throws AccessPoemException, ValidationPoemException {
    cooked = makeUnique(cooked);
    setFilename(cooked);
  }

 /** 
  * There are some situations where the messageboard has to deal with 
  * attachments that don't have a filename!  I have no idea why, but it is 
  * bound to be microsoft's fault.
  * If we have null, than set it to a default string.  Perhaps this
  * should be stored in the setting table, but perhaps it's not worthwhile.
  */
  public String makeUnique(String fileName) {
    if (fileName == null) fileName = "attachment";
    Message thisMessage = (Message)getDatabase().getTable("message").
                        getObject(getMessage_unsafe());
    Board board = (Board)getDatabase().getTable("board").
                        getObject(thisMessage.getBoard_unsafe());
    File testFile;
    // throw nicer exception if it goes tits up
    try {
      testFile = new File(board.getAttachmentspath_unsafe(), fileName);
    } catch (Exception e) {
      throw new UnexpectedExceptionException("Failed to create a file in directory:" +
                            board.getAttachmentspath_unsafe() + " filename:" + fileName, e);
    }
    int dot = fileName.lastIndexOf(".");
    String start = (dot != -1) ? fileName.substring(0, dot) : fileName;
    String extension = (dot != -1)
                        ? fileName.substring(dot, fileName.length()) : "";
    int count = 0;
    while (testFile.exists()) {
      fileName = start + (count++) + extension;
      testFile = new File(board.getAttachmentspath_unsafe(), fileName);
    }
    return testFile.getName();
  }

  /**
   * Write the content to this attachment. It tries to create the necessary
   * directories if they do not exist
   */
  public void writeData(byte[] content)
                                throws FileNotFoundException, IOException {
    writeDataToFile(new File(getPath()), content);
  }

  /**
   * Write the content to this attachment. It tries to create the necessary
   * directories if they do not exist
   */
  public void writeData_unsafe(byte[] content)
                                throws FileNotFoundException, IOException {
    writeDataToFile(new File(getPath_unsafe()), content);
  }

  private void writeDataToFile(File attachment, byte[] content)
                                throws FileNotFoundException, IOException {
    attachment.getParentFile().mkdirs();
    FileOutputStream fos = new FileOutputStream(attachment);
    fos.write(content);
    fos.close();
  }

  
}

