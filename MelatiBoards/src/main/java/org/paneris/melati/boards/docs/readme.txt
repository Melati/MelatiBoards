       README
       ======

       Melati Board System
       -------------------

       (c) Paneris 2000
       Author: M Chippendale (mylesc@paneris.org)


0) Introduction
---------------

This file describes the Melati (http://www.melati.org) board system
developed by Paneris (http://www.paneris.org) and how to install and
set it up. It assumes you have installed Melati and have created your
own POEM database/melati application to which you wish to add the
board system.

The board system is a java package to be used with Melati. Melati is
a java servlet based framework for developing HTTP applications using
POEM. POEM provides an OO wrapper around (currently) an SQL database.
Together Melati and POEM provide a RAD toolkit for web sites.

This sytem implements a message board system. There are a number of
boards, arranged by type, to which users can subscribe and post messages.
Members of the list receive an email copy of each message posted to the
board, and can post messages to a board by email. There is also a web-based
interface to the boards which allows users to view, post and reply to
messages, view members and alter options. It can be viewed of as
a cross between a email mailing list and a web bulletin board where all
the data is stored in a database.

There are a number of actions which users may or may not be allowed to do
depending on the settings of the boards and whether they are subscribed
to a board as a manager. Currently the board options which are available
are:

Open Subscription       -  With open subscription anyone can subscribe to
                           the board. Otherwise a board manager must
                           subscribe members. Also, if the subscription is
                           open, anyone can subscribe anyone else to this
                           list.
Moderated Subscription  -  With moderated subscription the manager must
                           approved all requests to be subscribed
Open Posting            -  With open posting anyone can post a message to
                           this list. Otherwise, only members can post
Moderated Posting       -  With moderated posting all messages posted to
                           the board must be approved by a manager before
                           they are viewable on the web or sent out by email
Open Message Viewing    -  With open message viewing anyone can view
                           messages in a board. Otherwise, only members
Open Member List        -  With open member list anyone can see the members
                           of the list. Otherwise, only members can see
                           who else is subscribed
Attachments Allowed     -  Can attachments be sent with messages. If not,
                           attachments are ignored.



In the package:

docs            - docs about the system
emailtemplates  - templates for emails sent to users
model           - A POEM database and model implementing a message board
                  system
receivemail     - An SMTP server to receiving incoming email and post them
                  to the boards
- the main directory contains (Melati)Servlets and templates to provide a
  web-based interface to the boards


0a Requirements
---------------

Melati:
http://www.melati.org

Sun's JavaBeans Activation Framework implementation:
http://java.sun.com/beans/glasgow/jaf.html

Sun's JavaMail API 1.1.3:
http://java.sun.com/products/javamail/



1) Set up your database to include the boards tables
----------------------------------------------------

In your .dsd file, include the following line after the `package ...' line
but before the first table definition:

import org.paneris.melati.boards.model.boards.dsd;

This will include all tables used by the boards system and adds an `email'
field to the user table.

Because of this, if you wish to extend the User table then you will need
to `extend org.paneris.melati.boards.model.User' rather than the more usual
`extend org.melati.poem.User'. 


2) Set up your boards
---------------------

You need to enter some data in your database in order to create your system.
E.g. go to /testapp/org.melati.admin.Admin/testdb/Main and

  a) Create one or more `Board Type' entries
  b) Create one or more `Boards'. Note that when someone creates a board then
     the User as which they are logged-in will be subscribed to the list as
     a manager. The _guest_ user cannot be subscribed to a board


2a) Settings
-----------

The values that you must set up in the Settings table (they are all Strings):

SMTPServer            - the SMTP server for outgoing mail
    (e.g. testapp.co.uk) 
BoardsEmailDomain     - the domain which receives mail for this database.
                        Note that this must be the same as that defined
                        in smtpServer.properties (or equivalent, if you
                        set a different name when starting SMTPServerServlet)
    (e.g. boards.testapp.co.uk) 
BoardsSystemURL       - the URL to the BoardAdmin handler for this database
    (e.g. http://www.dgroup.co.uk/testapp/org.paneris.melati.boards.BoardAdmin)
BoardsEmailTemplates  - the directory containing the templates for sending
                        out email. This is relative to WM's TemplatePath
    (e.g. dgroup/boards/emailtemplates)
BoardsAttachmentsPath - a directory which will contain one directory for
                        each board (named after the board) in which to
                        store attachments for this board.
    (e.g. usr/httpd/ColinR/dgroup/board_attachments)
BoardsAttachmentsURL  - a URL to the directory defined by BoardsAttachmentsPath
    (e.g. http://www.dgroup.co.uk/board_attachments)
LogicalDatabase       - the name of the database (note this must agree with
                        the entry in org.melati.LogicalDatabase.properties
    (e.g. testdb)


3) Set up the outgoing email part of the system
-----------------------------------------------

You need to create an entry in the Settings table called `SMTPServer'. Set
it to the SMTP server you wish to use for outgoing mail. Add this through
the Melati admin system, e.g. go to:

/testapp/org.melati.admin.Admin/testdb/Main

where testapp is the zone your board-enable database and edit the Settings table.


4) Create the incoming email part of the system
-----------------------------------------------

a) You need to start a SMTP server which handles incoming mail. Since this
   server connects to Melati, it should be started inside the same JVM which
   will be running your Melati application. This is so that both applications
   use the same cache and therefore see the database contents as being the
   same.

   NB The SMTP server is launched from a servlet. You need to launch this
   servlet when you start the JVM. This is usually part of your servlet
   runner's setup. For instance, using Apache Jserv I need to customise my
   `testapp.properties' file which controls the servlet zone `testapp'
   (which is where I run my boards-enabled Melati application). I need to add
   the following lines:

   servlets.startup=org.paneris.melati.boards.receivemail.SMTPServerServlet
   servlet.org.paneris.melati.boards.receivemail.SMTPServerServlet.initArgs=port=1615
   servlet.org.paneris.melati.boards.receivemail.SMTPServerServlet.initArgs=properties=smtpServer.properties
   servlet.org.paneris.melati.boards.receivemail.SMTPServerServlet.initArgs=log=/usr/local/apache/log/messageboard-receivemail.log

    (these initArgs are actually the defaults, so I needn't really add them).

   Consult your Servlet Runner documentation for more information.

   Note that you should launch an SMTP server for each different JVM running
   a boards-enabled Melati application. With JServ all zones share the same
   JVM so only one zone needs to start an SMTP server. If your servlet runner starts
   multiple JVMs then you will need to start multiple SMTP servers (using
   different ports!).

b) For this code to work you must install mail.jar and activation.jar in your
   classpath. 

c) You now need to direct messages addressed to boards to the SMTP server
   started by the SMTPServerServlet. To quote from
   org.paneris.melati.boards.receivemail.SMTPServerServlet:

/**
 * An SMTP server to handle mail coming in to messageboards.
 *
 * A single server can handle mail for messageboards in several
 * separate databases; the name of the database to which it should
 * connect is determined from the recipient address given when the
 * mail arrives.  The program looks for a `properties' resource under
 * the name <TT>smtpServer.properties</TT> which specifies the mappings
 * from domain to database name, in the form
 *
 * <PRE>
 *   org.paneris.melati.boards.receivemail.database.DOMAIN=DATABASE
 * </PRE>
 *
 * for example
 *
 * <PRE>
 *   org.paneris.melati.boards.receivemail.database.messageboards.x.com=x
 * </PRE>
 *
 * (see also <TT>smtpServer.properties.example</TT>).
 *
 * Mail can be funelled into the server by having it listen on
 * port 25 in place of sendmail, but this would require your httpd to run
 * as root (in ordr to access ports below 1024), so is not advisable.  
 * The prefered mechanism is to configure the locally running
 * sendmail to forward appropriately addressed email to the server on
 * a different port.  The only way I can see to do the latter is to
 * edit <TT>sendmail.cf</TT> to define a new mailer.  Where it says
 *
 * <PRE>
 *   Msmtp,             P=[IPC], F=mDFMuX, S=11/31, R=21, E=\r\n, L=990,
 *                      T=DNS/RFC822/SMTP,
 *                      A=IPC $h
 * </PRE>
 *
 * insert also
 *
 * <PRE>
 *   Msmtp1615,         P=[IPC], F=mDFMuX, S=11/31, R=21, E=\r\n, L=990,
 *                      T=DNS/RFC822/SMTP,
 *                      A=IPC $h 1615
 * </PRE>
 *
 * Then use the standard <TT>mailertable</TT> feature to direct, for
 * example, all mail to <TT>messageboards.x.com</TT> on to
 * <TT>smtp1615:x.com</TT>.  You can do this very easily with
 * <TT>linuxconf</TT>'s <I>Special (domain) routing</I> menu: make an
 * entry with <I>Destination</I> set to <TT>messageboards.x.com</TT>,
 * <I>Forwarder</I> set to <TT>x.com</TT>, and <I>Mailer</I> set to
 * <TT>smtp1615</TT>.  But make sure to hack
 * <TT>/usr/lib/linuxconf/mailconf/smtpmailer.std.cf</TT> first, as
 * described above, to get it to define the special mailer in the
 * <TT>sendmail.cf</TT> it generates.  And of course for every mail
 * domain which sendmail is configured to forward to the messageboards
 * server, there should be an entry in <TT>smtpServer.properties</TT>
 * telling the latter which database to use.
 *
 * The program takes the following command line arguments:
 *
 * <TABLE>
 *   <TR>
 *     <TD><TT>--port <I>number</I></TT></TD>
 *     <TD>the port on which to listen, if not 1615</TD>
 *   </TR>
 *   <TR>
 *     <TD><TT>--properties <I>filename</I></TT></TD>
 *     <TD>
 *       the name of a file in <TT>CLASSPATH</TT> containing the
 *       domain-to-database mappings, if not
 *       <TT>smtpServer.properties</TT>
 *     </TD>
 *   </TR>
 *   <TR>
 *     <TD><TT>--log <I>filename</I></TT></TD>
 *     <TD>
 *       the name of the log file (defaults to
 *       <TT>/usr/local/apache/log/messageboard-receivemail.log</TT>
 *     </TD>
 *   </TR>
 * </TABLE>
 */

So you need to copy smtpServer.properties.example to smtpServer.properties and
add your own line of the form

   org.paneris.melati.boards.receivemail.database.messageboards.x.com=x


5) Customise the UI
-------------------

You can see the default UI to the boards by pointing your browser at

/testapp/org.paneris.melati.boards.BoardAdmin/testdb/Types

If you want to customise this, one way to do it is to override the
default handler and templates thus:

    com.somecompany.testapp.boards.BoardAdmin.java:
    -----------------------------------------------

    package com.somecompany.testapp.boards;

    import org.melati.*;
    import org.melati.util.*;
    import org.melati.poem.*;
    import org.paneris.melati.boards.model.*;

    public class BoardAdmin extends org.paneris.melati.boards.BoardAdmin {

      protected String boardTemplate(TemplateContext context, String name) {
        return "melati/boards/" + name;
      }

    }


(This assumes that the directory containing the com.somecompany tree - and in
particular the `testapp' directory and code - is in Webmacro's templatePath).

If you don't override this, make sure that org/paneris is in the templatePath.

Then copy all the templates to the com.company.testapp.boards directory
and modify them as required.



6) Notes
--------

Board's attachmentpath and attachmenturl are set automatically from the
BoardAttachmentPath/URL setting and the name of the board.

Attachments path/url are set automatically from the board attachmentpath/url
and the filename of the attachment.

When you create a board, the user who created it is automatically subscribed
to it as a manager.

_guest_ cannot be subscribed to boards (and so cannot create them - see above).

Use Attachment.setFilename_unique[_unsafe] to prevent overwriting old files.
(note, message must be set already in the Attachment)

Messages are distributed using a new Thread if you call Message.distribute().
This is preferable to Board.distribute(message) which does the real work.

You will need to ensure that your User[Table] extends
org.paneris.melati.boards.model.User[Table]

--EOF--










