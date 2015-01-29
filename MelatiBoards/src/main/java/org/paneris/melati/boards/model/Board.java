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
import org.melati.poem.ValidationPoemException;
import org.melati.poem.util.EnumUtils;
import org.melati.poem.util.MappedEnumeration;
import org.melati.util.ChildrenDrivenMutableTree;
import org.melati.util.Email;
import org.paneris.melati.boards.model.generated.BoardBase;

/**
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>Persistent</code> <code>Board</code> object.
 * 
 * <p> 
 * Description: 
 *   A board for messages. 
 * </p>
 * 
 * <table> 
 * <tr><th colspan='3'>
 * Field summary for SQL table <code>Board</code>
 * </th></tr>
 * <tr><th>Name</th><th>Type</th><th>Description</th></tr>
 * <tr><td> id </td><td> Integer </td><td> &nbsp; </td></tr> 
 * <tr><td> type </td><td> BoardType </td><td> The type of this messageboard 
 * </td></tr> 
 * <tr><td> name </td><td> String </td><td> A code-name for this board 
 * </td></tr> 
 * <tr><td> displayname </td><td> String </td><td> A user-friendly name of 
 * this board </td></tr> 
 * <tr><td> purpose </td><td> String </td><td> The purpose of this message 
 * board </td></tr> 
 * <tr><td> archived </td><td> Boolean </td><td> If a board is archived then 
 * it cannot be viewed and is not displayed on lists by default </td></tr> 
 * <tr><td> opensubscription </td><td> Boolean </td><td> With open 
 * subscription anyone can subscribe to the board. Otherwise a board manager 
 * must subscribe members </td></tr> 
 * <tr><td> moderatedsubscription </td><td> Boolean </td><td> With moderated 
 * subscription the manager must approve all requests to be subscribed 
 * </td></tr> 
 * <tr><td> openposting </td><td> Boolean </td><td> With open posting anyone 
 * with a user account can post a message to this list. Otherwise, only 
 * members can post </td></tr> 
 * <tr><td> moderatedposting </td><td> Boolean </td><td> With moderated 
 * posting all messages must be approved by a manager </td></tr> 
 * <tr><td> openmessageviewing </td><td> Boolean </td><td> With open message 
 * viewing anyone can view messages in a board. Otherwise, only members can 
 * see messages </td></tr> 
 * <tr><td> openmemberlist </td><td> Boolean </td><td> With open member list 
 * anyone can see the members of the list. Otherwise, only members can see 
 * who else is subscribed </td></tr> 
 * <tr><td> attachmentsallowed </td><td> Boolean </td><td> Can attachments be 
 * sent with messages. If not, attachments are ignored </td></tr> 
 * <tr><td> anonymousposting </td><td> Boolean </td><td> Can people without 
 * user accounts post to this messageboard. If so, a user account is created 
 * for them when they post. </td></tr> 
 * <tr><td> attachmentspath </td><td> String </td><td> A path to the 
 * directory containing attachments for this board </td></tr> 
 * <tr><td> attachmentsurl </td><td> String </td><td> A URL to the directory 
 * containing attachments for this board </td></tr> 
 * </table> 
 * 
 * see org.melati.poem.prepro.TableDef#generatePersistentJava 
 */
public class Board extends BoardBase {

 /**
  * Constructor 
  * for a <code>Persistent</code> <code>Board</code> object.
  * <p>
  * Description: 
  *   A board for messages. 
  * </p>
  * 
  * see org.melati.poem.prepro.TableDef#generatePersistentJava 
  */
  public Board() { 
    super();
  }

  // programmer's domain-specific code here

  public static String SMTPSERVER = "SMTPServer";

  /*****************************************************************************
   * Users levels - Admin, Manager, Member
   ****************************************************************************/

  public boolean isAdmin(User user) {
    return ((AccessToken) user).givesCapability(getDatabase()
        .administerCapability());
  }

  /**
   * @return whether the user is a manager of this board
   */
  public boolean isManager(User user) {
    return getBoardsDatabaseTables().getSubscriptionTable().isManager(user,
        this);
  }

  /**
   * @return whether the user is a member of this board
   */
  public boolean isMember(User user) {
    return getBoardsDatabaseTables().getSubscriptionTable()
        .isMember(user, this);
  }

  /**
   * @return whether the user is a banned from this board
   */
  public boolean isBanned(User user) {
    Subscription subscription = getUserSubscription(user);
    if (subscription != null
        && subscription.getStatus() == getBoardsDatabaseTables()
            .getMembershipStatusTable().getBanned())
      return true;
    return false;
  }

  /**
   * Ban a user from posting to this board.
   */
  public void ban(User user) {
    Subscription subscription = getUserSubscription(user);
    subscription.setStatus(getBoardsDatabaseTables().getMembershipStatusTable()
        .getBanned());
  }

  /*****************************************************************************
   * List users
   ****************************************************************************/

  private CachedCount managerCount = null;

  /**
   * @return the number of managers
   */
  public int getManagerCount() {
    if (managerCount == null)
      managerCount = getBoardsDatabaseTables().getSubscriptionTable()
          .cachedManagerCount(this);
    return managerCount.count();
  }

  private CachedSelection managersubs = null;

  /**
   * @return the Subscriptions of the managers
   */
  public Enumeration getManagersSubscriptions() {
    if (managersubs == null)
      managersubs = getBoardsDatabaseTables().getSubscriptionTable()
          .cachedManagers(this);
    return managersubs.objects();
  }

  /**
   * @return the Managers of this board
   */
  public Enumeration getManagers() {
    return new MappedEnumeration(getManagersSubscriptions()) {
      public Object mapped(Object subscription) {
        return ((Subscription) subscription).getUser();
      }
    };
  }

  private CachedCount memberCount = null;

  public int getMemberCount() {
    if (memberCount == null)
      memberCount = getBoardsDatabaseTables().getSubscriptionTable()
          .cachedMemberCount(this);
    return memberCount.count();
  }

  private CachedSelection membersubs = null;

  public Enumeration getMembersSubscriptions() {
    if (membersubs == null)
      membersubs = getBoardsDatabaseTables().getSubscriptionTable()
          .cachedMembers(this);
    return membersubs.objects();
  }

  public Enumeration getMembers() {
    return new MappedEnumeration(getMembersSubscriptions()) {
      public Object mapped(Object subscription) {
        return ((Subscription) subscription).getUser();
      }
    };
  }

  public Enumeration getNormalDistributionList() {
    return getBoardsDatabaseTables().getSubscriptionTable()
        .getNormalDistributionList(this);
  }

  public Enumeration getDigestDistributionList() {
    return getBoardsDatabaseTables().getSubscriptionTable()
        .getDigestDistributionList(this);
  }

  /*****************************************************************************
   * Actions permitted to users of this board
   ****************************************************************************/

  public boolean canSubscribe(User user) {
    if (isBanned(user))
      return false;
    return getOpensubscription().booleanValue() || isManager(user)
        || isAdmin(user);
  }

  // banned people can't unsubscribe
  public boolean canUnSubscribe(User user) {
    if (isBanned(user))
      return false;
    return true;
  }

  public boolean canPost(User user) {
    if (isBanned(user))
      return false;
    return getOpenposting().booleanValue() || isMember(user) || isAdmin(user);
  }

  public boolean canViewMessages(User user) {
    if (isBanned(user))
      return false;
    return getOpenmessageviewing().booleanValue() || isMember(user)
        || isAdmin(user);
  }

  public boolean canViewMembers(User user) {
    if (isBanned(user))
      return false;
    return getOpenmemberlist().booleanValue() || isMember(user)
        || isAdmin(user);
  }

  public boolean canManage(User user) {
    return isManager(user) || isAdmin(user);
  }

  /*****************************************************************************
   * Creating/Updating a board
   ****************************************************************************/

  public void assertCanWrite(AccessToken token) throws AccessPoemException {
    if (token == AccessToken.root)
      return;
    try {
      if (!canManage((User) token))
        throw new AccessPoemException(token, new Capability("Manager"));
    } catch (ClassCastException e) {
      throw new AccessPoemException(token, new Capability("Manager"));
    }
  }
  public void assertCanCreate(AccessToken token) throws AccessPoemException {
    if (token == AccessToken.root)
      return;
    try {
      if (token == getDatabase().guestAccessToken())
        throw new AccessPoemException(token, new Capability("Logged In"));
    } catch (ClassCastException e) {
      throw new AccessPoemException(token, new Capability("Logged In"));
    }
  }

  /**
   * Method to allow a board to be created with a different manager than the
   * creating user.
   * @param manager the manager of the board
   */
  public final void makePersistent(User manager) {
    ((BoardTable) getTable()).create(this, manager);
  }

  /*****************************************************************************
   * Subscribing/Unsubscribing
   ****************************************************************************/

  public void subscribe(User user, MembershipStatus status, Boolean ismanager,
      Boolean approved) {
    // Unused variable until FIXME is addessed
    // Subscription sub =  
        getBoardsDatabaseTables().getSubscriptionTable().
            subscribe(user, this, status, ismanager, approved);

    // FIXME send getSubscriptionRequestReceivedNote() message to user
    //if (sub.getApproved_unsafe() == Boolean.FALSE) {
    //}
  }

  public void subscribe(User user) {
    subscribe(user, getBoardsDatabaseTables().getMembershipStatusTable()
        .getNormal(), Boolean.FALSE, new Boolean(
        !getModeratedsubscription_unsafe().booleanValue()));
  }

  public boolean isSubscribed(User user) {
    return getBoardsDatabaseTables().getSubscriptionTable()
        .isMember(user, this);
  }

  public void unsubscribe(User user) {
    getBoardsDatabaseTables().getSubscriptionTable().unsubscribe(user, this);
  }

  public Subscription getUserSubscription(User user) {
    return getBoardsDatabaseTables().getSubscriptionTable()
        .getUserSubscription(user, this);
  }

  /*****************************************************************************
   * Distribute
   ****************************************************************************/

  public void distribute(Message message) {

    String members = EnumUtils.concatenated(",", 
            new MappedEnumeration(getMembers()) {
      public Object mapped(Object member) {
        return Email.mailAddress(((User) member).getName(), 
                                 ((User) member).getEmail());
      }
    });

    // include author of parent message if they are not subscribed to this board
    // (ie board allows open posting)
    Message parent = message.getParent();
    if (parent != null && !isSubscribed(parent.getAuthor()))
      members += Email.mailAddress(parent.getAuthor().getEmail(), parent.getAuthor().getEmail());

    /*
     * String toString = ""; for(int i=0; i <emailArray.length; i++) { toString +=
     * emailArray[i] + ", "; } if (toString.length() != 0) toString =
     * toString.substring(0, toString.length() - 2);
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
      body += " (has " + message.getAttachmentCount() + " attachments)";
    body += ":\n";
    body += getMessageURL(message) + "\n";

    try {
      String smtpServer = getDatabase().getSettingTable().get(SMTPSERVER);
      File[] empty = {};
      Email.sendWithAttachments(smtpServer, 
                Email.mailAddress(message.getAuthor().getName(), // From
                                  message.getAuthor().getEmail()), 
                members, // To list
                replyTo, // reply to
                "[" + getName() + "] " + message.getSubject(), // subject
                body, 
                empty);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /*****************************************************************************
   * Messages and Threads (stored as Trees)
   ****************************************************************************/

  private CachedSelection roots = null;

  // Enumeration of Messages
  private Enumeration threadsRoots() {
    if (roots == null)
      roots = getBoardsDatabaseTables().getMessageTable()
          .cachedBoardRoots(this);
    return roots.objects();
  }

  // ordered list of thread roots
  private Vector threadTrees = null;

  // hashtable of thread roots
  private Hashtable threadsByRoot = null;

  private void computeThreads() {
    threadTrees = new Vector();
    threadsByRoot = new Hashtable();
    Enumeration rootsEnum = threadsRoots();
    while (rootsEnum.hasMoreElements()) {
      Message root = (Message)rootsEnum.nextElement();
      addThread(root);
    }
  }

  void addThread(Message root) {
    addThread(root, false);
  }

  void addThread(Message root, boolean atStart) {
    if (atStart)
      addThread(root, 0);
    else
      addThread(root, -1);
  }

  void addThread(Message root, int index) {
    if (threadTrees == null) // Don't bother is we haven't built the trees
      return; // (They will be built when we try to access them)
    ChildrenDrivenMutableTree tree = new ChildrenDrivenMutableTree(root);
    if (index != -1)
      threadTrees.insertElementAt(tree, index);
    else
      threadTrees.addElement(tree);
    threadsByRoot.put(root, tree);
    computeMessages();
  }

  void addToParent(Message mess, Message parent) {
    if (threadTrees == null) // Don't bother if we haven't built the trees
      return; // (They will be built when we try to access them)
    DefaultMutableTreeNode messNode = new DefaultMutableTreeNode(mess);
    DefaultMutableTreeNode parentNode = getTreeNode(parent);
    if (parentNode == null) // Shouldn't happen
      throw new MessageNotInBoardException("Message " + parent
          + " is not in this board (" + this + ")");
    parentNode.add(messNode);
    computeMessages();
  }

  void removeAndSquash(Message message) {
    if (threadTrees == null) // Don't bother if we haven't built the trees
      return; // (They will be built when we try to access them)

    // adjust caches
    DefaultMutableTreeNode parent = null;
    int treeIndex = 0;
    if (message.getParent() != null) {
      parent = getTreeNode(message.getParent());
    } else {
      treeIndex = threadTrees.indexOf(threadWithRoot(message));
    }
    // set the children so they have no parent, or their grandparent
    DefaultMutableTreeNode node = getTreeNode(message);

    if (node == null) // If this message isn't in the tree, because
      return; // it is pending authorisation for example

    Enumeration children = node.children();
    while (children.hasMoreElements()) {
      DefaultMutableTreeNode child = (DefaultMutableTreeNode) children
          .nextElement();
      if (parent == null) {
        child.setParent(null);
        addThread((Message) child.getUserObject(), treeIndex);
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
    for (int i = 0; i < threadTrees.size(); i++) {
      ChildrenDrivenMutableTree thread = (ChildrenDrivenMutableTree) threadTrees
          .elementAt(i);
      if ((res = thread.getTreeNodeFor(m)) != null)
        return res;
    }
    return null;
  }

  public ChildrenDrivenMutableTree threadWithRoot(Message m) {
    if (threadsByRoot == null)
      computeThreads();
    return (ChildrenDrivenMutableTree) threadsByRoot.get(m);
  }

  SoftReference messages = null;

  // Enumeration of DefaultMutableTreeNodes
  private void computeMessages() {
    if (threadsByRoot == null)
      computeThreads();

    Vector realMessages = new Vector();
    for (int i = 0; i < threadTrees.size(); i++) {
      ChildrenDrivenMutableTree thread = (ChildrenDrivenMutableTree) threadTrees
          .elementAt(i);
      Enumeration e = thread.preorderEnumeration();
      //      Enumeration e = thread.breadthFirstEnumeration();
      while (e.hasMoreElements())
        realMessages.addElement(e.nextElement());
    }

    messages = new SoftReference(realMessages);
  }

  /**
   * @return the messages
   */
  public Enumeration getMessages() {
    if (messages == null || messages.get() == null)
      computeMessages();
    Vector realMessages = (Vector) messages.get();
    return (realMessages == null) ? null : realMessages.elements();
  }

  private CachedCount messageCount = null;

  /**
   * @return the number of messages
   */
  public int getMessageCount() {
    if (messageCount == null)
      messageCount = getBoardsDatabaseTables().getMessageTable()
          .cachedMessageCount(this);
    return messageCount.count();
  }

  /**
   * Retrieve the firt message Id.
   * <p>
   * SQL Issues here.
   * </p>
   * <p>
   * Originally Myles had:
   * 
   * <pre>
   * 
   *  SELECT MIN(&quot;id&quot;), &quot;id&quot; FROM &quot;message&quot; 
   *  WHERE &quot;board&quot;=&quot;+getTroid().intValue()
   *  
   * </pre>
   * 
   * this was presumably a hack to work around a Postgresql feature that min(id)
   * is dependant upon id.
   * </p>
   * <p>
   * As of Postgresql 7.1 this issue has been cleared up and the above syntax is
   * wrong as it should now be:
   * 
   * <pre>
   * 
   *  SELECT MIN(&quot;id&quot;), &quot;id&quot; FROM &quot;message&quot; 
   *  WHERE &quot;board&quot;=&quot;+getTroid().intValue() GROUP BY &quot;id&quot;
   *  
   * </pre>
   * 
   * </p>
   * <p>
   * But now we can use the original sensible syntax:
   * 
   * <pre>
   * 
   *  SELECT MIN(&quot;id&quot;) FROM &quot;message&quot; 
   *  WHERE &quot;board&quot;=&quot;+getTroid().intValue()
   *  
   * </pre>
   * 
   * </p>
   * 
   * 
   * @return the min message table id
   * @throws SQLException
   *           if anything goes wrong at the SQL level
   */
  public int getFirstMessageId() throws SQLException {
    int result = -1;
    MessageTable messageTable = getBoardsDatabaseTables().getMessageTable();
    ResultSet rs = getDatabase().sqlQuery(
        "SELECT MIN(" + 
        messageTable.troidColumn().quotedName() 
        +") FROM "
        + messageTable.quotedName()
        + " WHERE  " 
        + messageTable.getBoardColumn().quotedName()
        + " = "
        + getTroid().intValue());
    if (rs.next())
      result = rs.getInt(1);
    rs.close();
    return result;
  }

  /**
   * Retrieve the last message Id.
   * 
   * @see #getFirstMessageId
   * @return the max message table id
   * @throws SQLException
   *           if anything goes wrong at the SQL level
   */
  public int getLastMessageId() throws SQLException {
    int result = -1;
    MessageTable messageTable = getBoardsDatabaseTables().getMessageTable();
    ResultSet rs = getDatabase().sqlQuery(
      "SELECT MAX(" + 
        messageTable.troidColumn().quotedName() 
        +") FROM "
        + messageTable.quotedName()
        + " WHERE  " 
        + messageTable.getBoardColumn().quotedName()
        + " = "
        + getTroid().intValue());
    
    if (rs.next())
      result = rs.getInt(1);
    rs.close();
    return result;
  }

  public Message getLastMessage() {
    try {
      return (Message) getDatabase().getTable("message").getObject(
          getLastMessageId());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /*****************************************************************************
   * Management stuff
   ****************************************************************************/

  /* Pending Messages */

  private CachedCount pendingMessageCount = null;
  private CachedSelection pendingMessages = null;

  public boolean hasPendingMessages() {
    return getPendingMessageCount() > 0;
  }

  public int getPendingMessageCount() {
    if (pendingMessageCount == null)
      pendingMessageCount = getBoardsDatabaseTables().getMessageTable()
          .cachedPendingMessageCount(this);
    return pendingMessageCount.count();
  }

  public Enumeration getPendingMessages() {
    if (pendingMessages == null)
      pendingMessages = getBoardsDatabaseTables().getMessageTable()
          .cachedPendingMessages(this);
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
      pendingSubscriptionCount = getBoardsDatabaseTables()
          .getSubscriptionTable().cachedPendingSubscriptionCount(this);
    return pendingSubscriptionCount.count();
  }

  public Enumeration getPendingSubscriptions() {
    if (pendingSubscriptions == null)
      pendingSubscriptions = getBoardsDatabaseTables().getSubscriptionTable()
          .cachedPendingSubscriptions(this);
    return pendingSubscriptions.objects();
  }

  /*****************************************************************************
   * Some board constants
   ****************************************************************************/

  public String getEmailAddress() throws SettingNotFoundException {
    return getName() + "@" + getBoardTable().getBoardsEmailDomain();
  }

  public String getEmailAddress(Message m) throws SettingNotFoundException {
    return m.troid() + "." + getName() + "@"
        + getBoardTable().getBoardsEmailDomain();
  }

  public String getBoardURL() throws SettingNotFoundException {
    return getBoardTable().getBoardsSystemURL() + "/"
        + getBoardTable().getLogicalDatabase() + "/board/" + troid() + "/Board";
  }

  public String getStylesheetURL() throws SettingNotFoundException {
    return getBoardTable().getBoardsStylesheetURL();
  }

  public String getMessageURL(Message m) throws SettingNotFoundException {
    return getBoardTable().getBoardsSystemURL() + "/"
        + getBoardTable().getLogicalDatabase() + "/message/" + m.troid()
        + "/Message";
  }

  /**
   * @param templateName short name of template
   * @return full name of template
   */
  public String templatePath(String templateName) {
    return getBoardTable().getBoardsEmailTemplates() + File.separator
        + templateName + ".wm";
  }

  /**
   * MTAs such as qmail set email addresses all lowercase, so we have to.
   */
  public void setName(String name) throws AccessPoemException,
      ValidationPoemException {
    String lName = name.toLowerCase();
    super.setName(lName);
  }

}

