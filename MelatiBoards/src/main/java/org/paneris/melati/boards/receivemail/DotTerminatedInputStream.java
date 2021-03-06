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


package org.paneris.melati.boards.receivemail;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

/**
 * A stream comprising everything in an underlying stream up to the
 * first line containing only a fullstop.
 *
 * Once the <TT>DotTerminatedInputStream</TT> has reported
 * end-of-file, the underlying stream can be used again to read the
 * data from the dot-line up to its own end-of-file.
 */
public class DotTerminatedInputStream extends FilterInputStream {
  private static final int
    TEXT = 0, FIRSTCR = 1, FIRSTNL = 2, DOT = 3, SECONDCR = 4, TERMINATED = 5,
    POP = 6;

  private int state = FIRSTNL;
  private int state1 = -1;
  private int byte1 = -1;
  private byte[] pushedBack = new byte[4];
  private int pushedBackIn = 0;
  private int pushedBackOut = 0;

  /**
   * @param in          the underlying stream: must be a
   *                    <TT>PushbackInputStream</TT> because
   *                    it's theoretically possible to end up
   *                    with a character that has to be pushed
   *                    back
   */
  public DotTerminatedInputStream(PushbackInputStream in) {
    super(in);
  }

  private int superRead() throws IOException {
    if (byte1 == -1) return super.read();
    int it = byte1;
    byte1 = -1;
    return it;
  }

  private void pushBack(byte b) {
    if (pushedBackIn == pushedBack.length) {
      // in fact this doesn't happen
      byte[] pushedBack_ = new byte[pushedBack.length * 2];
      System.arraycopy(pushedBack, 0, pushedBack_, 0, pushedBack.length);
      pushedBack = pushedBack_;
    }

    pushedBack[pushedBackIn++] = b;
  }

 /**
  * Read a character.
  *
  * @return a character or -1 if terminated.
  */
  public synchronized int read() throws IOException {
    int b;
    for (;;) {
      switch (state) {
    case TERMINATED:
      pushedBack = null;
      return -1;

    case POP:
      if (pushedBackOut < pushedBackIn) {
        return pushedBack[pushedBackOut++];
      }
      pushedBackOut = 0;
      pushedBackIn = 0;
      state = state1;
      break;

    case TEXT:
      switch (b = superRead()) {
        case '\r': state = FIRSTCR; pushBack((byte)13); break;
        case '\n': state = FIRSTNL; pushBack((byte)10); break;
        case   -1: state = POP; state1 = TERMINATED; break;
        default:   return b;
      }
      break;

    case FIRSTCR:
      switch (b = superRead()) {
        case '\n': state = FIRSTNL; pushBack((byte)10); break;
        case '\r': state = POP; state1 = TEXT; byte1 = '\r'; break;
        case   -1: state = POP; state1 = TERMINATED; break;
        default:   state = POP; state1 = TEXT; byte1 = b; break;
      }
      break;

    case FIRSTNL:
      switch (b = superRead()) {
        case  '.': state = DOT; pushBack((byte)46); break;
        case '\n': state = POP; state1 = TEXT; byte1 = '\n'; break;
        case '\r': state = POP; state1 = TEXT; byte1 = '\r'; break;
        case   -1: state = POP; state1 = TERMINATED; break;
        default:   state = POP; state1 = TEXT; byte1 = b; break;
      }
      break;

    case DOT:
      switch (b = superRead()) {
        case '\n': state = TERMINATED; break;
        case '\r': state = SECONDCR; break;
        case   -1: state = POP; state1 = TERMINATED; break;
        default:   state = POP; state1 = TEXT; byte1 = b; break;
      }
      break;

    case SECONDCR:
      if ((b = superRead()) != '\n')
        ((PushbackInputStream)in).unread(b);
      state = TERMINATED;
      break;
    default:  break; // do nothing ?? 
      }
    }
  }

  /**
   *  Read some characters.
   */
  public synchronized int read(byte[] b, int off, int len) throws IOException {
    if (len <= 0) {
      return 0;
    }

    int c = read();
    if (c == -1) {
      return -1;
    }
    b[off] = (byte) c;

    int i = 1;
//    try {
      for (; i < len; i++) {
        c = read();
        if (c == -1) {
          break;
        }
        if (b != null) {
          b[off + i] = (byte) c;
        }
      }
//    } catch (IOException ee) {
//    }
    return i;
  }

  
  /**
   * Skip forward a number of characters.
   * 
   * @return the actual number of characters skipped 
   * @see java.io.InputStream#skip(long)
   */
  public synchronized long skip(long n) throws IOException {
    while (n > 0L && read() != -1) --n;
    return n;
  }
}
