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

  public String TypesURL() {
    return boardURL + "/" + logicalDatabase + "/Types";
  }

  public String SearchForBoardURL() {
    return boardURL + "/" + logicalDatabase + "/SearchForBoard";
  }

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

  public String BoardURL(Board board, String start) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/Board?start="+start;
  }

  public String SearchBoardURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/SearchBoard";
  }

  public String MessageURL(Message message, String start) {
    return boardURL + "/" + logicalDatabase + "/message/" + message.troid()+
           "/Message?start="+start;
  }

  public String MessageNoThreadURL(Message message, String start) {
    return boardURL + "/" + logicalDatabase + "/message/" + message.troid()+
           "/MessageNoThread?start="+start;
  }

  public String MessageNewURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/MessageNew";
  }

  public String MessageCreateURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/MessageCreate";
  }

  public String SubscribeURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/Subscribe";
  }

  public String SubscriptionEditURL(Subscription sub) {
    return boardURL + "/" + logicalDatabase + "/subscription/" + sub.troid() +
           "/SubscriptionEdit";
  }

  public String SubscriptionUpdateURL(Subscription sub) {
    return boardURL + "/" + logicalDatabase + "/subscription/" + sub.troid() +
           "/SubscriptionUpdate";
  }

  public String UnsubscribeURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/Unsubscribe";
  }

  public String MembersURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/Members";
  }

  public String MembersEditURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/MembersEdit";
  }

  public String SubscribeOthersURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/SubscribeOthers";
  }

  public String PendingMessagesURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/PendingMessages";
  }

  public String PendingSubscriptionsURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/PendingSubscriptions";
  }

  public String ApproveMessagesURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
            "/ApproveMessages";
  }

  public String ApproveSubscriptionsURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
            "/ApproveSubscriptions";
  }

  public String SettingsURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/Settings";
  }

  public String SettingsUpdateURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/SettingsUpdate";
  }

  public String SettingsEditURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/SettingsEdit";
  }

  public String DeleteMessagesURL(Board board) {
    return boardURL + "/" + logicalDatabase + "/board/" + board.troid() +
           "/DeleteMessages";
  }

  public static int indent(int space, int depth) {
    return space*depth+1;
  }
}
