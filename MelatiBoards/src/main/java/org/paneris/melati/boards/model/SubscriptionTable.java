package org.paneris.melati.boards.model;

import org.paneris.melati.boards.model.User;
import org.paneris.melati.boards.model.UserTable;
import org.paneris.melati.boards.model.generated.*;
import java.util.*;
import java.sql.Date;
import java.sql.Timestamp;
import org.melati.poem.*;

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
    Enumeration e = selection(
                      getUserColumn().eqClause(user.troid()) + " AND " +
                      getBoardColumn().eqClause(board.troid()));
    while (e.hasMoreElements())
      ((Subscription)e.nextElement()).deleteAndCommit();
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

     Board b = getBoardsDatabase().getBoardTable().
                 getBoardObject(((Subscription)persistent).getBoard_unsafe());
     AccessToken token = PoemThread.accessToken();

     if (token == AccessToken.root)
     // This is the initial manager subscription for a new board
     {
       persistent.setRaw("approved", Boolean.TRUE);
       persistent.setRaw("ismanager", Boolean.TRUE);
     }
     else if (b.canManage((User)token)) {
       persistent.setRaw("approved", Boolean.TRUE);
     }
     else
     {
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
                       getBoardsDatabase().getMembershipStatusTable().
                         getNormal().getTroid()));
  }

  public Enumeration getDigestDistributionList(Board board) {
    return selection(memberSQL(board) + " AND " +
                     getStatusColumn().eqClause(
                       getBoardsDatabase().getMembershipStatusTable().
                         getDigest().getTroid()));
  }
}
