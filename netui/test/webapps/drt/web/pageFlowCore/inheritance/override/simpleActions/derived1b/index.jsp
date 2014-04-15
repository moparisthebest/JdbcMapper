<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
    <title>Page Flow Inheritance - Overriding Simple Actions</title>
</head>
<body>
    <h1>Page Flow Inheritance - Overriding Simple Actions</h1>
    <h3>Derived1b</h3>
    ${pageFlow.actionInfo}
    <br/>
    <netui:anchor action="actionOne">Action One</netui:anchor>
    <br/>
    <a href="../begin.do">back to start</a>
</body>
</html>
