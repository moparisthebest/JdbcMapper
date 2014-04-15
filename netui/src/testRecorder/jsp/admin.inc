
<%
   String prefix = ((HttpServletRequest)request).getContextPath();
%>

<a href="<%= prefix %>">Back to Titan Home Page</a>
<br>

<a href="<%= prefix %>/testRecorder/startRecord.jsp?testRecorder.filter.skip=true">Record Start Page</a>
<br>

<a href="<%= prefix %>/testRecorder?mode=record&cmd=stop&testRecorder.filter.skip=true">
<strong style="color: red">STOP</strong> recording</a>
<br>

<a href="<%= prefix %>/testRecorder?mode=admin&cmd=clean&testRecorder.filter.skip=true">
<strong style="color: red">Clean</strong> up Test Recorder sessions</a>
<br>

<a href="<%= prefix %>/testRecorder?mode=admin&cmd=setTestModeTrue&testRecorder.filter.skip=true">
Set test mode to <strong style="color: red">True</strong></a>
<br>

<a href="<%= prefix %>/testRecorder?mode=admin&cmd=setTestModeFalse&testRecorder.filter.skip=true">
Set test mode to <strong style="color: red">False</strong></a>
<br>

<a href="<%= prefix %>/testRecorder/status.jsp?testRecorder.filter.skip=true">Status</a>
<br>

<a href="<%= prefix %>/testRecorder/sessionInfo.jsp?testRecorder.filter.skip=true">Categories, Tests and JUnit Results</a>
<br>

<a href="<%= prefix %>/testRecorder/test.jsp?testRecorder.filter.skip=true">Test Page</a>
<br>
