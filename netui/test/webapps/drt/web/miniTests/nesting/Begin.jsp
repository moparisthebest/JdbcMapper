<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>ButtonSubmit</title>
</head>
<body>
<netui:anchor action="/nest" >Nest Page Flow</netui:anchor>
<br />
Last Action: <netui:span value="${pageFlow.action}"/>
</body>
</html>
