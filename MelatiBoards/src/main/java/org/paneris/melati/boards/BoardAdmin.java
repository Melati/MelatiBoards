package org.paneris.melati.boards;

import java.util.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.webmacro.*;
import org.webmacro.util.*;
import org.webmacro.servlet.*;
import org.webmacro.engine.*;
import org.webmacro.resource.*;
import org.webmacro.broker.*;
import org.melati.*;
import org.melati.util.*;
import org.melati.servlet.*;
import org.melati.template.*;
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

  protected String boardTemplate(TemplateContext context, String name)
      throws InvalidTypeException {
        return "melati/boards/" + name;
  }

  /*****************************
   * System Actions
   *****************************/
  
  protected String loginTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {
    org.melati.poem.User token = (org.melati.poem.User)PoemThread.accessToken();
    if (token.isGuest())
      throw new AccessPoemException(token, new Capability("Logged In"));
    return listBoardTemplate(context, melati);
  }

  protected String listTypesTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {
    return boardTemplate(context, "ListTypes.wm");
  }

  protected String searchForBoardTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {
    return boardTemplate(context, "SearchForBoard.wm");
  }

  /*******************
   * BoardType Actions
   *******************/
  
  protected String listBoardsTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {
    return boardTemplate(context, "ListBoards.wm");
  }

  /***************
   * Board Actions
   ***************/
  
  protected String createBoardTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {
    create(melati.getTable(), context);
    // Create attachment, if necessary
    return boardTemplate(context, "CreateBoard.wm");
  }

  protected String listBoardTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {

    // Find the start page
    int s = 0;
    String start = context.getForm("start");
    if (start != null && !start.equals(""))
      s = new Integer(start).intValue();

    DumbPageEnumeration messages = new DumbPageEnumeration (
        ((Board)melati.getObject()).getMessages(),
        s, HITS_PER_PAGE, MAX_HITS);
    context.put("messages", messages);

    return boardTemplate(context, "Board.wm");
  }

  protected String subscribeTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {
    return boardTemplate(context, "Subscribe.wm");
  }

  protected String unsubscribeTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {
    return boardTemplate(context, "Unsubscribe.wm");
  }

  protected String subscriptionOptionsTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {
    return boardTemplate(context, "SubscriptionOptions.wm");
  }

  protected String searchBoardTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {
    return boardTemplate(context, "SearchBoard.wm");
  }

  protected String newMessageTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {

    if (melati.getUser().isGuest())
      throw new AccessPoemException(melati.getUser(), new Capability("Logged In"));

    String parent = context.getForm("parent");
    if (parent != null && !parent.equals("")) {
      context.put("parent", ((BoardTable)melati.getTable()).getBoardsDatabase().
                        getMessageTable().getObject(new Integer(parent)));
    }

    return boardTemplate(context, "NewMessage.wm");
  }

  protected String listPendingMessagesTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {
    return boardTemplate(context, "ListPendingMessages.wm");
  }

  protected String listPendingSubscriptionsTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {
    return boardTemplate(context, "ListPendingSubsciptions.wm");
  }

  protected String manageTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {
    return boardTemplate(context, "Manage.wm");
  }

  /*****************
   * Message Actions
   *****************/
  
  protected String showMessageTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {
    return boardTemplate(context, "ShowMessage.wm");
  }

  protected String createMessageTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {
    Persistent newMessage = create(((BoardTable)melati.getTable()).
                              getBoardsDatabase().getMessageTable(), context);
    ((Message)newMessage).distribute();
    return boardTemplate(context, "CreateMessage.wm");
  }


  /*****************************
   * Approve
   *****************************/
  
  protected String approveSubscriptionTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {
    return boardTemplate(context, "ApproveSubscription.wm");
  }

  protected String approveMessageTemplate(TemplateContext context, Melati melati)
      throws InvalidTypeException, PoemException {
    return boardTemplate(context, "ApproveMessage.wm");
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

    String start = context.getForm("start");
    if (start != null)
      context.put("start",start);
    else
      context.put("start", "0");

    if (melati.getTable() == null) {
      if (melati.getMethod().equals("ListTypes"))
        return listTypesTemplate(context,melati);
      if (melati.getMethod().equals("SearchForBoard"))
        return searchForBoardTemplate(context,melati);
    }
    else if (melati.getTable().getName().equals("board") &&
                                         melati.getObject() != null) {
      if (melati.getMethod().equals("Board"))
        return listBoardTemplate(context,melati);
      if (melati.getMethod().equals("Login"))
        return loginTemplate(context,melati);
      if (melati.getMethod().equals("NewMessage"))
        return newMessageTemplate(context,melati);
      if (melati.getMethod().equals("CreateMessage"))
        return createMessageTemplate(context,melati);
      if (melati.getMethod().equals("SearchBoard"))
        return searchBoardTemplate(context,melati);
      if (melati.getMethod().equals("Subscribe"))
        return subscribeTemplate(context,melati);
      if (melati.getMethod().equals("Unsubscribe"))
        return unsubscribeTemplate(context,melati);
      if (melati.getMethod().equals("SubscriptionOptions"))
        return subscriptionOptionsTemplate(context,melati);
      if (melati.getMethod().equals("Manage"))
        return manageTemplate(context,melati);
      if (melati.getMethod().equals("ListPendingMessages"))
        return listPendingMessagesTemplate(context,melati);
      if (melati.getMethod().equals("ListPendingSubscriptions"))
        return listPendingSubscriptionsTemplate(context,melati);
      if (melati.getMethod().equals("CreateBoard"))
        return createBoardTemplate(context,melati);
    }
    else if (melati.getTable().getName().equals("message") &&
                                         melati.getObject() != null) {
      if (melati.getMethod().equals("ApproveMessage"))
        return approveMessageTemplate(context,melati);
      if (melati.getMethod().equals("ShowMessage"))
        return showMessageTemplate(context,melati);
    }
    else if (melati.getTable().getName().equals("boardtype") &&
                                         melati.getObject() != null) {
      if (melati.getMethod().equals("ListBoards"))
        return listBoardsTemplate(context,melati);
    }
    else if (melati.getTable().getName().equals("subscription") &&
                                             melati.getObject() != null) {
      if (melati.getMethod().equals("ApproveSubscription"))
        return approveSubscriptionTemplate(context,melati);
    }

    throw new InvalidUsageException(this, melati.getContext());
  }
}
