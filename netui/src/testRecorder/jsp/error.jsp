
<%@ page import="org.apache.beehive.netui.tools.testrecorder.server.TestRecorderServlet,
                 org.apache.beehive.netui.tools.testrecorder.shared.Constants" %>

<html>

<head>
<title>Test Recorder Error</title>
</head>

<body>

<h2>Test Recorder Error</h2>

<%@ include file="admin.inc" %>
<hr>

<br>

<%
    String msg = null;
    msg = (String) request.getAttribute( Constants.MSG_ATTRIBUTE );
%>
<%= ( msg == null ) ? "no message provided." : msg %>

<br>
<br>

<hr>

<%@ include file="status.inc" %>

</body>

</html>
