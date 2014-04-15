<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<netui:html>
<head>
<title>Exceptions Handling and Reporting</title>
</head>
<netui:body>
<h4>Exceptions Handling and Reporting</h3>
<p style="color:green">
This test verifies that an action that throws an exception is caught by a exception handler.  When the
link below is pressed, the action will throw an exception.  The exception handler then handles the
exception and forwards control to the ErrorPage.jsp.  This page uses the <b>Exceptions</b> tag to report
the error.
</p>
<netui:anchor action="throwGlobal">Throw Exception</netui:anchor><br>
</netui:body>
</netui:html>
