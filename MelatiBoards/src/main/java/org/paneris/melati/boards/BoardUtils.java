/*
 * $Source$
 * $Revision$
 *
 * Copyright (C) 2000 Myles Chippendale
 *
 * Part of a Melati application. This application is free software;
 * Permission is granted to copy, distribute and/or modify this
 * software under the same terms as those set out for Melati below.
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
 *     Mylesc Chippendale <mylesc At paneris.org>
 *     http://paneris.org/
 *     29 Stanley Road, Oxford, OX4 1QY, UK
 */


package org.paneris.melati.boards;

import org.paneris.melati.boards.model.Board;
import org.paneris.melati.boards.model.BoardType;
import org.paneris.melati.boards.model.Message;
import org.paneris.melati.boards.model.Subscription;

/**
 * Board Utilities. 
 */
public class BoardUtils {

  private String boardURL;
  private String logicalDatabase;

  /**
   * Constructor. 
   * 
   * @param boardURL the whole URL for the board, used in emails
   * @param logicalDatabase the name of the db
   */
  public BoardUtils(String boardURL, String logicalDatabase) {
    this.boardURL = boardURL;
    this.logicalDatabase = logicalDatabase;
  }

  /**
   * @return the login url
   */
  public String LoginURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
             "/Login";
  }

  /**
   * @return the Types url
   */
  public String TypesURL() {
    return boardURL + "/" + logicalDatabase + "/Types";
  }

  /**
   * @return the board search url
   */
  public String SearchForBoardURL() {
    return boardURL + "/" + logicalDatabase + "/SearchForBoard";
  }

  /**
   * @return the list boards of type url
   */
  public String ListBoardsURL(BoardType type) {
    return boardURL + "/" + logicalDatabase + "/boardtype/" + type.troid()+
           "/ListBoards";
  }


/*
  public String CreateBoardURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/CreateBoard";
  }
*/

  /**
   * @return the url for a board, starting at a message
   */
  public String BoardURL(Board board, String start) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/Board?start="+start;
  }

  /**
   * @return the search url for a board
   */
  public String SearchBoardURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/SearchBoard";
  }

  /**
   * @param message
   * @param start
   * @return the message url with bookmark
   */
  public String MessageURL(Message message, String start) {
    return boardURL + "/" + logicalDatabase + "/message/" + message.troid()+
           "/Message?start="+start;
  }

  /**
   * @return message url without thread
   */
  public String MessageNoThreadURL(Message message, String start) {
    return boardURL + "/" + logicalDatabase + "/message/" + message.troid()+
           "/MessageNoThread?start="+start;
  }

  /**
   * @return the new message url
   */
  public String MessageNewURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/MessageNew";
  }

  /**
   * @return the create message url
   */
  public String MessageCreateURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/MessageCreate";
  }

  /**
   * @return the subscribe url
   */
  public String SubscribeURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/Subscribe";
  }

  /**
   * @return the edit subscription url
   */
  public String SubscriptionEditURL(Subscription sub) {
    return boardURL + "/" + logicalDatabase + "/subscription/" + sub.troid() +
           "/SubscriptionEdit";
  }

  /**
   * @return the update subscription url
   */
  public String SubscriptionUpdateURL(Subscription sub) {
    return boardURL + "/" + logicalDatabase + "/subscription/" + sub.troid() +
           "/SubscriptionUpdate";
  }

  /**
   * @return the unsubscribe url
   */
  public String UnsubscribeURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/Unsubscribe";
  }

  /**
   * @return the members url
   */
  public String MembersURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/Members";
  }

  /**
   * @return the edit members url
   */
  public String MembersEditURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/MembersEdit";
  }

  /**
   * @return the subscribe other people url
   */
  public String SubscribeOthersURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/SubscribeOthers";
  }

  /**
   * @return the pending messages url
   */
  public String PendingMessagesURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/PendingMessages";
  }

  /**
   * @return the pending subscriptions url
   */
  public String PendingSubscriptionsURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/PendingSubscriptions";
  }

  /**
   * @return the approve message url
   */
  public String ApproveMessagesURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
            "/ApproveMessages";
  }

  /**
   * @return the approve subscriptions url
   */
  public String ApproveSubscriptionsURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
            "/ApproveSubscriptions";
  }

  /**
   * @return the settings url
   */
  public String SettingsURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/Settings";
  }

  /**
   * @return the update settings url
   */
  public String SettingsUpdateURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/SettingsUpdate";
  }

  /**
   * @return the edit settings url
   */
  public String SettingsEditURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/SettingsEdit";
  }

  /**
   * @return the delete messages url
   */
  public String DeleteMessagesURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/DeleteMessages";
  }

  /**
   * Utility to calculate message indentation.
   * 
   * @param space the number of spaces in a tab
   * @param depth the depth within a tree where the top is zero
   * @return a number (of spaces) to insert 
   */
  public static int indent(int space, int depth) {
    return space*depth+1;
  }
}
