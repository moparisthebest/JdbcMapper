<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>

<netui:html>
   <head>
      <title>MiscJpf Bug 38484 test</title>
   </head>
   <body>
      <h3 align="center">MiscJpf Bug 38484 test - Jsp2.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <font color="blue">
            This page should be secure, (https).  If it is not, the test failed.
         </font>
         <br/><br/>
         <netui:anchor action="action2">Continue...</netui:anchor>
      </center>
   </body>
</netui:html>
