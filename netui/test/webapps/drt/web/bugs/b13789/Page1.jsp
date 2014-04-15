<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Page1</title>
</head>
<body>
<p style="color:green">
This page directly binds to the page flow using a label
</p>
<hr>
Label: <netui:span value="${pageFlow.string}"/>
</body>
</html>
