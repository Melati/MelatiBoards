package org.paneris.melati.boards.receivemail;



import java.io.*;

import java.text.*;

import java.util.*;



public class Log {



    static private PrintWriter target =

//         new PrintWriter(new OutputStreamWriter(System.err));

         new PrintWriter(System.err);

    static private DateFormat dateFormat =

      DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);

    private String module = null;



    public Log(String module) {

      this.module = module;

    }



    public void setTarget(PrintWriter target) {

      this.target = target;

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



