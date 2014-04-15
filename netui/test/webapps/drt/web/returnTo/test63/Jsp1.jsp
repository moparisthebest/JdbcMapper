<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>ReturnTo Test63</title>
   </head>
   <body>
      <h3 align="center">ReturnTo Test63 - Jsp2.jsp</h3>
      <hr width="95%"/>
      <br/>
        <center>
         <netui:anchor action="action1">First visit...</netui:anchor>
         <br/><br/>
         <netui:anchor action="action3">Second visit...</netui:anchor>
         <br/><br/>
         <netui:anchor action="finish">Third visit...</netui:anchor>
        </center>
      </center>
   </body>
</html>
