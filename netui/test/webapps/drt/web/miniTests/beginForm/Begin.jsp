<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Pass a Form to a Begin</title>
</head>
<body>
<h4>Pass a Form to a Begin</h4>    
<netui:anchor action="getUserInfo">Get User</netui:anchor>
<hr />
User: <netui:span value="${pageFlow.user}"/><br />
</body>
</html>
