<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Scope One</title>
</head>
<body>
<h4>Scope One</h4>
<netui:anchor action="/nav">Navigate</netui:anchor><br />
<netui:anchor action="/nestPageFlow">Nest Page Flow</netui:anchor><br />
Count: <netui:span value="${pageFlow.visits}"/>
</body>
</html>
