<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
</head>

<netui:body>
    <p>Test container-generated control's events, onCreate, onAquire and onRelease.</p>

    <netui:anchor href="beanrecord/begin.do">Test specifically for the onAquire event.</netui:anchor>
    <br>
    <netui:anchor href="implrecord/testContextEvents.do">Test that all expected lifecycle events are generated.</netui:anchor>
    <br>
    <netui:anchor href="implrecord/testContextEventsP.do">Test that all expected lifecycle events are generated (programatic control instantiation).</netui:anchor>
    <br>

    <h3>Test Results</h3>
    <table border="1">
        <tr><td>Test Results:</td><td>'${pageInput.message}'</td></tr>
    </table>
</netui:body>
</html>
