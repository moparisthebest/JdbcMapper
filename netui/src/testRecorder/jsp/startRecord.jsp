
<%
   String contextPath = ((HttpServletRequest)request).getContextPath();
%>

<%@ page import="java.io.File" %>

<%
  
%>


<html>

<head>
<title>Recording Session Start</title>
</head>

<body>

<h2>Recording Session Start</h2>

<%@ include file="admin.inc" %>
<hr>

<br>

<form method="POST" action="<%= contextPath %>/testRecorder?mode=record&cmd=start" >

  <table>

  <tr>
    <td align="right"><strong>Test Name: </strong></td>
    <td align="left"><input name="testName" type="text" size="30" /></td>
  </tr>

  <tr>
    <td align="right"><strong>Test User: </strong></td>
    <td align="left"><input name="testUser" type="text" size="30" /></td>
  </tr>

  <tr>
    <td align="right"><strong>Description: </strong></td>
    <td align="left"><textarea name="description" rows="5" cols="40"></textarea></td>
  </tr>

  <tr>
    <td align="right"><strong>Overwrite existing test: </strong></td>
    <td align="left"><input type="checkbox" name="overwrite" value="true" /></td>
  </tr>

  </table>

  <br>

  <input type="submit" />
</form>

</body>

</html>
