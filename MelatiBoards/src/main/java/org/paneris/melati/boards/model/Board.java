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

import java.io.File;
import java.lang.ref.SoftReference;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import org.melati.poem.AccessPoemException;
import org.melati.poem.AccessToken;
import org.melati.poem.CachedCount;
import org.melati.poem.CachedSelection;
import org.melati.poem.Capability;
import org.melati.util.ChildrenDrivenMutableTree;
import org.melati.util.Email;
import org.melati.util.EnumUtils;
import org.melati.util.MappedEnumeration;
import org.paneris.melati.boards.model.generated.BoardBase;

public class Board extends BoardBase {
  public Board() {}
  
  /***************************
   * Users levels - Admin, Manager, Member
   ****************************/
  
  public boolean isAdmin(User user) {
    return ((AccessToken)user).givesCapability(
    getDatabase().administerCapability());
  }
  
  public boolean isManager(User user) {
    return getBoardsDatabaseTables().getSubscriptionTable().
    isManager(user, this);
  }
  
  public boolean isMember(User user) {
    return getBoardsDatabaseTables().getSubscriptionTable().
    isMember(user, this);
  }
  
  public boolean isBanned(User user) {
    Subscription subscription = getUserSubscription(user);
    if (subscription != null && subscription.getStatus() == 
        getBoardsDatabaseTables().getMembershipStatusTable().getBanned()) 
           return true;
    return false;
  }

  public void ban(User user) {
    Subscription subscription = getUserSubscription(user);
    subscription.setStatus(
        getBoardsDatabaseTables().getMembershipStatusTable().getBanned());
  }
  
  /***************************
   * List users
   ****************************/
  
  private CachedCount managerCount = null;
  
  public int getManagerCount() {
    if (managerCount == null)
      managerCount =
      getBoardsDatabaseTables().getSubscriptionTable().
      cachedManagerCount(this);
    return managerCount.count();
  }
  
  private CachedSelection managersubs = null;
  
  public Enumeration getManagersSubscriptions() {
    if (managersubs == null)
      managersubs =
      getBoardsDatabaseTables().getSubscriptionTable().cachedManagers(this);
    return managersubs.objects();
  }
  
  public Enumeration getManagers() {
    return new MappedEnumeration(getManagersSubscriptions()) {
      public Object mapped(Object subscription) {
        return ((Subscription)subscription).getUser();
      }
    };
  }
  
  private CachedCount memberCount = null;
  
  public int getMemberCount() {
    if (memberCount == null)
      memberCount =
      getBoardsDatabaseTables().getSubscriptionTable().
      cachedMemberCount(this);
    return memberCount.count();
  }
  
  private CachedSelection membersubs = null;
  
  public Enumeration getMembersSubscriptions() {
    if (membersubs == null)
      membersubs =
      getBoardsDatabaseTables().getSubscriptionTable().cachedMembers(this);
    return membersubs.objects();
  }
  
  public Enumeration getMembers() {
    return new MappedEnumeration(getMembersSubscriptions()) {
      public Object mapped(Object subscription) {
        return ((Subscription)subscription).getUser();
      }
    };
  }
  
  public Enumeration getNormalDistributionList() {
    return getBoardsDatabaseTables().getSubscriptionTable().
    getNormalDistributionList(this);
  }
  
  public Enumeration getDigestDistributionList() {
    return getBoardsDatabaseTables().getSubscriptionTable().
    getDigestDistributionList(this);
  }
  
  /*************************
   * Actions permitted to users of this board
   **************************/
  
  public boolean canSubscribe(User user) {
    if (isBanned(user)) return false;
    return  getOpensubscription().booleanValue() ||
    isManager(user) ||
    isAdmin(user);
  }
  
  // banned people can't unsubscribe
  public boolean canUnSubscribe(User user) {
    if (isBanned(user)) return false;
    return true;
  }
  
  public boolean canPost(User user) {
    if (isBanned(user)) return false;
    return getOpenposting().booleanValue() ||
    isMember(user) ||
    isAdmin(user);
  }
  
  public boolean canViewMessages(User user) {
    if (isBanned(user)) return false;
    return getOpenmessageviewing().booleanValue() ||
    isMember(user) ||
    isAdmin(user);
  }
  
  public boolean canViewMembers(User user) {
    if (isBanned(user)) return false;
    return getOpenmemberlist().booleanValue() ||
    isMember(user) ||
    isAdmin(user);
  }
  
  public boolean canManage(User user) {
    return isManager(user) ||
    isAdmin(user);
  }
  
  
  /*********************
   * Creating/Updating a board
   *********************/
  
  public void assertCanWrite(AccessToken token)
  throws AccessPoemException {
    if (token == AccessToken.root) return;
    try {
      if (!canManage((User)token))
        throw new AccessPoemException(token, new Capability("Manager"));
    } catch (ClassCastException e) {
      throw new AccessPoemException(token, new Capability("Manager"));
    }
  }
  public void assertCanCreate(AccessToken token)
  throws AccessPoemException {
    if (token == AccessToken.root) return;
    try {
      if (token == getDatabase().guestAccessToken())
        throw new AccessPoemException(token, new Capability("Logged In"));
    } catch (ClassCastException e) {
      throw new AccessPoemException(token, new Capability("Logged In"));
    }
  }
  
  // method to allow a board to be created with a different manager than the
  // creating user
  public final void makePersistent(User manager) {
    ((BoardTable)getTable()).create(this, manager);
  }
  
  
  /***************************
   * Subscribing/Unsubscribing
   ***************************/
  
  public void subscribe(User user, MembershipStatus status,
  Boolean ismanager, Boolean approved) {
    Subscription sub =
    getBoardsDatabaseTables().getSubscriptionTable().
    subscribe(user, this, status, ismanager, approved);
    
    if (sub.getApproved_unsafe() == Boolean.FALSE) {
      // FIXME send getSubscriptionRequestReceivedNote() message to user
    }
  }
  
  public void subscribe(User user) {
    subscribe(user,
    getBoardsDatabaseTables().getMembershipStatusTable().
    getNormal(),
    Boolean.FALSE,
    new Boolean(!getModeratedsubscription_unsafe().booleanValue()));
  }
  
  public boolean isSubscribed(User user) {
    return getBoardsDatabaseTables().getSubscriptionTable().isMember(user, this);
  }
  
  public void unsubscribe(User user) {
    getBoardsDatabaseTables().getSubscriptionTable().unsubscribe(user, this);
  }
  
  public Subscription getUserSubscription(User user) {
    return getBoardsDatabaseTables().getSubscriptionTable().
    getUserSubscription(user, this);
  }
  
  
  /****************************************
   * Distribute
   ****************************************/
  
  public void distribute(Message message) {
    
    // Vector of email addresses (Strings)
    Vector members = EnumUtils.vectorOf(
    new MappedEnumeration(getMembers()) {
      public Object mapped(Object member) {
        return ((User)member).getEmail();
      }
    });
    
    // include author of parent message if they are not subscribed to this board
    // (ie board allows open posting)
    Message parent = message.getParent();
    if (parent != null && !isSubscribed(parent.getAuthor()))
      members.add(parent.getAuthor().getEmail());
    
    String[] emailArray = new String[members.size()];
    members.copyInto(emailArray);
    
/*
    String toString = "";
    for(int i=0; i<emailArray.length; i++) {
      toString += emailArray[i] + ", ";
    }
    if (toString.length() != 0)
      toString = toString.substring(0, toString.length() - 2);
 */
    
    String toString = getEmailAddress(message);
    String replyTo = toString;
    String body = message.getBody();
    body += "\n\n-----------------------------------------------------------------------\n";
    body += "From the " + getDisplayname() + " board:\n";
    body += "To unsubscribe, see the list of members or other messages, go to:\n";
    body += getBoardURL() + "\n\n";
    body += "This message";
    if (message.hasAttachments())
      body += " (has "+message.getAttachmentCount()+" attachments)";
    body += ":\n";
    body += getMessageURL(message) + "\n";
    
    try {
      Email.sendToList(
      getDatabase(),
      message.getAuthor().getEmail(),   // From
      emailArray,                       // To list
      toString,                         // apparently to
      replyTo,                          // reply to
      "["+getName()+"] " + message.getSubject(),    // subject
      body);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  
  /****************************************
   * Messages and Threads (stored as Trees)
   ****************************************/
  
  private CachedSelection roots = null;
  
  // Enumeration of Messages
  private Enumeration threadsRoots() {
    if (roots == null)
      roots = getBoardsDatabaseTables().getMessageTable().
      cachedBoardRoots(this);
    return roots.objects();
  }
  
  // ordered list of thread roots
  private Vector threadTrees = null;
  
  // hashtable of thread roots
  private Hashtable threadsByRoot = null;
  
  private void computeThreads() {
    threadTrees = new Vector();
    threadsByRoot = new Hashtable();
    Enumeration roots = threadsRoots();
    while (roots.hasMoreElements()) {
      Message root = (Message)roots.nextElement();
      addThread(root);
    }
  }
  
  void addThread(Message root) {
    addThread(root, false);
  }

  void addThread(Message root, boolean atStart) {
    if (atStart) 
      addThread(root,0);
    else 
      addThread(root,-1);
  }
  
  void addThread(Message root, int index) {
    if (threadTrees == null)  // Don't bother is we haven't built the trees
      return;                 // (They will be built when we try to access them)
    ChildrenDrivenMutableTree tree = new ChildrenDrivenMutableTree(root);
    if (index != -1)
      threadTrees.insertElementAt(tree, index);
    else
      threadTrees.addElement(tree);
    threadsByRoot.put(root, tree);
    computeMessages();
  }
  
  void addToParent(Message mess, Message parent) {
    if (threadTrees == null)  // Don't bother if we haven't built the trees
      return;                 // (They will be built when we try to access them)
    DefaultMutableTreeNode messNode = new DefaultMutableTreeNode(mess);
    DefaultMutableTreeNode parentNode = getTreeNode(parent);
    if (parentNode == null)     // Shouldn't happen
      throw new MessageNotInBoardException("Message "+parent+
      " is not in this board ("+this+")");
    parentNode.add(messNode);
    computeMessages();
  }
  
  void removeAndSquash(Message message) {
    if (threadTrees == null)  // Don't bother if we haven't built the trees
      return;                 // (They will be built when we try to access them)
    
    // adjust caches
    DefaultMutableTreeNode parent = null;
    int treeIndex = 0;
    if (message.getParent() != null) {
      parent = (DefaultMutableTreeNode)getTreeNode(message.getParent());
    } else {
      treeIndex = threadTrees.indexOf(threadWithRoot(message));
    }
    // set the children so they have no parent, or their grandparent
    DefaultMutableTreeNode node = getTreeNode(message);

    if (node == null)   // If this message isn't in the tree, because 
      return;         // it is pending authorisation for example
    
    Enumeration children = node.children();
    while (children.hasMoreElements()) {
      DefaultMutableTreeNode child = (DefaultMutableTreeNode)children.nextElement();
      if (parent == null) {
        child.setParent(null);
        addThread((Message)child.getUserObject(),treeIndex);
      } else {
        parent.add(child);
      }
    }
    node.removeFromParent();
    threadTrees.remove(threadWithRoot(message));
    threadsByRoot.remove(message);
    computeMessages();
  }
    
  private DefaultMutableTreeNode getTreeNode(Message m) {
    DefaultMutableTreeNode res = null;
    for(int i=0; i < threadTrees.size(); i++) {
      ChildrenDrivenMutableTree thread =
      (ChildrenDrivenMutableTree)threadTrees.elementAt(i);
      if ((res = thread.getTreeNodeFor(m)) != null)
        return res;
    }
    return null;
  }
  
  public ChildrenDrivenMutableTree threadWithRoot(Message m) {
    if (threadsByRoot == null)
      computeThreads();
    return (ChildrenDrivenMutableTree)threadsByRoot.get(m);
  }
  
  SoftReference messages = null;
  
  // Enumeration of DefaultMutableTreeNodes
  private void computeMessages() {
    if (threadsByRoot == null)
      computeThreads();
    
    Vector realMessages = new Vector();
    for (int i=0; i < threadTrees.size(); i++) {
      ChildrenDrivenMutableTree thread =
      (ChildrenDrivenMutableTree)threadTrees.elementAt(i);
      Enumeration e = thread.preorderEnumeration();
      //      Enumeration e = thread.breadthFirstEnumeration();
      while (e.hasMoreElements())
        realMessages.addElement(e.nextElement());
    }
    
    messages = new SoftReference(realMessages);
  }
  
  
  
  public Enumeration getMessages() {
    if (messages == null || messages.get() == null)
      computeMessages();
    Vector realMessages = (Vector)messages.get();
    return (realMessages == null) ? null : realMessages.elements();
  }
  
  private CachedCount messageCount = null;
  
  public int getMessageCount() {
    if (messageCount == null)
      messageCount =
      getBoardsDatabaseTables().getMessageTable().cachedMessageCount(this);
    return messageCount.count();
  }
  
  /**
   * Retrieve the firt message Id.
   * <p>
   * SQL Issues here.
   * </p>
   * <p>
   * Originally Myles had:
   * <pre>
   * SELECT MIN("id"), "id" FROM "message" 
   * WHERE "board"="+getTroid().intValue()
   * </pre>
   * this was presumably a hack to work around a Postgresql 
   * feature that min(id) is dependant upon id.
   * </p>
   * <p>
   * As of Postgresql 7.1 this issue has been cleared up and the 
   * above syntax is wrong as it should now be:
   * <pre>
   * SELECT MIN("id"), "id" FROM "message" 
   * WHERE "board"="+getTroid().intValue() GROUP BY "id"
   * </pre>
   * 
   * </p>
   * <p>
   * But now we can use the original sensible syntax:
   * <pre>
   * SELECT MIN("id") FROM "message" 
   * WHERE "board"="+getTroid().intValue()
   * </pre>
   * 
   * </p>
   *
   * 
   * @return the min message table id
   * @throws SQLException if anything goes wrong at the SQL level
   */
  public int getFirstMessageId() throws SQLException {
    int result = -1;
    ResultSet rs = getDatabase().sqlQuery("SELECT MIN(\"id\") FROM \"message\" WHERE \"board\"="+getTroid().intValue());
    if(rs.next())
      result = rs.getInt(1);
    rs.close();
    return result;
  }
  
  /**
   * Retrieve the last message Id.
   * 
   * @see #getFirstMessage
   * @return the max message table id
   * @throws SQLException if anything goes wrong at the SQL level
   */
  public int getLastMessageId() throws SQLException {
    int result = -1;
    ResultSet rs = getDatabase().sqlQuery("SELECT MAX(\"id\") FROM \"message\" WHERE \"board\"="+getTroid().intValue());
    if(rs.next())
      result = rs.getInt(1);
    rs.close();
    return result;
  }
  
  /********************
   * Management stuff
   ********************/
  
  /* Pending Messages */
  
  private CachedCount pendingMessageCount = null;
  private CachedSelection pendingMessages = null;
  
  public boolean hasPendingMessages() {
    return getPendingMessageCount() > 0;
  }
  
  public int getPendingMessageCount() {
    if (pendingMessageCount == null)
      pendingMessageCount =
      getBoardsDatabaseTables().getMessageTable().
      cachedPendingMessageCount(this);
    return pendingMessageCount.count();
  }
  
  public Enumeration getPendingMessages() {
    if (pendingMessages == null)
      pendingMessages =
      getBoardsDatabaseTables().getMessageTable().
      cachedPendingMessages(this);
    return pendingMessages.objects();
  }
  
  /* Pending Subscriptions */
  
  private CachedCount pendingSubscriptionCount = null;
  private CachedSelection pendingSubscriptions = null;
  
  public boolean hasPendingSubscriptions() {
    return getPendingSubscriptionCount() > 0;
  }
  
  public int getPendingSubscriptionCount() {
    if (pendingSubscriptionCount == null)
      pendingSubscriptionCount =
      getBoardsDatabaseTables().getSubscriptionTable().
      cachedPendingSubscriptionCount(this);
    return pendingSubscriptionCount.count();
  }
  
  public Enumeration getPendingSubscriptions() {
    if (pendingSubscriptions == null)
      pendingSubscriptions =
      getBoardsDatabaseTables().getSubscriptionTable().
      cachedPendingSubscriptions(this);
    return pendingSubscriptions.objects();
  }
  
  
  /********************************
   * Some board constants
   ********************************/
  
  public String getEmailAddress() throws SettingNotFoundException {
    return getName() + "@" + getBoardTable().getBoardsEmailDomain();
  }
  
  public String getEmailAddress(Message m) throws SettingNotFoundException {
    return m.troid() + "." + getName() + "@" +
    getBoardTable().getBoardsEmailDomain();
  }
  
  public String getBoardURL() throws SettingNotFoundException {
    return getBoardTable().getBoardsSystemURL() + "/" +
    getBoardTable().getLogicalDatabase() + "/board/" +
    troid()+"/Board";
  }
  
  public String getStylesheetURL() throws SettingNotFoundException {
    return getBoardTable().getBoardsStylesheetURL();
  }
  
  public String getMessageURL(Message m) throws SettingNotFoundException {
    return getBoardTable().getBoardsSystemURL() + "/" +
    getBoardTable().getLogicalDatabase() + "/message/" +
    m.troid()+"/Message";
  }
  
  public String templatePath(String templateName) {
    return getBoardTable().getBoardsEmailTemplates() + File.separator +
    templateName + ".wm";
  }
  
}
