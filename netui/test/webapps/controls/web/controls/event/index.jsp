<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
</head>

<netui:body>
    <p>Test control event handlers.</p>

    <netui:anchor href="eventhandler/begin.do">Test control with multiple event sets.</netui:anchor>
    <br>
    <netui:anchor href="eventhandler/verifyResult.do">Verify the results of the prevous test, this test should only be invoked after the preceeding test.</netui:anchor>
    <br>
    <netui:anchor href="listener/begin.do">Test an event listener.</netui:anchor>
    <br>

    <h3>Test Results</h3>
    <table border="1">
        <tr><td>Test Results:</td><td>'${pageInput.message}'</td></tr>
    </table>
</netui:body>
</html>
