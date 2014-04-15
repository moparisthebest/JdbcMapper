
<%@ page import="org.apache.beehive.netui.tools.testrecorder.server.*,
                 org.apache.beehive.netui.tools.testrecorder.server.state.PlaybackSession,
                 org.apache.beehive.netui.tools.testrecorder.shared.Constants" %>

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
    PlaybackSession playSession = (PlaybackSession) request.getAttribute(
        Constants.PLAYBACK_SESSION_ATTRIBUTE );
%>

<%= ( msg == null ) ? "error, no message provided." : msg %>
<br>
<br>

<%= ( playSession == null ) ? "" : playSession.toString() %>

<hr>

<%@ include file="status.inc" %>

</body>

</html>
