package org.paneris.melati.boards.model;


import org.paneris.melati.boards.model.generated.UserTableBase;
import org.melati.poem.DefinitionSource;
import org.melati.poem.Database;
import org.melati.poem.PoemException;

/**
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>UserTable</code> object.
 * <p>
 * Description: 
 *   A board user (with an email address). 
 * </p>
 *
 * 
 * <table> 
 * <tr><th colspan='3'>
 * Field summary for SQL table <code>User</code>
 * </th></tr>
 * <tr><th>Name</th><th>Type</th><th>Description</th></tr>
 * <tr><td> email </td><td> String </td><td> The user's email address 
 * </td></tr> 
 * </table> 
 * 
 * see  org.melati.poem.prepro.TableDef#generateTableJava 
 */
public class UserTable<T extends User> extends UserTableBase<User> {

 /**
  * Constructor.
  * 
  * see org.melati.poem.prepro.TableDef#generateTableJava 
  * @param database          the POEM database we are using
  * @param name              the name of this <code>Table</code>
  * @param definitionSource  which definition is being used
  * @throws PoemException    if anything goes wrong
  */
  public UserTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  // programmer's domain-specific code here
  

  public void init() throws PoemException {
    super.init();

    // We can't put this in the constructor because the email
    // column isn't defined until we've called UserTableBase.init()
    getEmailColumn().setRaw_unsafe(guestUser, "");
    getEmailColumn().setRaw_unsafe(administratorUser, "");
  }
  
}

