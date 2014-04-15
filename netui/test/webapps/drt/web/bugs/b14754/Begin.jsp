<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Array Type Binding</title>
</head>
<body>
<h4>Array Type Binding</h4>
<p style="color:green">
This will bind a label to the 4th element of an array inside a page flow.  The value should be '<b>5</b>'.   This is the only page
hit during this test.
</p>
Element [4]: <netui:span value="${pageFlow.foo[4]}" />
</body>
</html>

	


			   
