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

import org.melati.poem.Database;
import org.melati.poem.DefinitionSource;
import org.melati.poem.PoemException;
import org.melati.poem.TableInfo;
import org.paneris.melati.boards.model.generated.BoardTypeTableBase;

/**
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>BoardTypeTable</code> object.
 * <p>
 * Description: 
 *   A type of a board. 
 * </p>
 *
 * 
 * <table> 
 * <tr><th colspan='3'>
 * Field summary for SQL table <code>BoardType</code>
 * </th></tr>
 * <tr><th>Name</th><th>Type</th><th>Description</th></tr>
 * <tr><td> id </td><td> Integer </td><td> &nbsp; </td></tr> 
 * <tr><td> type </td><td> String </td><td> The name of a type </td></tr> 
 * <tr><td> description </td><td> String </td><td> The description of the 
 * type </td></tr> 
 * </table> 
 * 
 * see  org.melati.poem.prepro.TableDef#generateTableJava 
 */
public class BoardTypeTable<T extends BoardType> extends BoardTypeTableBase<BoardType> {

 /**
  * Constructor.
  * 
  * see org.melati.poem.prepro.TableDef#generateTableJava 
  * @param database          the POEM database we are using
  * @param name              the name of this <code>Table</code>
  * @param definitionSource  which definition is being used
  * @throws PoemException    if anything goes wrong
  */
  public BoardTypeTable(
      Database database, String name,
      DefinitionSource definitionSource) throws PoemException {
    super(database, name, definitionSource);
  }

  // programmer's domain-specific code here

  public void postInitialise() {
    super.postInitialise();
    TableInfo info = getInfo();
    if (info.getDefaultcanwrite() == null)
      info.setDefaultcanwrite(getDatabase().administerCapability());
    if (info.getCancreate() == null)
      info.setCancreate(getDatabase().administerCapability());
  }
}

