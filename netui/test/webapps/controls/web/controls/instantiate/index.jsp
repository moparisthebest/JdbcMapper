<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
</head>

<netui:body>
    <p>Test control instantiation.</p>

    <netui:anchor action ="instantiate.do">Test control instantiation (declaritive).</netui:anchor>
    <br>
    <netui:anchor action="instantiateP.do">Test control instantiation (programmatic).</netui:anchor>
    <br>

    <h3>Test Results</h3>
    <table border="1">
        <tr><td>Test Results:</td><td>'${pageInput.message}'</td></tr>
    </table>
</netui:body>
</html>
