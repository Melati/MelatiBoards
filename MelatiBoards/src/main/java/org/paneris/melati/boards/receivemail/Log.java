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
package org.paneris.melati.boards.receivemail;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;



public class Log {

    private static PrintWriter target =
//         new PrintWriter(new OutputStreamWriter(System.err));
         new PrintWriter(System.err);
    private static DateFormat dateFormat =
      DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
    private String module = null;

    public Log(String module) {
      this.module = module;
    }

    public void setTarget(PrintWriter target) {
      Log.target = target;
      target.println("*** BEGIN: " + dateFormat.format(new Date())
                     + " : " + module + " ***");
    }

    public void setTarget(String path) {
      PrintWriter out;
      try {
         out = new PrintWriter(new FileWriter(path, true));
      } catch (Exception e) {
//         out = new PrintWriter(new OutputStreamWriter(System.err));
         out = new PrintWriter(System.err);
      }
      setTarget(out);
      out.flush();
    }

    public void write(String message) {
      try {
        target.println(dateFormat.format(new Date())
                       + "\t" + module + "\t" + message); 
        target.flush();
      }
      catch (java.lang.Exception e) {
         System.err.println("** COULD NOT WRITE LOG! SWITCHING TO STDERR **");
         System.err.println(dateFormat.format(new Date()) + "\t" + message);
         System.err.flush();
         setTarget(new PrintWriter(System.err));
      }
    }

    public void exception(Exception e) {
      write("EXCEPTION:");
      e.printStackTrace(target);
      target.flush();
    }

    public void debug(String s) {
      write("DEBUG: " + s);
    }

    public void error(String s) {
      write("ERROR: " + s);
    }

    public void warning(String s) {
      write("WARNING: " + s);
    }
}



