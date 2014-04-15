<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>PageInput Test29</title>
   </head>
   <body>
      <h3 align="center">PageInput Test29 - Jsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <h3><font color="blue">
            This page has no delcarePageInput yet displays pageInput values.
         </font></h3>
         <br/><br/>

         PageInput value1: <netui:span value="${pageInput.myString}" />
         <br/><br/>
         <netui:anchor action="finish">Finish...</netui:anchor>
      </center>
   </body>
</html>
