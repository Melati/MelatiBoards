package org.paneris.melati.boards.model;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.paneris.melati.boards.model.generated.SubscriptionTableBase;
import org.melati.poem.AccessPoemException;
import org.melati.poem.AccessToken;
import org.melati.poem.CachedCount;
import org.melati.poem.CachedSelection;
import org.melati.poem.DefinitionSource;
import org.melati.poem.Database;
import org.melati.poem.InitialisationPoemException;
import org.melati.poem.Initialiser;
import org.melati.poem.Persistent;
import org.melati.poem.PoemException;
import org.melati.poem.PoemThread;
import org.melati.poem.ValidationPoemException;

/**
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>SubscriptionTable</code> object.
 * <p>
 * Description: 
 *   Which users are members of which boards with what settings. 
 * </p>
 *
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
 * see  org.melati.poem.prepro.TableDef#generateTableJava 
 */
public class SubscriptionTable<T extends Subscription> extends SubscriptionTableBase<Subscription> {

 /**
  * Constructor.
  * 
  * see org.melati.poem.prepro.TableDef#generateTableJava 
  * @param database          the POEM database we are using
  * @param name              the name of this <code>Table</code>
  * @param definitionSource  which definition is being used
  * @throws PoemException    if anything goes wrong
  */
  public SubscriptionTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  // programmer's domain-specific code here


  /** 
   * Convenience method to subscribe a user to a board.
   */
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

  /** 
   * Convenience method to unsubscribe a user to a board.
   */
   public void unsubscribe(User user, Board board) {
     List<Subscription> toDelete = new ArrayList<Subscription>();
     for (Enumeration<Subscription> e = selection(
                              getUserColumn().eqClause(user.troid()) + 
                              " AND " +
                              getBoardColumn().eqClause(board.troid()));
        e.hasMoreElements();) {
        toDelete.add(e.nextElement());
     }
     for (Iterator<Subscription> it = toDelete.iterator(); it.hasNext();) {
       Subscription s = it.next();
       s.deleteAndCommit();
     }
   }

   /**
    * @return the subscription for a user to a board
    */
   public Subscription getUserSubscription(User user, Board board) {
     Enumeration<Subscription> e = selection(
                       getUserColumn().eqClause(user.troid()) + " AND " +
                       getBoardColumn().eqClause(board.troid()));
     if (e.hasMoreElements())
       return (Subscription)e.nextElement();
     return null;
   }

   /**
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


  /**
   * Board managership SQL.
   */
   public String managerSQL(Board board) {
     return getBoardColumn().eqClause(board.troid()) + " AND " +
            getApprovedColumn().eqClause(Boolean.TRUE) + " AND " +
            getIsmanagerColumn().eqClause(Boolean.TRUE);
   }

  /**
   * Board membership SQL.
   */
   public String memberSQL(Board board) {
     return getBoardColumn().eqClause(board.troid()) + " AND " +
            getApprovedColumn().eqClause(Boolean.TRUE);

   }

  /**
   * Board membership pending SQL.
   */
   public String pendingSubscriptionSQL(Board board) {
     return getBoardColumn().eqClause(board.troid()) + " AND " +
            getApprovedColumn().eqClause(Boolean.FALSE);

   }

  /**
   * Is a board manager SQL.
   */
   public boolean isManager(User user, Board board) {
     return exists(getUserColumn().eqClause(user.troid()) + " AND " +
                     managerSQL(board));
   }

  /**
   * Is a member SQL.
   */
   public boolean isMember(User user, Board board) {
     return exists(getUserColumn().eqClause(user.troid()) + " AND " +
                     memberSQL(board));
   }

  /**
   * @return a <tt>CachedSelection</tt> of the Managers.
   */
   public CachedSelection<Subscription> cachedManagers(Board board) {
     return cachedSelection(managerSQL(board), null);
   }

  /**
   * @return a <tt>CachedSelection</tt> of the Members.
   */
   public CachedSelection<Subscription> cachedMembers(Board board) {
     return cachedSelection(memberSQL(board), null);
   }

  /**
   * @return a <tt>CachedSelection</tt> of the PendingSubscriptions.
   */
   public CachedSelection<Subscription> cachedPendingSubscriptions(Board board) {
     return cachedSelection(pendingSubscriptionSQL(board), null);
   }

  /**
   * @return a <tt>CachedCount</tt> of the Managers.
   */
   public CachedCount cachedManagerCount(Board board) {
     return cachedCount(managerSQL(board));
   }

  /**
   * @return a <tt>CachedCount</tt> of the Members.
   */
   public CachedCount cachedMemberCount(Board board) {
     return cachedCount(memberSQL(board));
   }

  /**
   * @return a <tt>CachedCount</tt> of the PendingSubscriptions.
   */
   public CachedCount cachedPendingSubscriptionCount(Board board) {
     return cachedCount(pendingSubscriptionSQL(board));
   }

  /**
   * @return an <tt>enumeration</tt> of the Members.
   */
   public Enumeration getNormalDistributionList(Board board) {
     return selection(memberSQL(board) + " AND " +
                      getStatusColumn().eqClause(
                        getBoardsDatabaseTables().getMembershipStatusTable().
                          getNormal().getTroid()));
   }

  /**
   * @return an <tt>enumeration</tt> of the Digest Members.
   */
   public Enumeration getDigestDistributionList(Board board) {
     return selection(memberSQL(board) + " AND " +
                      getStatusColumn().eqClause(
                        getBoardsDatabaseTables().getMembershipStatusTable().
                          getDigest().getTroid()));
   }

}

