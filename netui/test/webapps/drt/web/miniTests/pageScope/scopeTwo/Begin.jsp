<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Scope Two</title>
</head>
<body>
<h4>Scope Two</h4>
<netui:anchor action="/nav">Navigate</netui:anchor><br />
<netui:anchor action="/changePageFlow">Change Page Flow</netui:anchor><br />
Count: <netui:span value="${pageFlow.visits}"/>
</body>
</html>
