/*
 * $Source$
 * $Revision$
 *
 * Copyright (C) 2003 Vasily Pozhidaev 
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
 *     Vasily Pozhidaev  <vasilyp At paneris.org>
 */
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Run HSQLDB.
 */
class RunHSQL implements Runnable {
  final Thread myThread;

  /**
   * Constructor. 
   */
  public RunHSQL() {
     myThread = Thread.currentThread();
   }
  /**
   * Run.
   */
   public void run() {
     String[] args = new String[2];
     args[0] = "-port";
     args[1] = "9124";
     org.hsqldb.Server.main(args);
   }
   /**
    * Stop.
    */
    public void stop() {
      myThread.interrupt();
    }

}

/**
 * Run HSQLDB from a servlet.
 */
public class hsqlRunnerServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private static RunHSQL hsql;
  /**
  * Init.
  *
  * @param conf the servlet configuration.
  */
  public void init(ServletConfig conf) throws ServletException {
    hsql = new RunHSQL();
    new Thread(hsql).start();
    super.init(conf);
  }

  
  /* (non-Javadoc)
    * @see javax.servlet.GenericServlet#destroy()
    */
   public void destroy() {
     hsql.stop();
     super.destroy();

   }

}
