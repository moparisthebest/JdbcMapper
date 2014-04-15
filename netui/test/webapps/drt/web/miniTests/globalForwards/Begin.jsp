<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Global Forwards</title>
</head>
<body>
<h4>Global Forwards</h4>
Last Action: <b><netui:span value="${pageFlow.action}"/></b><br />
<netui:anchor action="/next" >Next</netui:anchor>
</body>
</html>
