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
import java.io.*;
import java.util.*;
import java.sql.Date;
import java.sql.Timestamp;
import javax.swing.tree.DefaultMutableTreeNode;
import org.melati.*;
import org.melati.poem.*;
import org.melati.util.*;

public class Board extends BoardBase {
  public Board() {}

  /***************************
   * Users levels - Admin, Manager, Member
   *                Checks, lists and counts
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
    return  getOpensubscription().booleanValue() ||
            isManager(user) ||
            isAdmin(user);
  }

  public boolean canPost(User user) {
    return getOpenposting().booleanValue() ||
           isMember(user) ||
           isAdmin(user);
  }

  public boolean canViewMessages(User user) {
    return getOpenmessageviewing().booleanValue() ||
           isMember(user) ||
           isAdmin(user);
  }

  public boolean canViewMembers(User user) {
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
  /***************************
   * Subscribing/Unsubscribing
   ***************************/

  public void subscribe(User user, MembershipStatus status,
                        Boolean ismanager, Boolean approved) {
    Subscription sub =
      getBoardsDatabaseTables().getSubscriptionTable().
        subscribe(user, this, status, ismanager, approved);

    if (sub.getApproved_unsafe() == Boolean.FALSE) {
       // FIXME
       // send getSubscriptionRequestReceivedNote() message to user
    }
  }

  public void subscribe(User user) {
    subscribe(user,
              getBoardsDatabaseTables().getMembershipStatusTable().
                getNormal(),
              Boolean.FALSE,
              new Boolean(!getModeratedsubscription_unsafe().booleanValue()));
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

    String[] emailArray = new String[members.size()];
    members.copyInto(emailArray);

    String toString = "";
    for(int i=0; i<emailArray.length; i++) {
      toString += emailArray[i] + ", ";
    }
    if (toString.length() != 0)
      toString = toString.substring(0, toString.length() - 2);

    String body = message.getBody();
    body += "\n\n-----------------------------------------------------------------------\n";
    body += "From the "+getDisplayname()+" board:\n";
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
          message.getAuthor().getEmail(),
          emailArray,
          toString,
          getEmailAddress(message),
          "["+getName()+"] " + message.getSubject(),
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

  private Vector threadTrees = null;
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
    if (threadTrees == null)  // Don't bother is we haven't built the trees
      return;                 // (They will be built when we try to access them)
    ChildrenDrivenMutableTree tree = new ChildrenDrivenMutableTree(root);
    if (atStart)
      threadTrees.insertElementAt(tree, 0);
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

  Vector messages = null;

  // Enumeration of DefaultMutableTreeNodes
  private void computeMessages() {
    if (threadsByRoot == null)
      computeThreads();

    messages = new Vector();
    for(int i=0; i < threadTrees.size(); i++)
    {
      ChildrenDrivenMutableTree thread =
        (ChildrenDrivenMutableTree)threadTrees.elementAt(i);
      Enumeration e = thread.preorderEnumeration();
//      Enumeration e = thread.breadthFirstEnumeration();
      while (e.hasMoreElements())
        messages.addElement(e.nextElement());
    }
  }

  public Enumeration getMessages() {
    if (messages == null)
      computeMessages();
    return messages.elements();
  }

  private CachedCount messageCount = null;
  
  public int getMessageCount() {
    if (messageCount == null)
      messageCount =
        getBoardsDatabaseTables().getMessageTable().cachedMessageCount(this);
    return messageCount.count();
  }

  /********************
   * Management stuff
   ********************/

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
   * Notes about the list for users
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

  public String getMessageURL(Message m) throws SettingNotFoundException {
    return getBoardTable().getBoardsSystemURL() + "/" +
           getBoardTable().getLogicalDatabase() + "/message/" +
           m.troid()+"/Message";
  }

  public String templatePath(String templateName) {
    return getBoardTable().getBoardsEmailTemplates() + File.separator +
             templateName + ".wm";
  }
/*
  public String evalTemplate(Melati melati, String template) {
    // construct a Melati with a StringWriter instead of a servlet
    // request and response
    TemplateEngine = melati.getTemplateEngine();
    MelatiWriter sw = TemplateEngine.getStringWriter(melati.getEncoding());
    Melati melati2 = new Melati(melati.getConfig(), sw);
    TemplateContext context = templateEngine.getTemplateContext(melati2);
    context.put("melati", melati2);
    templateEngine.expandTemplate(melati2.getWriter(),
                                  templatePath(template),
                                  context);
    return sw.asString();
  }

  public String getSubscribedNote(Melati melati) {
    return evalTemplate(melati, "SubscribedNote");
    return "You are now subscribed to the "+getDisplayname()+
           " board. Please post messages to "+getEmailAddress(); // FIXME
  }

  public String getSubscriptionRequestReceivedNote() {
    return "We have received your request to join the  "+getDisplayname()+
           " board. We will let you know if you have been accepted.";
  }

  public String getSubscriptionRequestApprovedNote() {
    return "We have received your request to join the  "+getDisplayname()+
           " board. We will let you know if you have been accepted.";
  }

  public String getSubscriptionRequestDeclinedNote() {
    String note = "Unfortunately, your request to join the  "+getDisplayname()+
                  " board has been declined.\nPlease contact one of the managers"+
                  " for more information. The managers are:\n";
    Enumeration e = getManagers();
    User manager = null;
    while(e.hasMoreElements()) {
      manager = (User)e.nextElement();
      note += "  " + manager.getName() + " : " + manager.getEmail() + "\n";
    }
    return note;
    // Delete the subscription
  }

  public String getUsageInstructionsNote() {
    return "You should visit the web site to do most stuff " +
           "or just reply to messages"; //FIXME
  }
*/
  public String getManageInstructionsNote() {
    return "You should visit the web site to do most stuff " +
           "or just reply to messages"; //FIXME
  }

  public String getThisOrganisationNote() {
    return "Welcome to Paneris!!!";
  }
  
  // method to allow a board to be created with a different manager than the
  // creating user
  public final void makePersistent(User manager) {
    ((BoardTable)getTable()).create(this, manager);
  }

}
