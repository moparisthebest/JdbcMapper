<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui:html>
    <head>
        <title>Control Lifecycle Test</title>
    </head>
    <netui:body>
        <p style="color:green">This test will call a method on a control in the three blocks
	that can run control code in a page flow, the onCreate, the Action and during JSP rendering.
	The table below contains an echo'd message from a control.  Each line represents calling the control
	at various stages of the page flow life cycle for rendering a page.
	</p>
	<table>
        <tr><td>onCreate message</td><td>'${requestScope.createMsg}'</td></tr>
        <tr><td>begin message</td><td>'${requestScope.beginMsg}'</td></tr>
        <tr><td>property access message</td><td>'${pageFlow.propertyMsg}'</td></tr>
	</table>
    </netui:body>
</netui:html>