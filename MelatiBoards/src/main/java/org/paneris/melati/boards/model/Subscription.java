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
      Board b = getBoardsDatabase().getBoardTable().
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
      Board b = getBoardsDatabase().getBoardTable().
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
      Board b = getBoardsDatabase().getBoardTable().
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
      Board b = getBoardsDatabase().getBoardTable().
                    getBoardObject(getBoard_unsafe());
      if (!b.canManage((User)token) || isUsersDetails((User)token))
        throw new AccessPoemException(token,
                                      new Capability("Logged In or Manager"));
    } catch (ClassCastException e) {
        throw new AccessPoemException(token,
                                      new Capability("Logged In or Manager"));
    }
  }

  public void setUser_unsafe(Integer cooked) {
    if (getDatabase().getUserTable().guestUser().troid() == cooked ||
        getDatabase().getUserTable().administratorUser().troid() == cooked)
      throw new SubscribingGuestException(
        "You cannot subscribe the guest or admin user to a board");
   super.setUser_unsafe(cooked);
  }

}
