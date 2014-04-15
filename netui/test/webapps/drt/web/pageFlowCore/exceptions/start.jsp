<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<html>
<head>
<title>ExceptionTest - Start Page</title>
</head>
<body bgcolor="white">

<h3>ExceptionTest - Start Page</h3>

<netui:anchor action="catchLocalToMethod">
    handle a local action-method exception with a method
</netui:anchor>
<br>
<netui:anchor action="catchLocalToPage">
    handle a local action-method exception by forwarding to a page
</netui:anchor>
<br>
<netui:anchor action="catchGlobalToMethod">
    handle a global exception with a method
</netui:anchor>
<br>
<netui:anchor action="catchGlobalToPage">
    handle a global exception by forwarding to a page
</netui:anchor>
<br>
<netui:anchor action="throwUnhandled">
    Generate an unhandled exception
</netui:anchor>
<br>
<netui:anchor action="failRoles">
    Try and raise an action that requires a role you don't fulfill.
</netui:anchor>
</body>
</html>
