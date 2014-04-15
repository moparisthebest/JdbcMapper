<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>

<netui:html>
   <head>
      <title>MiscJpf Bug 38484 test</title>
   </head>
   <body>
      <h3 align="center">MiscJpf Bug 38484 test - Jsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <font color="blue">
            This page should NOT be secure, (http).  If it is, the test failed.
            <br/>
            The first time you see this page press the "Absolute" link, the second time
            press the "Relative" link.
         </font>
         <br/><br/>
         <netui:anchor href="/coreWeb/miscJpf/bug38484/action1.do">Absolute</netui:anchor>
         <br/><br/>
         <netui:anchor href="../bug38484/action1.do">Relative</netui:anchor>
      </center>
   </body>
</netui:html>
