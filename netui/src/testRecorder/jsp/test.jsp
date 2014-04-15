
<%
   String contextPath = ((HttpServletRequest)request).getContextPath();
%>

<html>

<head>
<title>Test Recorder Admin</title>
</head>

<body>

<h2>Test Recorder Admin</h2>

<%@ include file="admin.inc" %>
<hr>

<br>

<%@ include file="status.inc" %>

<hr>

<br>
<a href="testRecord.jsp">a simple page to record data for tests</a>
<br>
<br>

<br>
<a href="<%= contextPath %>/testRecorder">no params</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=record">mode=record</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=unknown">mode=unknown</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=record&cmd=start">mode=record, cmd=start</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=record&cmd=unknown">mode=record, cmd=unknown</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=record&cmd=start&testUser=ozzy">
mode=record, cmd=start, testUser=ozzy</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=record&cmd=start&testName=test1">
mode=record, cmd=start, testName=test1</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=record&cmd=start&description=someDescription">
mode=record, cmd=start, description=someDescription</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=record&cmd=start&overwrite=false">
mode=record, cmd=start, overwrite=false</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=record&cmd=start&overwrite=true">
mode=record, cmd=start, overwrite=true</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=record&cmd=start&testRecorder.filter.skip=false">
mode=record, cmd=start, testRecorder.filter.skip=false</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=record&cmd=start&testRecorder.filter.skip=true">
mode=record, cmd=start, testRecorder.filter.skip=true</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=record&cmd=start&testName=test1&testUser=ozzy&description=someDescription&overwrite=false&testRecorder.filter.skip=true">
START record for 'test1', do not overwrite</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=record&cmd=start&testName=test1&testUser=ozzy&description=someDescription&overwrite=true&testRecorder.filter.skip=true">
START record for 'test1', overwrite if necessary</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=record&cmd=start&testName=test2&testUser=ozzy&description=someDescription&overwrite=true&testRecorder.filter.skip=true">
START record for 'test2', overwrite if necessary</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=record&cmd=stop&testRecorder.filter.skip=true">record STOP</a>
<br>

<hr>

<a href="<%= contextPath %>/testRecorder?mode=playback">mode=playback</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=playback&cmd=start">mode=playback, cmd=start</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=playback&cmd=unknown">mode=playback, cmd=unknown</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=playback&cmd=start&testUser=ozzy">
mode=playback, cmd=start, testUser=ozzy</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=playback&cmd=start&testName=test1">
mode=playback, cmd=start, testName=test1</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=playback&cmd=start&description=someDescription">
mode=playback, cmd=start, description=someDescription</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=playback&cmd=start&testRecorder.filter.skip=false">
mode=playback, cmd=start, testRecorder.filter.skip=false</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=playback&cmd=start&testRecorder.filter.skip=true">
mode=playback, cmd=start, testRecorder.filter.skip=true</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=playback&cmd=start&testName=test1&testUser=ozzy&description=someDescription&overwrite=false&testRecorder.filter.skip=true">
START playback for 'test1'</a>
<br>
<a href="<%= contextPath %>/testRecorder?mode=playback&cmd=start&testName=test2&testUser=ozzy&description=someDescription&overwrite=false&testRecorder.filter.skip=true">
START playback for 'test2'</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=playback&cmd=stop&testRecorder.filter.skip=true">playback STOP, no testId</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=xml&cmd=xml&file=unknown">unknown XML file</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=xml&cmd=xml&file=config">config XML file</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=xml&cmd=xml&file=webapp">webapp XML file</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=xml&cmd=xml&file=tests">tests XML file</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=xml&cmd=html&file=unknown">unknown HTML file</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=xml&cmd=html&file=config">config HTML file</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=xml&cmd=html&file=webapp">webapp HTML file</a>
<br>

<a href="<%= contextPath %>/testRecorder?mode=xml&cmd=html&file=tests">tests HTML file</a>
<br>

</body>

</html>
