<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Passing a Bad Form</title>
</head>
<body>
<h4>Passing a Bad Form</h4>    
<netui:anchor action="getUserInfo">Get User</netui:anchor>
<hr />
User: <netui:span value="${pageFlow.user}"/><br />
</body>
</html>
