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
import org.melati.poem.*;
import org.paneris.melati.boards.model.*;


public class BoardAdmin extends MelatiServlet {

  static private int MAX_HITS = 2000;
  static private int HITS_PER_PAGE = 20;

  protected Persistent create(final Table table, final WebContext context) {
    return table.create(
        new Initialiser() {
          public void init(Persistent object)
              throws AccessPoemException, ValidationPoemException {
            Melati.extractFields(context, object);
          }
        });
  }

  protected Template boardTemplate(WebContext context, String name)
      throws NotFoundException, InvalidTypeException {
        return getTemplate("melati/boards/" + name);
  }

  /*****************************
   * System Actions
   *****************************/
  
  protected Template loginTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {
    org.melati.poem.User token = (org.melati.poem.User)PoemThread.accessToken();
    if (token.isGuest())
      throw new AccessPoemException(token, new Capability("Logged In"));
    return listBoardTemplate(context, melati);
  }

  protected Template listTypesTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {
    return boardTemplate(context, "ListTypes.wm");
  }

  protected Template searchForBoardTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {
    return boardTemplate(context, "SearchForBoard.wm");
  }

  /*******************
   * BoardType Actions
   *******************/
  
  protected Template listBoardsTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {
    return boardTemplate(context, "ListBoards.wm");
  }

  /***************
   * Board Actions
   ***************/
  
  protected Template createBoardTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {
    create(melati.getTable(), context);
    // Create attachment, if necessary
    return boardTemplate(context, "CreateBoard.wm");
  }

  protected Template listBoardTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {

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

  protected Template subscribeTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {
    return boardTemplate(context, "Subscribe.wm");
  }

  protected Template unsubscribeTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {
    return boardTemplate(context, "Unsubscribe.wm");
  }

  protected Template subscriptionOptionsTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {
    return boardTemplate(context, "SubscriptionOptions.wm");
  }

  protected Template searchBoardTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {
    return boardTemplate(context, "SearchBoard.wm");
  }

  protected Template newMessageTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {

    if (melati.getUser().isGuest())
      throw new AccessPoemException(melati.getUser(), new Capability("Logged In"));

    String parent = context.getForm("parent");
    if (parent != null && !parent.equals("")) {
      context.put("parent", ((BoardTable)melati.getTable()).getBoardsDatabase().
                        getMessageTable().getObject(new Integer(parent)));
    }

    return boardTemplate(context, "NewMessage.wm");
  }

  protected Template listPendingMessagesTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {
    return boardTemplate(context, "ListPendingMessages.wm");
  }

  protected Template listPendingSubscriptionsTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {
    return boardTemplate(context, "ListPendingSubsciptions.wm");
  }

  protected Template manageTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {
    return boardTemplate(context, "Manage.wm");
  }

  /*****************
   * Message Actions
   *****************/
  
  protected Template showMessageTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {
    return boardTemplate(context, "ShowMessage.wm");
  }

  protected Template createMessageTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {
    Persistent newMessage = create(((BoardTable)melati.getTable()).
                              getBoardsDatabase().getMessageTable(), context);
    ((Message)newMessage).distribute();
    return boardTemplate(context, "CreateMessage.wm");
  }


  /*****************************
   * Approve
   *****************************/
  
  protected Template approveSubscriptionTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {
    return boardTemplate(context, "ApproveSubscription.wm");
  }

  protected Template approveMessageTemplate(WebContext context, Melati melati)
      throws NotFoundException, InvalidTypeException, PoemException {
    return boardTemplate(context, "ApproveMessage.wm");
  }



  /*****************************
   * Handler
   *****************************/
  
  protected Template handle(WebContext context, Melati melati)
      throws Exception {

    context.put("utils",
        new BoardUtils(context.getRequest().getServletPath(),
                       melati.getLogicalDatabaseName()));

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
