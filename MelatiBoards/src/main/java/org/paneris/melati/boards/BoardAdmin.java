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


package org.paneris.melati.boards;

import java.io.*;
import java.util.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.melati.*;
import org.melati.util.*;
import org.melati.servlet.*;
import org.melati.template.*;
import org.paneris.melati.boards.model.User;
import org.melati.poem.*;
import org.melati.servlet.InvalidUsageException;
import org.paneris.melati.boards.model.*;


public class BoardAdmin extends TemplateServlet {

  static private int MAX_HITS = 2000;
  static private int HITS_PER_PAGE = 20;

  protected Persistent create(final Table table, final TemplateContext context) {
    return table.create(
        new Initialiser() {
          public void init(Persistent object)
              throws AccessPoemException, ValidationPoemException {
            MelatiUtil.extractFields(context, object);
          }
        });
  }

  protected String boardTemplate(TemplateContext context, String name) {
    return "melati/boards/" + name;
  }

  /*****************************
   * System Actions
   *****************************/
  
  protected String loginTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    org.melati.poem.User token = (org.melati.poem.User)PoemThread.accessToken();
    if (token.isGuest())
      throw new AccessPoemException(token, new Capability("Logged In"));
    return boardTemplate(context, melati);
  }

  protected String typesTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    return boardTemplate(context, "Types");
  }

  protected String searchForBoardTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    return boardTemplate(context, "SearchForBoard");
  }

  /*******************
   * BoardType Actions
   *******************/
  
  protected String listBoardsTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    return boardTemplate(context, "ListBoards");
  }

  /***************
   * Board Actions
   ***************/
  
  /**
   * List a page of messages for this board, starting at a particular
   * message (as determined by the "start" parameter).
   */
  protected String boardTemplate(TemplateContext context, Melati melati)
      throws PoemException {

    // The start parameter is set in the main handler function
    int s = new Integer((String)context.get("start")).intValue();

    DumbPageEnumeration messages = new DumbPageEnumeration (
        ((Board)melati.getObject()).getMessages(),
        s, HITS_PER_PAGE, MAX_HITS);
    context.put("messages", messages);

    return boardTemplate(context, "Board");
  }

  protected String searchBoardTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    return boardTemplate(context, "SearchBoard");
  }

  protected String settingsTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    return boardTemplate(context, "Settings");
  }

  protected String settingsUpdateTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    MelatiUtil.extractFields(context, melati.getObject());
    return boardTemplate(context, "SettingsUpdate");
  }

  protected String settingsEditTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    return boardTemplate(context, "SettingsEdit");
  }

  protected String membersTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    return boardTemplate(context, "Members");
  }

  /**
   * Present a form for a user to enter a new message
   */
  protected String messageNewTemplate(TemplateContext context, Melati melati)
      throws PoemException {

    if (melati.getUser().isGuest())
      throw new AccessPoemException(melati.getUser(), new Capability("Logged In"));

    String parent = context.getForm("parent");
    if (parent != null && !parent.equals("")) {
      context.put("parent", ((BoardTable)melati.getTable()).getBoardsDatabaseTables().
                        getMessageTable().getObject(new Integer(parent)));
    }

    return boardTemplate(context, "MessageNew");
  }

  /*****************
   * Message Actions
   *****************/
  
  protected String messageTemplate(TemplateContext context, Melati melati,
                                   boolean withThread)
      throws PoemException {
    context.put("withThread", new Boolean(withThread));
    return boardTemplate(context, "Message");
  }

  /**
   * Create a new message
   * <p>
   * If we need approval from a manager, we send the user an email notifying
   * them. Otherwise, we distribute the message to the members of the board
   */
  protected String messageCreateTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    Message newMessage = (Message)create(((BoardTable)melati.getTable()).
                              getBoardsDatabaseTables().getMessageTable(), context);
    if ("".equals(newMessage.getSubject())) {
      newMessage.setSubject("(no subject)");
    }
    if (newMessage.getApproved().booleanValue() == true) {
      newMessage.distribute();
      return boardTemplate(context, "MessageCreate");
    }
    else {
      emailNotification(melati,
                        newMessage.getBoard(),
                        (org.paneris.melati.boards.model.User)melati.getUser(),
                        "MessageReceived");
      return boardTemplate(context, "MessageNeedsModerating");
    }
  }

  protected String pendingMessagesTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    return boardTemplate(context, "PendingMessages");
  }

  protected String approveMessagesTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    String[] messages = melati.getRequest().getParameterValues("message");
    for(int i=0; i < messages.length; i++) {
      String approve = context.getForm(messages[i]);
      if (approve != null && approve.equals("yes")) {
        Message message = (Message)
                            ((BoardsDatabaseTables)melati.getDatabase()).
                          getMessageTable().getObject(new Integer(messages[i]));
        message.approve();
      }
    }
    return boardTemplate(context, "ApproveMessages");
  }


  /*****************************
   * Subscriptions
   *****************************/
  
  /**
   * Process a user's request to be subscribed to a board.
   * <p>
   * If the user is already subscribed (or has asked to be subscribed)
   * we decline their request.
   * <p>
   * Otherwise, we look to see if their request needs to be approved by
   * a manager. If so, we let the user and the managers know by email,
   * otherwise we subscribe them to the board.
   */
  protected String subscribeTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    Board board = (Board)melati.getObject();
    User user = (User)melati.getUser();

    if (!board.canSubscribe(user)) {
      return boardTemplate(context, "SubscribeByManager");
    }
    else if (board.isMember(user)) {
      return boardTemplate(context, "SubscribedAlready");
    }
    else if ( ((BoardsDatabaseTables)melati.getDatabase()).getSubscriptionTable().
                getUserSubscription(user, board) != null) {
      return boardTemplate(context, "SubscriptionAlreadyPending");
    }
    else if (board.getModeratedsubscription().booleanValue() == true) {
      board.subscribe(user,
                      ((BoardsDatabaseTables)melati.getDatabase()).
                        getMembershipStatusTable().getNormal(),
                      Boolean.FALSE,             // not a manager
                      Boolean.FALSE);            // not approved
      emailNotification(melati,
                        board,
                        (org.paneris.melati.boards.model.User)melati.getUser(),
                        "SubscriptionRequestReceived");
      return boardTemplate(context, "SubscribeApprovalRequired");
    }
    else {
      board.subscribe(user);
      emailNotification(melati,
                        board,
                        (org.paneris.melati.boards.model.User)melati.getUser(),
                        "Subscribed");
      return boardTemplate(context, "Subscribe");
    }
  }

  protected String unsubscribeTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    ((Board)melati.getObject()).unsubscribe((User)melati.getUser());
    return boardTemplate(context, "Unsubscribe");
  }

  /**
   * Update the members subscriptions.
   * <p>
   * A manager can alter a member's subscription type, whether they
   * are a manager and can delete the user from the board.
   */
  protected String membersEditTemplate(TemplateContext context, Melati melati)
      throws PoemException {

    // Read in the new settings, if any. Otherwise, return the form
    String[] subscriptions = melati.getRequest().
                               getParameterValues("subscription");
    if (subscriptions == null || subscriptions.length == 0)
      return boardTemplate(context, "MembersEdit");

    Vector toDelete = new Vector();
    for(int i = 0; i < subscriptions.length; i++) {
      Subscription sub = 
        ((Board)melati.getObject()).getBoardsDatabaseTables().
          getSubscriptionTable().
            getSubscriptionObject(new Integer(subscriptions[i]));

      String delete = context.getForm("delete-" + subscriptions[i]);
      if ("true".equals(delete))
      {
        if (sub.getUser() != melati.getUser())
          toDelete.addElement(sub);
      }
      else
      {
        String manager = context.getForm("manager-" + subscriptions[i]);
        if (sub.getUser() == melati.getUser())
          manager = sub.getIsmanager().toString();
        String status = context.getForm("status-" + subscriptions[i]);
        sub.setIsmanager("true".equals(manager) ? Boolean.TRUE : Boolean.FALSE);
        sub.setStatusTroid(new Integer(status));
      }
    }

    for (int j = 0; j < toDelete.size(); j++)
      ((Subscription)toDelete.elementAt(j)).deleteAndCommit();

    return boardTemplate(context, "MembersEdit");
  }

  /**
   * A manager can subscribe users to a board. We notify the user and the
   * managers by email.
   */
  protected String subscribeOthersTemplate(TemplateContext context, Melati melati)
      throws PoemException {

    String[] others = melati.getRequest().getParameterValues("others");
    if (others == null || others.length == 0)
      return boardTemplate(context, "MembersEdit");

    Boolean manager = (context.getForm("manager") != null &&
                       context.getForm("manager").equals("true"))
                       ? Boolean.TRUE
                       : Boolean.FALSE;
    MembershipStatus normal =
      ((Board)melati.getObject()).getBoardsDatabaseTables().
        getMembershipStatusTable().getNormal();

    for(int i = 0; i < others.length; i++) {
      User newUser = (User)
        ((Board)melati.getObject()).getBoardsDatabaseTables().
          getUserTable().getUserObject(new Integer(others[i]));

      Board board = (Board)melati.getObject();
      board.subscribe(newUser, normal, manager, Boolean.TRUE);
      emailNotification(melati,
                        board,
                        newUser,
                        "Subscribed");
    }
    return boardTemplate(context, "MembersEdit");
  }

  protected String pendingSubscriptionsTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    return boardTemplate(context, "PendingSubscriptions");
  }

  /**
   * A manager can approve a user's request to be subscribed to a board
   * or can decline it.
   */
  protected String approveSubscriptionsTemplate(TemplateContext context, Melati melati)
      throws PoemException {

    String[] subscriptions = melati.getRequest().getParameterValues("subscription");

    for(int i=0; i < subscriptions.length; i++) {
      // Get this subscription
      Subscription subscription =
        (Subscription) ((BoardsDatabaseTables)melati.getDatabase()).
           getSubscriptionTable().getObject(new Integer(subscriptions[i]));
      String action = context.getForm(subscriptions[i]);

      if (action.equals("normal") || action.equals("manager")) {
        if (action.equals("manager"))
          subscription.setIsmanager(Boolean.TRUE);
        subscription.approve();
        emailNotification(melati,
                          subscription.getBoard(),
                          (org.paneris.melati.boards.model.User)melati.getUser(),
                          "SubscriptionRequestApproved");
      }
      else if (action.equals("remove")) {
        emailNotification(melati,
                          subscription.getBoard(),
                          (org.paneris.melati.boards.model.User)melati.getUser(),
                          "SubscriptionRequestDeclined");
        subscription.deleteAndCommit();
      }
    }
    return boardTemplate(context, "ApproveSubscriptions");
  }

  protected String subscriptionEditTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    return boardTemplate(context, "SubscriptionEdit");
  }

  protected String subscriptionUpdateTemplate(TemplateContext context, Melati melati)
      throws PoemException {
    MelatiUtil.extractFields(context, melati.getObject());
    return boardTemplate(context, "SubscriptionUpdate");
  }

  /***********************************
   * Send emails to users and managers
   ***********************************/
  
  public void emailNotification(Melati melati, Board board, User user,
                                String templateName) {

    try {
      String message = evalTemplate(melati, user, templateName, board);

      // Send email to user
      Email.send(board.getDatabase(),
                 "admin."+board.getEmailAddress(),       // From
                 user.getEmail(),      // To
                 "",                   // reply to
                 "["+board.getName()+"]: Admin message",
                 message);

      Vector managers = EnumUtils.vectorOf(
                          new MappedEnumeration(board.getManagers()) {
                            public Object mapped(Object manager) {
                              return ((User)manager).getEmail();
                            }
                          });
      String[] emailArray = new String[managers.size()];
      managers.copyInto(emailArray);

      // Send email to managers
      Email.sendToList(board.getDatabase(),
                       "admin."+board.getEmailAddress(),       // From
                       emailArray,           // To
                       user.getEmail(),     // Apparently to
                       "",                   // reply to
                       "["+board.getName()+"]: Admin message",
                       "The following message has been sent to " +
                       user.getEmail() + " (" + user.getName() + ") \n\n" +
                       message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String evalTemplate(Melati melati, User user, String template, Board board)
                                throws IOException, TemplateEngineException {

      // templateEngine comes from TemplateServlet
      MelatiWriter sw = templateEngine.getStringWriter(melati.getEncoding());
      Melati melati2 = new Melati(melati.getConfig(), sw);
      TemplateContext context = templateEngine.getTemplateContext(melati2);
      context.put("melati", melati2);
      context.put("board", board);
      context.put("user", user);
      templateEngine.expandTemplate(melati2.getWriter(),
                                    board.templatePath(template),
                                    context);
      return sw.asString();
  }

  /*****************************
   * Handler
   *****************************/

  protected String doTemplateRequest( 
                   Melati melati, TemplateContext context) 
                   throws Exception {

    context.put("utils",
        new BoardUtils(melati.getRequest().getServletPath(),
                       melati.getContext().logicalDatabase));

    Board board = null;
    if (melati.getTable() != null && melati.getObject() != null) {
      if (melati.getTable().getName().equals("board"))
        board = (Board)melati.getObject();
      else if (melati.getTable().getName().equals("subscription"))
        board = ((Subscription)melati.getObject()).getBoard();
      else if (melati.getTable().getName().equals("message"))
        board = ((Message)melati.getObject()).getBoard();
    }

    context.put("start",
      getBoardStart(board, melati.getRequest().getSession(false),
                             context.getForm("start")));

    if (melati.getTable() == null) {
      if (melati.getMethod().equals("Types"))
        return typesTemplate(context,melati);
      if (melati.getMethod().equals("SearchForBoard"))
        return searchForBoardTemplate(context,melati);
    }
    else if (melati.getTable().getName().equals("board") &&
                                         melati.getObject() != null) {
      if (melati.getMethod().equals("Board"))
        return boardTemplate(context,melati);
      if (melati.getMethod().equals("Login"))
        return loginTemplate(context,melati);
      if (melati.getMethod().equals("MessageNew"))
        return messageNewTemplate(context,melati);
      if (melati.getMethod().equals("MessageCreate"))
        return messageCreateTemplate(context,melati);
      if (melati.getMethod().equals("SearchBoard"))
        return searchBoardTemplate(context,melati);
      if (melati.getMethod().equals("Settings"))
        return settingsTemplate(context,melati);
      if (melati.getMethod().equals("SettingsEdit"))
        return settingsEditTemplate(context,melati);
      if (melati.getMethod().equals("SettingsUpdate"))
        return settingsUpdateTemplate(context,melati);
      if (melati.getMethod().equals("Members"))
        return membersTemplate(context,melati);
      if (melati.getMethod().equals("Subscribe"))
        return subscribeTemplate(context,melati);
      if (melati.getMethod().equals("Unsubscribe"))
        return unsubscribeTemplate(context,melati);
      if (melati.getMethod().equals("SubscribeOthers"))
        return subscribeOthersTemplate(context,melati);
      if (melati.getMethod().equals("MembersEdit"))
        return membersEditTemplate(context,melati);
      if (melati.getMethod().equals("PendingMessages"))
        return pendingMessagesTemplate(context,melati);
      if (melati.getMethod().equals("PendingSubscriptions"))
        return pendingSubscriptionsTemplate(context,melati);
      if (melati.getMethod().equals("ApproveMessages"))
        return approveMessagesTemplate(context,melati);
      if (melati.getMethod().equals("ApproveSubscriptions"))
        return approveSubscriptionsTemplate(context,melati);
    }
    else if (melati.getTable().getName().equals("message") &&
                                         melati.getObject() != null) {
      if (melati.getMethod().equals("Message"))
        return messageTemplate(context,melati, true);
      if (melati.getMethod().equals("MessageNoThread"))
        return messageTemplate(context,melati, false);
    }
    else if (melati.getTable().getName().equals("boardtype") &&
                                         melati.getObject() != null) {
      if (melati.getMethod().equals("ListBoards"))
        return listBoardsTemplate(context,melati);
    }
    else if (melati.getTable().getName().equals("subscription") &&
                                             melati.getObject() != null) {
      if (melati.getMethod().equals("SubscriptionEdit"))
        return subscriptionEditTemplate(context,melati);
      if (melati.getMethod().equals("SubscriptionUpdate"))
        return subscriptionUpdateTemplate(context,melati);
    }

    throw new InvalidUsageException(this, melati.getContext());
  }

  /**
   * Get the start page for a board from the <code>start</code>
   * value passed in, if not null, or from the user's session otherwise.
   * <p>
   * We also update the board's start value stored in the session, if
   * appropriate
   */
  private String getBoardStart(Board board, HttpSession session, String start) {
    if (board != null) {
      String key = "org.paneris.melati.boards."+board.troid();
      if (start == null)
        start = (String)session.getValue(key);
      else
        session.putValue(key, start);
    }
      
    return (start != null) ? start : "0";
  }
}

