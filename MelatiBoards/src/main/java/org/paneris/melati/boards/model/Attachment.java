/*
 * $Source$
 * $Revision$
 *
 * Copyright (C) 2000 Myles Chippendale
 *
 * Part of a Melati application. This application is free software;
 * Permission is granted to copy, distribute and/or modify this
 * software under the same terms as those set out for Melati, below.
 *
 * Melati (http://melati.org) is a framework for the rapid
 * development of clean, maintainable web applications.
 *
 * Melati is free software; Permission is granted to copy, distribute
 * and/or modify this software under the terms either:
 *
 * a) the GNU General Public License as published by the Free Software
 *    Foundation; either version 2 of the License, or (at your option)
 *    any later version,
 *
 *    or
 *
 * b) any version of the Melati Software License, as published
 *    at http://melati.org
 *
 * You should have received a copy of the GNU General Public License and
 * the Melati Software License along with this program;
 * if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA to obtain the
 * GNU General Public License and visit http://melati.org to obtain the
 * Melati Software License.
 *
 * Feel free to contact the Developers of Melati (http://melati.org),
 * if you would like to work out a different arrangement than the options
 * outlined here.  It is our intention to allow Melati to be used by as
 * wide an audience as possible.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * Contact details for copyright holder:
 *
 *     Mylesc Chippendale <mylesc@paneris.org>
 *     http://paneris.org/
 *     29 Stanley Road, Oxford, OX4 1QY, UK
 */

package org.paneris.melati.boards.model;

import org.paneris.melati.boards.model.generated.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.sql.Date;
import java.sql.Timestamp;
import org.melati.poem.*;
import org.melati.util.UnexpectedExceptionException;

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
    // there are some situations where the messageboard has to deal with 
    // attachments that don't have a filename!  I have no idea why, but it is 
    // bound to be microsoft's fault.
    // if we have null, than set it to a default string.  perhaps this
    // should be stored in the setting table, but perhaps it's not worthwhile
    if (filename == null) filename = "attachment";
    Message message = (Message)getDatabase().getTable("message").
                        getObject(getMessage_unsafe());
    Board board = (Board)getDatabase().getTable("board").
                        getObject(message.getBoard_unsafe());
    File testFile;
    // throw nicer exception if it goes tits up
    try {
      testFile = new File(board.getAttachmentspath_unsafe(), filename);
    } catch (Exception e) {
      throw new UnexpectedExceptionException("Failed to create a file in directory:" +
                            board.getAttachmentspath_unsafe() + " filename:" + filename, e);
    }
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
