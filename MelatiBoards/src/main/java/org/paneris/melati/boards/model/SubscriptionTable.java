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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Iterator;

import org.melati.poem.AccessPoemException;
import org.melati.poem.AccessToken;
import org.melati.poem.CachedCount;
import org.melati.poem.CachedSelection;
import org.melati.poem.Database;
import org.melati.poem.DefinitionSource;
import org.melati.poem.InitialisationPoemException;
import org.melati.poem.Initialiser;
import org.melati.poem.Persistent;
import org.melati.poem.PoemException;
import org.melati.poem.PoemThread;
import org.melati.poem.ValidationPoemException;
import org.paneris.melati.boards.model.generated.SubscriptionTableBase;

public class SubscriptionTable extends SubscriptionTableBase {

  public SubscriptionTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  // Convenience method
  public Subscription subscribe(final User user,
                                final Board board,
                                final MembershipStatus status,
                                final Boolean ismanager,
                                final Boolean approved) {
    return (Subscription)create(
      new Initialiser() {
        public void init(Persistent object)
            throws AccessPoemException, ValidationPoemException {
          object.setRaw("user", user.troid());
          object.setRaw("board", board.troid());
          object.setRaw("status", status.troid());
          object.setRaw("ismanager", ismanager);
          object.setRaw("approved", approved);
        }
      });
  }

  public void unsubscribe(User user, Board board) {
    List toDelete = new ArrayList();
    for (Enumeration e = selection(
                             getUserColumn().eqClause(user.troid()) + 
                             " AND " +
                             getBoardColumn().eqClause(board.troid()));
       e.hasMoreElements();) {
       toDelete.add(e.nextElement());
    }
    for (Iterator it = toDelete.iterator(); it.hasNext();) {
      Subscription s = (Subscription)it.next();
      s.deleteAndCommit();
    }
  }

  public Subscription getUserSubscription(User user, Board board) {
    Enumeration e = selection(
                      getUserColumn().eqClause(user.troid()) + " AND " +
                      getBoardColumn().eqClause(board.troid()));
    if (e.hasMoreElements())
      return (Subscription)e.nextElement();
    else
      return null;
  }

  /*
   * If they are a manager, observe the isManager/approved.
   * If this is the first subscription, set isManager/approved to true.
   * If they are a general subscriber, set isManager/approved to false
   * (unless moderatedsubscription for the board is false, in which case
   * we can let the user be approved)
   */

  public void create(Persistent persistent)
      throws AccessPoemException, ValidationPoemException,
             InitialisationPoemException {

     Board b = getBoardsDatabaseTables().getBoardTable().
                 getBoardObject(((Subscription)persistent).getBoard_unsafe());
     AccessToken token = PoemThread.accessToken();

     // This is the initial manager subscription for a new board
     if (token == AccessToken.root) {
       persistent.setRaw("approved", Boolean.TRUE);
       persistent.setRaw("ismanager", Boolean.TRUE);
     }
     else if (b.canManage((User)token)) {
       persistent.setRaw("approved", Boolean.TRUE);
     }
     else {
       persistent.setRaw("approved",
         new Boolean(!b.getModeratedsubscription_unsafe().booleanValue()));
       persistent.setRaw("ismanager", Boolean.FALSE);
     }

    super.create(persistent);
  }


  /*
   * Board membership/managership
   */

  public String managerSQL(Board board) {
    return getBoardColumn().eqClause(board.troid()) + " AND " +
           getApprovedColumn().eqClause(Boolean.TRUE) + " AND " +
           getIsmanagerColumn().eqClause(Boolean.TRUE);
  }

  public String memberSQL(Board board) {
    return getBoardColumn().eqClause(board.troid()) + " AND " +
           getApprovedColumn().eqClause(Boolean.TRUE);

  }

  public String pendingSubscriptionSQL(Board board) {
    return getBoardColumn().eqClause(board.troid()) + " AND " +
           getApprovedColumn().eqClause(Boolean.FALSE);

  }

  public boolean isManager(User user, Board board) {
    return exists(getUserColumn().eqClause(user.troid()) + " AND " +
                    managerSQL(board));
  }

  public boolean isMember(User user, Board board) {
    return exists(getUserColumn().eqClause(user.troid()) + " AND " +
                    memberSQL(board));
  }

  public CachedSelection cachedManagers(Board board) {
    return cachedSelection(managerSQL(board), null);
  }

  public CachedSelection cachedMembers(Board board) {
    return cachedSelection(memberSQL(board), null);
  }

  public CachedSelection cachedPendingSubscriptions(Board board) {
    return cachedSelection(pendingSubscriptionSQL(board), null);
  }

  public CachedCount cachedManagerCount(Board board) {
    return cachedCount(managerSQL(board));
  }

  public CachedCount cachedMemberCount(Board board) {
    return cachedCount(memberSQL(board));
  }

  public CachedCount cachedPendingSubscriptionCount(Board board) {
    return cachedCount(pendingSubscriptionSQL(board));
  }

  public Enumeration getNormalDistributionList(Board board) {
    return selection(memberSQL(board) + " AND " +
                     getStatusColumn().eqClause(
                       getBoardsDatabaseTables().getMembershipStatusTable().
                         getNormal().getTroid()));
  }

  public Enumeration getDigestDistributionList(Board board) {
    return selection(memberSQL(board) + " AND " +
                     getStatusColumn().eqClause(
                       getBoardsDatabaseTables().getMembershipStatusTable().
                         getDigest().getTroid()));
  }
}
