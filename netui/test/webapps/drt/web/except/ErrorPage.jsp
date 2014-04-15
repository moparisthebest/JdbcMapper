<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Error Page</title>
</head>
<body>
<h4>Report Exception</h4>
Message <netui:span value="${pageFlow.message}"/><br />
<netui:anchor action="goHome">Return</netui:anchor>
</body>
</html>
