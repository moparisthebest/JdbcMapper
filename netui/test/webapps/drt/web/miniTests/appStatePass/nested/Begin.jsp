<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Nested Page</title>
</head>
<body>
<h4>Nested Page</h4>
<netui:anchor action="nestOne" >Nest Return to Page</netui:anchor><br />
<netui:anchor action="nestTwo" >Nest Return to Action</netui:anchor><br />
<hr />
Global State:<b><netui:content value="${pageFlow.state}"/></b>
</body>
</html>
