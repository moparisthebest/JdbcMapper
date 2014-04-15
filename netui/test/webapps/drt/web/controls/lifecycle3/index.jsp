<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui:html>
    <head>
        <title>Control Lifecycle Test 3</title>
    </head>
    <netui:body>
        <h4>Control Lifecycle Test 3</h4>
        <p style="color:green">This test verifies that the request and response and servlet context
		are correct in the control context;
	</p>
	<table border='1'>
        <tr><td>Info</td><td>${requestScope.info}</td></tr>
        <tr><td>onCreate message</td><td>'${requestScope.createMsg}'</td></tr>
        <tr><td>begin message</td><td>'${requestScope.beginMsg}'</td></tr>
        <tr><td>property access message</td><td>'${pageFlow.propertyMsg}'</td></tr>
	</table>
    </netui:body>
</netui:html>