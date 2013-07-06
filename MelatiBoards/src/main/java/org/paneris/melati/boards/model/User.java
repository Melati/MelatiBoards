package org.paneris.melati.boards.model;


import org.melati.poem.Column;
import org.melati.poem.util.StringUtils;
import org.paneris.melati.boards.model.generated.UserBase;

/**
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>Persistent</code> <code>User</code> object.
 * 
 * <p> 
 * Description: 
 *   A board user (with an email address). 
 * </p>
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
 * see org.melati.poem.prepro.TableDef#generatePersistentJava 
 */
public class User extends UserBase {

 /**
  * Constructor 
  * for a <code>Persistent</code> <code>User</code> object.
  * <p>
  * Description: 
  *   A board user (with an email address). 
  * </p>
  * 
  * see org.melati.poem.prepro.TableDef#generatePersistentJava 
  */
  public User() { 
    super();
}

  // programmer's domain-specific code here
  
  
 /**
  * Create the default field values.
  */

  public void generateDefaults() {
    if (getPassword() == null || getPassword().equals("")) setPassword(StringUtils.randomString(6));
    if (getLogin() == null || getLogin().equals("")) setLogin(generateLogin());
    // we must have a name, but it should not be the email address as it would 
    // not be fair to expose the user's email address on systems where this should
    // be kept hidden.
    if (getName() == null || getName().equals("")) setName(generateName());
  }

  /**
   * By default name = login. 
   * @return a generated name
   */
  public String generateName() {
     return getLogin();
   }

  /**
   * Calculates the login id from the user name.  the string before the 
   * 1st ' ', '@' or '.' is extracted, and then made unique.
   * 
   * override this to do your own thing
   */
  public String generateLogin() {
    String loginid = getName();
    
    // no name - try email
    if (loginid == null || loginid.equals("")) loginid = getEmail();
    // ahhh - still none - randomise
    if (loginid == null || loginid.equals("")) return StringUtils.randomString(6);
    
    int space = loginid.indexOf(' ');
    if (space > 0) {
      loginid = loginid.substring(0, space);
      space++;
      if (space < loginid.length()) loginid += loginid.charAt(space);
    } else {
      // try and make the best of it if we have a name that is 
      // actually an email address
      int at = loginid.indexOf('@');
      int dot = loginid.indexOf('.');
      if (dot != -1 && dot < at) at = dot;
      if (at > 0) loginid = loginid.substring(0, at);
    }
    
    // check to see if we already have this login id
    Column<String> loginColumn = getBoardsDatabaseTables().getUserTable().
                             getLoginColumn();
    boolean found = loginColumn.selectionWhereEq(loginid).hasMoreElements();
    String testId = new String(loginid);
    int count = 0;
    while (found) {
      count++;
      testId = new String(loginid);
      String testIdString = "" + count;
      for (int i=0; i < (2 - testIdString.length()); i++) {
        testId += "0";
      }
      testId += count;
      found = loginColumn.selectionWhereEq(testId).hasMoreElements();
    }
    return testId.trim();
  }

  
}

