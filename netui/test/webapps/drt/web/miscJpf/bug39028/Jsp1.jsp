<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>

<netui:html>
   <head>
      <title>MiscJpf Bug 39028 test</title>
   </head>
   <body>
      <h3 align="center">MiscJpf Bug 39028 test - Jsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <netui-data:declarePageInput name="visit" type="int" />
      <center>
         <font color="blue">
            Page visit: <font color="blue">"<netui:span value="${pageInput.visit}" />"</font>
         </font>
         <br/><br/>
         The 1st time you see this page, press here:
         <netui:anchor action="action1">Continue...</netui:anchor>

         <br/><br/>
         The 2nd time you see this page, press here:
         <netui:anchor action="action2">Continue...</netui:anchor>

         <br/><br/>
         The 3rd time you see this page, press here:
         <netui:anchor action="action2">Continue...</netui:anchor>

         <br/><br/>
         The 4th time you see this page, press here:
         <netui:anchor action="finish">Finish...</netui:anchor>
      </center>
   </body>
</netui:html>
