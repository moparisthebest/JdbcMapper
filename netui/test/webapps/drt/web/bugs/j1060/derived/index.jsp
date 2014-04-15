<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui:html>
    <head>
        <title>Inherited Control Test</title>
    </head>
    <netui:body>
        <h3>Inherited Control Test</h3>
        <p> This test will call an inherited page flow method that calls
            a method on a control declared with default (package) access
            in the parent page flow Controller. The test will call the
            inherited method to access/use the control in page flow
            onCreate, the Action, and during JSP rendering.
        </p>
        <p> The table below contains an echo'd message from the control in
            the parent page flow Controller. Each line represents calling
            the control at various stages of the page flow life cycle for
            rendering a page.
        </p>
        <table>
            <tr><td>onCreate message</td><td>'${requestScope.createMsg}'</td></tr>
            <tr><td>action message</td><td>'${requestScope.actionMsg}'</td></tr>
            <tr><td>property access message</td><td>'${pageFlow.propertyMsg}'</td></tr>
        </table>
        <br>
        <netui:anchor action="anotherAction">another action</netui:anchor>
    </netui:body>
</netui:html>
