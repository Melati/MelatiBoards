package org.paneris.melati.boards;

import org.webmacro.WebMacroException;
import org.paneris.melati.boards.model.*;
import org.melati.poem.*;
import org.melati.*;

public class BoardUtils {

  private String boardURL;
  private String logicalDatabase;

  public BoardUtils(String boardURL, String logicalDatabase) {
    this.boardURL = boardURL;
    this.logicalDatabase = logicalDatabase;
  }

  public String LoginURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
             "/Login";
  }

  public String ListTypesURL() {
    return boardURL + "/" + logicalDatabase + "/ListTypes";
  }

  public String SearchForBoardURL() {
    return boardURL + "/" + logicalDatabase + "/SearchForBoard";
  }

  public String ListBoardsURL(BoardType type) {
    return boardURL + "/" + logicalDatabase + "/boardtype/" + type.troid()+
           "/ListBoards";
  }

  public String CreateBoardURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/CreateBoard";
  }

  public String BoardURL(Board board, String start) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/Board?start="+start;
  }

  public String SubscribeURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/Subscribe";
  }

  public String UnsubscribeURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/Unsubscribe";
  }

  public String SubscriptionOptionsURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/SubscriptionOptions";
  }

  public String SearchBoardURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/SearchBoard";
  }

  public String NewMessageURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/NewMessage";
  }

  public String ListPendingMessagesURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/ListPendingMessages";
  }

  public String ApproveMessageURL(Message message) {
    return boardURL + "/" + logicalDatabase + "/message/" + message.troid()+
           "/ApproveMessage";
  }

  public String ApproveSubscriptionURL(Subscription sub) {
    return boardURL + "/" + logicalDatabase + "/subscription/" +
             sub.troid() + "/ApproveSubscription";
  }

  public String ListPendingSubscriptionsURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/ListPendingSubscriptions";
  }

  public String ManageURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/Manage";
  }

  public String ShowMessageURL(Message message) {
    return boardURL + "/" + logicalDatabase + "/message/" + message.troid()+
           "/ShowMessage";
  }

  public String CreateMessageURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/CreateMessage";
  }

  public String ReplyToURL(Message message) {
    return boardURL + "/" + logicalDatabase + "/message/" + message.troid()+
           "/ReplyTo";
  }

  public static int indent(int space, int depth) {
    return space*depth+1;
  }
}
