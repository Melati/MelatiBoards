/*
 * $Source$
 * $Revision$
 *
 * Copyright (C) 2000 Myles Chippendale
 *
 * Part of a Melati application. This application is free software;
 * Permission is granted to copy, distribute and/or modify this
 * software under the same terms as those set out for Melati below.
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

import org.melati.poem.Database;
import org.melati.poem.DefinitionSource;
import org.melati.poem.PoemException;
import org.melati.poem.TableInfo;
import org.paneris.melati.boards.model.generated.MembershipStatusTableBase;

/**
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>MembershipStatusTable</code> object.
 * <p>
 * Description: 
 *   The status of a user's subscription to a board. 
 * </p>
 *
 * 
 * <table> 
 * <tr><th colspan='3'>
 * Field summary for SQL table <code>MembershipStatus</code>
 * </th></tr>
 * <tr><th>Name</th><th>Type</th><th>Description</th></tr>
 * <tr><td> id </td><td> Integer </td><td> &nbsp; </td></tr> 
 * <tr><td> status </td><td> String </td><td> The name of a status </td></tr> 
 * </table> 
 * 
 * @generator  org.melati.poem.prepro.TableDef#generateTableMainJava 
 */
public class MembershipStatusTable extends MembershipStatusTableBase {

 /**
  * Constructor.
  * 
  * @generator org.melati.poem.prepro.TableDef#generateTableMainJava 
  * @param database          the POEM database we are using
  * @param name              the name of this <code>Table</code>
  * @param definitionSource  which definition is being used
  * @throws PoemException    if anything goes wrong
  */
  public MembershipStatusTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  // programmer's domain-specific code here

  private MembershipStatus normal, digest, suspended, banned;

  protected void postInitialise() {
    super.postInitialise();

    normal = ensure("normal");
    digest = ensure("digest");
    suspended = ensure("suspended");
    banned = ensure("banned");

    TableInfo info = getInfo();
    if (info.getDefaultcanwrite() == null)
      info.setDefaultcanwrite(getDatabase().administerCapability());
    if (info.getCancreate() == null)
      info.setCancreate(getDatabase().administerCapability());
  }

 /**
  * @return the Normal MembershipStatus.
  */
  public MembershipStatus getNormal() {
    return normal;
  }

 /**
  * @return the Digest MembershipStatus.
  */
  public MembershipStatus getDigest() {
    return digest;
  }

 /**
  * @return the Suspended MembershipStatus.
  */
  public MembershipStatus getSuspended() {
    return suspended;
  }

 /**
  * @return the Banned MembershipStatus.
  */
  public MembershipStatus getBanned() {
    return banned;
  }

 /**
  * Make sure that a record exists.
  *
  * @param name   of the Membershipstatus to ensure.
  * @return the existing or newly created MembershipStatus of given name.
  */
  public MembershipStatus ensure(String name) {
    MembershipStatus status =
                    (MembershipStatus)getStatusColumn().firstWhereEq(name);
    if (status != null)
      return status;
    else {
      status = (MembershipStatus) newPersistent();
      status.setStatus(name);
      return (MembershipStatus)getStatusColumn().ensure(status);
    }
  }

}
