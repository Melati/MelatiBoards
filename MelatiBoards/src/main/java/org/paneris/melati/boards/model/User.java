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

import org.paneris.melati.boards.model.UserTable;
import org.paneris.melati.boards.model.generated.*;
import java.util.*;
import java.sql.Date;
import java.sql.Timestamp;
import org.melati.poem.*;
import org.melati.util.StringUtils;

public class User extends UserBase {
  public User() {}
  
  public void generateDefaults() {
    if (getPassword() == null || getPassword().equals("")) setPassword(StringUtils.randomString(6));
    if (getLogin() == null || getLogin().equals("")) setLogin(generateLogin());
    // we must have a name, but it should not be the email address as it would 
    // not be fair to expose the user's email address on systems where this should
    // be kept hidden.
    System.err.println("name:" + getName() + ":");
    if (getName() == null || getName().equals("")) setName(generateName());
    System.err.println("name:" + getName() + ":");
  }

   public String generateName() {
     // by default - name = login
     return getLogin();
   }

  /*
   * this calculates the login id from the user name.  the string before the 
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
      loginid = loginid.substring(0,space);
      space ++;
      if (space < loginid.length()) loginid += loginid.charAt(space);
    } else {
      // try and make the best of it if we have a name that is actually an email address
      int at = loginid.indexOf('@');
      int dot = loginid.indexOf('.');
      if (dot != -1 && dot < at) at = dot;
      if (at > 0) loginid = loginid.substring(0,at);
    }
    
    // check to see if we already have this login id
    Column loginColumn = getBoardsDatabaseTables().getUserTable().getLoginColumn();
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
