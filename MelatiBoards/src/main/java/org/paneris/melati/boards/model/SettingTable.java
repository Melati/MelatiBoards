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
import org.paneris.melati.boards.model.Setting;
import org.paneris.melati.boards.model.generated.*;
import java.util.*;
import java.sql.*;
import java.sql.Date;
import java.sql.Timestamp;
import org.melati.poem.*;
import org.melati.util.Email;

public class SettingTable extends SettingTableBase {

    String[] names = {"SMTPServer",         // FIXME should be Email.SMTPSERVER,
                    "BoardsEmailDomain",
                    "BoardsSystemURL",
                    "BoardsEmailTemplates",
                    "BoardsAttachmentsPath",
                    "BoardsAttachmentsURL",
                    "LogicalDatabase"};

  String[] displaynames = {"SMTP server",
                           "Boards email domain",
                           "Boards system URL",
                           "Boards email templates",
                           "Boards attachments Path",
                           "Boards attachments URL",
                           "Logical database"};

  String[] descriptions = {"The SMTP server for outgoing mail",
                           "The domain which receives mail for this database. " +
                           "Note that this must be the same as that defined " +
                           "in smtpServer.properties (or equivalent, if you " +
                           "set a different name when starting SMTPServerServlet) " +
                           "(e.g. boards.testapp.co.uk)",
                           "the URL to the BoardAdmin handler for this database " +
                           "(e.g. http://www.testapp.co.uk/testapp/org.paneris.melati.boards.BoardAdmin)",
                           "The directory containing the templates for sending " +
                           "out email. This is relative to WM's TemplatePath " +
                           "(e.g. testapp/boards/emailtemplates)",
                           "A directory which will contain one directory for " +
                           "each board (named after the board) in which to " +
                           "store attachments for this board " +
                           "(e.g. /usr/httpd/testapp/board_attachments)",
                           "A URL to the directory defined by BoardsAttachmentsPath " +
                           "(e.g. http://www.testapp.co.uk/board_attachments)",
                           "The name of the database (note this must agree with " +
                           "the entry in org.melati.LogicalDatabase.properties " +
                           "(e.g. testdb)"
                          };

  public SettingTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  public synchronized void unifyWithDB(ResultSet colDescs)
      throws SQLException, PoemException {

    super.unifyWithDB(colDescs);

    Setting blank = (Setting)newPersistent();
    blank.setUsereditable(Boolean.TRUE);
    blank.setTypefactory(PoemTypeFactory.STRING);
    blank.setNullable(Boolean.FALSE);
    blank.setSize(new Integer(-1));
    blank.setWidth(new Integer(40));
    blank.setHeight(new Integer(1));

    for (int i = 0; i < names.length; i++) {
      Setting _new = (Setting)blank.duplicatedFloating();
      _new.setName(names[i]);
      _new.setValue("");
      _new.setRawString("title", "");
      _new.setDisplayname(displaynames[i]);
      _new.setDescription(descriptions[i]);
      getNameColumn().ensure(_new);
    }

/*
    ensure("SMTPServer", PoemTypeFactory.STRING, "",
           "SMTP server",  "The SMTP server for outgoing mail",
                           "The domain which receives mail for this database. " +
                           "Note that this must be the same as that defined " +
                           "in smtpServer.properties (or equivalent, if you " +
                           "set a different name when starting SMTPServerServlet) " +
                           "(e.g. boards.testapp.co.uk)");
*/
  }
}
