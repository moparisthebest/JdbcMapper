<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Page1</title>
</head>
<body>
<h4>Results</h4>
<p style="color:green">This binds to the results that should have been posted by the form.  You should see the results here
</p>
PageFlow.String: <netui:span value="${pageFlow.string}"/>
</body>
</html>
