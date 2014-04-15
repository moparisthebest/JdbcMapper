<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>ReturnTo Test47</title>
   </head>
   <body>
      <h3 align="center">ReturnTo Test47 - Jsp2.jsp</h3>
      <hr width="95%"/>
      <br/>
        <center>
            <netui:form action="action2">
               String 1: <netui:span value="${actionForm.string1}"/>
               <br/>
               String 2: <netui:span value="${actionForm.string2}"/>
               <br/><br/>
               <netui:button>Continue...</netui:button>
            </netui:form>
        </center>
      </center>
   </body>
</html>
