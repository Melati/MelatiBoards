package org.paneris.melati.boards.model;

import org.paneris.melati.boards.model.generated.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.sql.Date;
import java.sql.Timestamp;
import org.melati.poem.*;

public class Attachment extends AttachmentBase {
  public Attachment() {}

  public void setFilename_unsafe(String cooked) {
    super.setFilename_unsafe(cooked);
    Message message = (Message)getDatabase().getTable("message").
                        getObject(getMessage_unsafe());
    Board board = (Board)getDatabase().getTable("board").
                        getObject(message.getBoard_unsafe());
    setPath_unsafe(board.getAttachmentspath_unsafe() +
                         File.separatorChar + cooked);
    setUrl_unsafe(board.getAttachmentsurl_unsafe() +
                         File.separatorChar + URLEncoder.encode(cooked));
  }

  public void setFilename(String cooked) {
    super.setFilename(cooked);
    setPath(getMessage().getBoard().getAttachmentspath() +
                         File.separatorChar + cooked);
    setUrl(getMessage().getBoard().getAttachmentsurl() +
                         File.separatorChar + URLEncoder.encode(cooked));
  }

  public void setFilename_unique_unsafe(String cooked) {
    cooked = makeUnique(cooked);
    setFilename_unsafe(cooked);
  }

  public void setFilename_unique(String cooked)
      throws AccessPoemException, ValidationPoemException {
    cooked = makeUnique(cooked);
    setFilename(cooked);
  }

  public String makeUnique(String filename) {
    Message message = (Message)getDatabase().getTable("message").
                        getObject(getMessage_unsafe());
    Board board = (Board)getDatabase().getTable("board").
                        getObject(message.getBoard_unsafe());
    File testFile = new File(board.getAttachmentspath_unsafe(), filename);
    int dot = filename.lastIndexOf(".");
    String start = (dot != -1) ? filename.substring(0,dot) : filename;
    String extension = (dot != -1)
                        ? filename.substring(dot,filename.length()) : "";
    int count = 0;
    while (testFile.exists()) {
      filename = start + (count++) + extension;
      testFile = new File(board.getAttachmentspath_unsafe(), filename);
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
    try {fos.close();} catch (Exception e) {}
  }

}
