package org.paneris.melati.boards.model;


import java.io.File;

import org.paneris.melati.boards.model.generated.BoardTableBase;
import org.melati.poem.AccessPoemException;
import org.melati.poem.AccessToken;
import org.melati.poem.DefinitionSource;
import org.melati.poem.Database;
import org.melati.poem.InitialisationPoemException;
import org.melati.poem.Persistent;
import org.melati.poem.PoemException;
import org.melati.poem.PoemTask;
import org.melati.poem.PoemThread;
import org.melati.poem.ValidationPoemException;

/**
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>BoardTable</code> object.
 * <p>
 * Description: 
 *   A board for messages. 
 * </p>
 *
 * 
 * <table> 
 * <tr><th colspan='3'>
 * Field summary for SQL table <code>Board</code>
 * </th></tr>
 * <tr><th>Name</th><th>Type</th><th>Description</th></tr>
 * <tr><td> id </td><td> Integer </td><td> &nbsp; </td></tr> 
 * <tr><td> type </td><td> BoardType </td><td> The type of this messageboard 
 * </td></tr> 
 * <tr><td> name </td><td> String </td><td> A code-name for this board 
 * </td></tr> 
 * <tr><td> displayname </td><td> String </td><td> A user-friendly name of 
 * this board </td></tr> 
 * <tr><td> purpose </td><td> String </td><td> The purpose of this message 
 * board </td></tr> 
 * <tr><td> archived </td><td> Boolean </td><td> If a board is archived then 
 * it cannot be viewed and is not displayed on lists by default </td></tr> 
 * <tr><td> opensubscription </td><td> Boolean </td><td> With open 
 * subscription anyone can subscribe to the board. Otherwise a board manager 
 * must subscribe members </td></tr> 
 * <tr><td> moderatedsubscription </td><td> Boolean </td><td> With moderated 
 * subscription the manager must approve all requests to be subscribed 
 * </td></tr> 
 * <tr><td> openposting </td><td> Boolean </td><td> With open posting anyone 
 * with a user account can post a message to this list. Otherwise, only 
 * members can post </td></tr> 
 * <tr><td> moderatedposting </td><td> Boolean </td><td> With moderated 
 * posting all messages must be approved by a manager </td></tr> 
 * <tr><td> openmessageviewing </td><td> Boolean </td><td> With open message 
 * viewing anyone can view messages in a board. Otherwise, only members can 
 * see messages </td></tr> 
 * <tr><td> openmemberlist </td><td> Boolean </td><td> With open member list 
 * anyone can see the members of the list. Otherwise, only members can see 
 * who else is subscribed </td></tr> 
 * <tr><td> attachmentsallowed </td><td> Boolean </td><td> Can attachments be 
 * sent with messages. If not, attachments are ignored </td></tr> 
 * <tr><td> anonymousposting </td><td> Boolean </td><td> Can people without 
 * user accounts post to this messageboard. If so, a user account is created 
 * for them when they post. </td></tr> 
 * <tr><td> attachmentspath </td><td> String </td><td> A path to the 
 * directory containing attachments for this board </td></tr> 
 * <tr><td> attachmentsurl </td><td> String </td><td> A URL to the directory 
 * containing attachments for this board </td></tr> 
 * </table> 
 * 
 * see  org.melati.poem.prepro.TableDef#generateTableJava 
 */
public class BoardTable<T extends Board> extends BoardTableBase<Board> {

 /**
  * Constructor.
  * 
  * see org.melati.poem.prepro.TableDef#generateTableJava 
  * @param database          the POEM database we are using
  * @param name              the name of this <code>Table</code>
  * @param definitionSource  which definition is being used
  * @throws PoemException    if anything goes wrong
  */
  public BoardTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  // programmer's domain-specific code here

  public void init() throws PoemException {
    super.init();

    // Define empty versions of the settings below

  }



  /**************
   * Settings
   **************/

  /**
   * @return the email domain for this board
   */
  public String getBoardsEmailDomain() throws SettingNotFoundException {
    return getSettingValue("BoardsEmailDomain");
  }

  /**
   * @return the base url for the baords
   */
  public String getBoardsSystemURL() throws SettingNotFoundException {
    return getSettingValue("BoardsSystemURL");
  }

  /**
   * @return the templates setting 
   */
  public String getBoardsEmailTemplates() throws SettingNotFoundException {
    return getSettingValue("BoardsEmailTemplates");
  }

  /**
   * @return the attachments path setting
   */
  public String getBoardsAttachmentsPath() throws SettingNotFoundException {
    return getSettingValue("BoardsAttachmentsPath");
  }

  /**
   * @return the attachemnts url path
   */
  public String getBoardsAttachmentsURL() throws SettingNotFoundException {
    return getSettingValue("BoardsAttachmentsURL");
  }

  /**
   * @return the stylesheet url
   */
  public String getBoardsStylesheetURL() throws SettingNotFoundException {
    return getSettingValue("BoardsStylesheetURL");
  }

  /**
   * @return the logical database 
   */
  public String getLogicalDatabase() throws SettingNotFoundException {
    return getSettingValue("LogicalDatabase");
  }

  /**
   * @param settingName the name of a setting to retrieve
   * @return the string value of a setting
   */
  public String getSettingValue(String settingName) throws SettingNotFoundException {
    Setting setting =
      (Setting)getDatabase().getSettingTable().getNameColumn().
                  firstWhereEq(settingName);
    if (setting.getValue_unsafe().equals(""))
      throw new SettingNotFoundException(settingName);
    return setting.getValue_unsafe();
  }


  /**
   * Create a board with a null manager. 
   * {@inheritDoc}
   * @see org.melati.poem.Table#create(org.melati.poem.Persistent)
   */
  public void create(Persistent persistent) {
    create(persistent, null);
  }

  /**
   * Create a board.
   * @param persistent the floating board persistent
   * @param manager the board manager user
   */
  public void create(Persistent persistent, User manager)
      throws AccessPoemException, ValidationPoemException,
             InitialisationPoemException {
    final Board b = (Board)persistent;
    b.setAttachmentspath(
      getBoardsAttachmentsPath() + File.separatorChar + b.getName_unsafe());
    b.setAttachmentsurl(
      getBoardsAttachmentsURL() + File.separatorChar + b.getName_unsafe());
    super.create(persistent);

    // When we have created a new board, subscribe the user who created it
    // as the first manager (unless otherwise supplied)
    if (manager == null) manager = (User)PoemThread.accessToken();
    final User theManager = manager;
    PoemThread.withAccessToken(
      AccessToken.root,
      new PoemTask() {
        public void run() {
          b.subscribe(
              theManager,
              getBoardsDatabaseTables().getMembershipStatusTable().getNormal(),
              Boolean.TRUE,
              Boolean.TRUE);
        }

        public String toString() {
          return "Subscribing the user " + theManager + " to this board";
        }
      });
  }


}

