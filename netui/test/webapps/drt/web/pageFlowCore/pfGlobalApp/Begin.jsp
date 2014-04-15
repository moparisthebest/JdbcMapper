<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>PageFlow Global Info</title>
</head>
<body>
<h4>PageFlow Global Info</h4>
<netui:anchor action="globalAction_getInfo">Get Global Info</netui:anchor>
<hr />
<netui:content value="${pageFlow.globalAppInfo}"/>
</body>
</html>
