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
 *     Mylesc Chippendale <mylesc@paneris.org>
 *     http://paneris.org/
 *     29 Stanley Road, Oxford, OX4 1QY, UK
 */


package org.paneris.melati.boards.model;

import java.sql.ResultSet;

import org.melati.poem.Database;
import org.melati.poem.DefinitionSource;
import org.melati.poem.PoemException;
import org.melati.poem.PoemTypeFactory;
import org.paneris.melati.boards.model.generated.SettingTableBase;


/**
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>SettingTable</code> object.
 *
 * 
 * <table> 
 * <tr><th colspan='3'>
 * Field summary for SQL table <code>Setting</code>
 * </th></tr>
 * <tr><th>Name</th><th>Type</th><th>Description</th></tr>
 * </table> 
 * 
 * see  org.melati.poem.prepro.TableDef#generateTableJava 
 */
public class SettingTable<T extends Setting> extends SettingTableBase<Setting> {

 /**
  * Constructor.
  * 
  * see org.melati.poem.prepro.TableDef#generateTableJava 
  * @param database          the POEM database we are using
  * @param name              the name of this <code>Table</code>
  * @param definitionSource  which definition is being used
  * @throws PoemException    if anything goes wrong
  */
  public SettingTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  // programmer's domain-specific code here
  

  String[] names = 
     {"SMTPServer",         // FIXME should be Email.SMTPSERVER,
      "BoardsEmailDomain",
      "BoardsSystemURL",
      "BoardsEmailTemplates",
      "BoardsAttachmentsPath",
      "BoardsAttachmentsURL",
      "BoardsStylesheetURL",
      "LogicalDatabase"};

    // At least two of these need to be set by hand, 
    // the other defaults need to be reviewed for each installation
  String[] values = 
   {"SMTP server",
    "", // Boards email domain
    "/BoardAdmin",
    "/dist/MelatiBoards/src/org/paneris/melati/boards/emailtemplates",
    "/dist/MelatiBoards/attachments",
    "/attachments/",
    "/css/boards.css", 
    ""}; //Logical database

  String[] displaynames = 
   {"SMTP server",
    "Boards email domain",
    "Boards system URL",
    "Boards email templates",
    "Boards attachments Path",
    "Boards attachments URL",
    "Boards stylesheet URL",
    "Logical database"};

  String[] descriptions = 
   {"The SMTP server for outgoing mail",
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
    "A URL to a stylesheet for all board pages",
    "The name of the database (note this must agree with " +
    "the entry in org.melati.LogicalDatabase.properties " +
    "(e.g. testdb)"
   };

 /**
  * Ensure that default settings are present.
  * 
  */
  public synchronized void unifyWithDB(ResultSet colDescs, String troidColumnName)
      throws PoemException {

    super.unifyWithDB(colDescs, troidColumnName);

    for (int i = 0; i < names.length; i++) {
       ensure(names[i], PoemTypeFactory.STRING, values[i],
              displaynames[i], descriptions[i]);
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

