<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Link Submit</title>
</head>
<body>
<netui:anchor action="linkOne" >Link One</netui:anchor>
<netui:anchor action="/linkTwo" >Link Two</netui:anchor>
<br />
Last Action: <netui:span value="${pageFlow.action}"/>
</body>
</html>
