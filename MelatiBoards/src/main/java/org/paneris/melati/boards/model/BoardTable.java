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
              getBoardsDatabase().getMembershipStatusTable().getNormal(),
              Boolean.TRUE,
              Boolean.TRUE);
        }
      });
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
    if (setting == null)
      throw new SettingNotFoundException(settingName);
    return setting.getValue_unsafe();
  }


}
