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
import java.sql.Date;
import java.sql.Timestamp;
import org.melati.poem.*;
import org.melati.*;

public class BoardTable extends BoardTableBase {

  public BoardTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  protected void init() throws PoemException {
    super.init();

    // Define empty versions of the settings below
    
  }

  /**************
   * Settings
   **************/

  public String getBoardsEmailDomain() throws SettingNotFoundException {
    return getSettingValue("BoardsEmailDomain");
  }

  public String getBoardsSystemURL() throws SettingNotFoundException {
    return getSettingValue("BoardsSystemURL");
  }

  public String getBoardsEmailTemplates() throws SettingNotFoundException {
    return getSettingValue("BoardsEmailTemplates");
  }

  public String getBoardsAttachmentsPath() throws SettingNotFoundException {
    return getSettingValue("BoardsAttachmentsPath");
  }

  public String getBoardsAttachmentsURL() throws SettingNotFoundException {
    return getSettingValue("BoardsAttachmentsURL");
  }

  public String getLogicalDatabase() throws SettingNotFoundException {
    return getSettingValue("LogicalDatabase");
  }

  public String getSettingValue(String settingName) throws SettingNotFoundException {
    Setting setting =
      (Setting)getDatabase().getSettingTable().getNameColumn().
                  firstWhereEq(settingName);
//    if (setting == null)
    if (setting.getValue_unsafe().equals(""))
      throw new SettingNotFoundException(settingName);
    return setting.getValue_unsafe();
  }



  public void create(Persistent persistent)
      throws AccessPoemException, ValidationPoemException,
             InitialisationPoemException {

    final Board b = (Board)persistent;
    b.setAttachmentspath(
      getBoardsAttachmentsPath() + File.separatorChar + b.getName_unsafe());
    b.setAttachmentsurl(
      getBoardsAttachmentsURL() + File.separatorChar + b.getName_unsafe());

    super.create(persistent);

    // When we have created a new board, subscribe the user who created it
    // as the first manager
    final User manager = (User)PoemThread.accessToken();
    getDatabase().inSession(
      AccessToken.root,
      new PoemTask() {
        public void run() {
          b.subscribe(
              manager,
              getBoardsDatabaseTables().getMembershipStatusTable().getNormal(),
              Boolean.TRUE,
              Boolean.TRUE);
        }

        public String toString() {
          return "Subscribing the user " + manager + " to this board";
        }
      });
  }

}
