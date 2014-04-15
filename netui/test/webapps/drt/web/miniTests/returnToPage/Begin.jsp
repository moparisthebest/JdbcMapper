<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Return to Page</title>
</head>
<body>
<h4>Return To Page</h4>
<netui:anchor action="/nest" >Nest Page Flow</netui:anchor>
<br />
Last Action: <netui:span value="${pageFlow.action}"/>
</body>
</html>
