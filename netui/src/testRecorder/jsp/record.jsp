
<%@ page import="org.apache.beehive.netui.tools.testrecorder.server.TestRecorderServlet,
                 org.apache.beehive.netui.tools.testrecorder.shared.Constants" %>
<%@ page import="org.apache.beehive.netui.tools.testrecorder.server.state.RecordSession" %>

<html>

<head>
<title>Test Recorder</title>
</head>

<body>

<h2>Test Recorder</h2>

<%@ include file="admin.inc" %>
<hr>

<br>

<%
    String msg = null;
    msg = (String) request.getAttribute( Constants.MSG_ATTRIBUTE );
    RecordSession recSession = (RecordSession) request.getAttribute(
        Constants.RECORD_SESSION_ATTRIBUTE );
%>

<%= ( msg == null ) ? "error, no message provided." : msg %>
<br>
<br>

<%= ( recSession == null ) ? "" : recSession.toString() %>

<hr>

<%@ include file="status.inc" %>

</body>

</html>
