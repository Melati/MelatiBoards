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

import org.melati.poem.Database;
import org.melati.poem.DefinitionSource;
import org.melati.poem.PoemException;
import org.melati.poem.TableInfo;
import org.paneris.melati.boards.model.generated.MembershipStatusTableBase;

public class MembershipStatusTable extends MembershipStatusTableBase {

  private MembershipStatus normal, digest, suspended, banned;

  public MembershipStatusTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

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

  public MembershipStatus getNormal() {
    return normal;
  }

  public MembershipStatus getDigest() {
    return digest;
  }

  public MembershipStatus getSuspended() {
    return suspended;
  }

  public MembershipStatus getBanned() {
    return banned;
  }

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
