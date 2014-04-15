<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Global Forwards Next Page</title>
</head>
<body>
<h4>Global Forwards Next Page</h4>
Last Action: <b><netui:span value="${pageFlow.action}"/></b><br />
<netui:anchor action="/home">Home</netui:anchor>
</body>
</html>
