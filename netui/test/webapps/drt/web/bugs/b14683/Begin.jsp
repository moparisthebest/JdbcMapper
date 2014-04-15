<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Hit a JSP Directly</title>
</head>
<body>
<h4>Hit a JSP Directly</h4>
<p style="color:green">
In this example, we hit the JSP directly before the page flow is created.  There is a label which binds to 
a page flow variable.  The JSP filter needs to insure that the page flow is created.  Below you should see the 
value '<b>String</b>'.  This is the only page of the test.
</p>
Value: '<netui:span value="${pageFlow.data}" />'
</body>
</html>
