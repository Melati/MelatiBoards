       README
       ======

Sample web.xml part:

  <servlet>
    <servlet-name>
        NNTPServer
    </servlet-name>
    <servlet-class>
        org.paneris.melati.boards.receivemail.nntp.NNTPServerServlet
    </servlet-class>
    <init-param>
     <param-name>database</param-name>
     <param-value>paneris</param-value>
    </init-param>
    <init-param>
     <param-name>prefix</param-name>
     <param-value>org.paneris</param-value>
    </init-param>
    <init-param>
     <param-name>log</param-name>
     <param-value>d:\temp\nntpserver.log</param-value>
    </init-param>
    <init-param>
     <param-name>port</param-name>
     <param-value>119</param-value>
    </init-param>
    <init-param>
     <param-name>identifier</param-name>
     <param-value>paneris.org</param-value>
    </init-param>
    <load-on-startup>100</load-on-startup>
  </servlet>