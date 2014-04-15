<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Global App Binding -- no JPF</title>
  </head>

  <body>
    <b>Global App Binding -- no JPF</b>
<br/>
<br/>
<b>JSP 2.0 EL</b>
The Global.app says: <netui:span value="${globalApp.sayHello}"/>
<br/>
<br/>
<b>NetUIEL</b><br/>
<netui-data:repeater dataSource="globalApp.strings">
    ${container.item}<br/>
</netui-data:repeater>
<br/>
<br/>
  </body>
</html>
