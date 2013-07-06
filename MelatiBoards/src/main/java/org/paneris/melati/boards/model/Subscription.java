package org.paneris.melati.boards.model;


import org.melati.poem.AccessPoemException;
import org.melati.poem.AccessToken;
import org.melati.poem.Capability;
import org.melati.poem.CreationAccessPoemException;
import org.melati.poem.PoemThread;
import org.paneris.melati.boards.model.generated.SubscriptionBase;

/**
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>Persistent</code> <code>Subscription</code> object.
 * 
 * <p> 
 * Description: 
 *   Which users are members of which boards with what settings. 
 * </p>
 * 
 * <table> 
 * <tr><th colspan='3'>
 * Field summary for SQL table <code>Subscription</code>
 * </th></tr>
 * <tr><th>Name</th><th>Type</th><th>Description</th></tr>
 * <tr><td> id </td><td> Integer </td><td> &nbsp; </td></tr> 
 * <tr><td> user </td><td> User </td><td> The user </td></tr> 
 * <tr><td> board </td><td> Board </td><td> The board to which this user 
 * belongs </td></tr> 
 * <tr><td> status </td><td> MembershipStatus </td><td> How users would like 
 * to receive emails from this board </td></tr> 
 * <tr><td> ismanager </td><td> Boolean </td><td> Can the user administrator 
 * the board with manager priviledges? </td></tr> 
 * <tr><td> approved </td><td> Boolean </td><td> Has this user's subscription 
 * to this board been approved by a manager (if the board has moderated 
 * subscription) </td></tr> 
 * </table> 
 * 
 * see org.melati.poem.prepro.TableDef#generatePersistentJava 
 */
public class Subscription extends SubscriptionBase {

 /**
  * Constructor 
  * for a <code>Persistent</code> <code>Subscription</code> object.
  * <p>
  * Description: 
  *   Which users are members of which boards with what settings. 
  * </p>
  * 
  * see org.melati.poem.prepro.TableDef#generatePersistentJava 
  */
  public Subscription() { 
    super();
}

  // programmer's domain-specific code here

  /**
   * Check user against another.
   * 
   */
   public boolean isUsersDetails(User userP) {
     return getUser_unsafe().equals(userP.troid());
   }

  /**
   * Approve a subscription.
   * 
   */
   public void approve() {
     setApproved(Boolean.TRUE);
   }

     /**
    * A member can change their own membership status - unless they are banned!
    */
   public void setUsersStatus(MembershipStatus status) {

     AccessToken token = PoemThread.accessToken();
     
     try {
       if (!isUsersDetails((User)token))
         throw new AccessPoemException(token, new Capability("Own Details"));
       if (getStatus() == getBoardsDatabaseTables().getMembershipStatusTable().getBanned())
         throw new AccessPoemException(token, new Capability("Your are banned from this Board"));
     } catch (ClassCastException e) {
         throw new AccessPoemException(token, new Capability("Own Details"));
     }
     setStatus_unsafe(status.troid());
   }


  /**
   * A user can read the record if they have canViewMembers rights for
   * the appropriate board or it's their subscription.
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
   * don't want member altering their 'approved' or 'ismanager' settings.
   * <p>
   * For a user to change his subscription status, use
   * setMembershipStatus(MembershipStatus status)
   */
   public void assertCanWrite(AccessToken token) throws AccessPoemException {
     assertCanRead(token);
   }


  /**
   * A user can create a new record if they have subscribe rights for
   * the relevant board.
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
   * A manager can delete a user's subscription. So can the user 
   *  (unless they are banned).
   */
   public void assertCanDelete(AccessToken token) throws AccessPoemException {

     if (token == AccessToken.root) return;
     try {
       Board b = getBoardsDatabaseTables().getBoardTable().
                     getBoardObject(getBoard_unsafe());
       User u = (User)token;
       if (!b.canManage(u) && !isUsersDetails(u))
         throw new AccessPoemException(token,
                                       new Capability("Logged In or Manager"));
       if (b.isBanned(u))
         throw new AccessPoemException(token,
                                       new Capability("Not Banned"));
     } catch (ClassCastException e) {
         throw new AccessPoemException(token,
                                       new Capability("Logged In or Manager"));
     }
   }

  /**
   *  Subscribe a user.
   *
   * @todo Throw a login provoking error.
   */
   public void setUser_unsafe(Integer cooked) {

     if (getDatabase().getUserTable().guestUser().troid() == cooked)
       throw new SubscribingGuestException(
         "You cannot subscribe the guest user to a board");
     super.setUser_unsafe(cooked);
   }

}

