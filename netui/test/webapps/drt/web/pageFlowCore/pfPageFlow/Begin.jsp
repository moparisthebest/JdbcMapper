<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>PageFlow Errors</title>
</head>
<body>
<h4>PageFlow Errors</h4>
<netui:anchor action="getInfo">Generate Page Flow Calls</netui:anchor><br />
<netui:anchor action="sendError">Generate An Error Page</netui:anchor>
<hr />
<netui:content value="${pageFlow.results}"/>

</body>
</html>
