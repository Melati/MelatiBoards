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

import org.paneris.melati.boards.model.User;
import org.paneris.melati.boards.model.UserTable;
import org.paneris.melati.boards.model.generated.*;
import java.util.*;
import java.sql.Date;
import java.sql.Timestamp;
import org.melati.poem.*;
import org.melati.util.*;


public class Subscription extends SubscriptionBase {
  public Subscription() {}

  public boolean isUsersDetails(User user) {
    return getUser_unsafe().equals(user.troid());
  }

  public void approve() {
    setApproved(Boolean.TRUE);
  }

  /*
   * A member can change their own membership status
   */
  public void setUsersStatus(MembershipStatus status) {
    AccessToken token = PoemThread.accessToken();
    try {
      if (!isUsersDetails((User)token))
        throw new AccessPoemException(token, new Capability("Own Details"));
    } catch (ClassCastException e) {
        throw new AccessPoemException(token, new Capability("Own Details"));
    }
    setStatus_unsafe(status.troid());
  }

  /**
   * A user can read the record if they have canViewMembers rights for
   * the appropriate board or it's their subscription
   */
  public void assertCanRead(AccessToken token) throws AccessPoemException {
    if (token == AccessToken.root) return;
    try {
      Board b = getBoardsDatabaseTables().getBoardTable().
                    getBoardObject(getBoard_unsafe());
      if (!isUsersDetails((User)token) && !b.canViewMembers((User)token))
        throw new AccessPoemException(token, new Capability("Member"));
    } catch (ClassCastException e) {
        throw new AccessPoemException(token, new Capability("Member"));
    }
  }

  /**
   * Only a manager can generally alter a user's membership, 'cos we
   * don't want member altering their 'approved' or 'ismanager' settings
   *
   * For a user to change his subscription status, use
   * setMembershipStatus(MembershipStatus status)
   */
  public void assertCanWrite(AccessToken token) throws AccessPoemException {
    if (token == AccessToken.root) return;
    try {
      Board b = getBoardsDatabaseTables().getBoardTable().
                    getBoardObject(getBoard_unsafe());
      if (!b.canManage((User)token))
        throw new AccessPoemException(token,
                                      new Capability("Logged In or Manager"));
    } catch (ClassCastException e) {
        throw new AccessPoemException(token,
                                      new Capability("Logged In or Manager"));
    }
  }


  /**
   * A user can create a new record if they have subscribe rights for
   * the relevant board
   */
  public void assertCanCreate(AccessToken token)
                    throws CreationAccessPoemException {
    if (token == AccessToken.root) return;
    try {
      Board b = getBoardsDatabaseTables().getBoardTable().
                    getBoardObject(getBoard_unsafe());
      if (!b.canSubscribe((User)token))
        throw new CreationAccessPoemException(getTable(), token,
                                            new Capability("Manager"));
    } catch (ClassCastException e) {
        throw new CreationAccessPoemException(getTable(), token,
                                            new Capability("Manager"));
    }
  }

  /**
   * A manager can delete a user's subscription. So can the user.
   */
  public void assertCanDelete(AccessToken token) throws AccessPoemException {
    if (token == AccessToken.root) return;
    try {
      Board b = getBoardsDatabaseTables().getBoardTable().
                    getBoardObject(getBoard_unsafe());
      if (!b.canManage((User)token) && !isUsersDetails((User)token))
        throw new AccessPoemException(token,
                                      new Capability("Logged In or Manager"));
    } catch (ClassCastException e) {
        throw new AccessPoemException(token,
                                      new Capability("Logged In or Manager"));
    }
  }

  public void setUser_unsafe(Integer cooked) {
    if (getDatabase().getUserTable().guestUser().troid() == cooked)
      throw new SubscribingGuestException(
        "You cannot subscribe the guest user to a board");
   super.setUser_unsafe(cooked);
  }

}
