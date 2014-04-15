<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
</head>

<netui:body>
    <p>Test that all of the expected contextual services are available to a control in a page flow container.
    Controller 1 contains a control which was declarativly instantiated, Controller 2 contains a control which
    was programatically instantiated.</p>

    <netui:anchor action="testServices">Test services for a control.</netui:anchor>
    <br>
    <netui:anchor action="testServicesP">Test services for a control declared programmatically.</netui:anchor>
    <br>

    <h3>Test Results</h3>
    <table border="1">
        <tr><td>Test Results:</td><td>'${pageInput.message}'</td></tr>
    </table>
</netui:body>
</html>
