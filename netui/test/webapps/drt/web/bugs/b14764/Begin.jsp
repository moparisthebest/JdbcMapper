<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<html>
<head>
<title>Compound Expressions</title>
</head>
<body>
<h4>Compound Expression</h4>
<p style="color:green">
This test demonstrates binding to multiple page flow variables inside
the same label.  The value should be 'lastname, firstname', where the
names are bound to page flow variables.  This is the only page hit in 
this test.
</p>
Name: <netui:span value="${pageFlow.lastName}, ${pageFlow.firstName}" />
</body>
</html>

	


			   
