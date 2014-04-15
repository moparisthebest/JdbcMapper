<%@ page language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="temp" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<html>
<head>
<title><temp:attribute name="title"/></title>
<link rel="stylesheet" href="/coreWeb/site/style.css"/>
<netui:base />
</head>
<body>
<table class="header" width="100%">
<tr>
  <td width="150" style="padding:4,0,0,4">
      <netui:image src="/coreWeb/template/tempRelative/image/godzilla.gif"
        height="66" width="48" />
  </td><td valign="top">
      <h1 class="title"><temp:attribute name="title"/></h1>
  </td></tr>
<tr><td colspan="2">
<hr />
</td></tr>
<tr>
  <td valign="top" width="100px">
    <netui:anchor action="home">Home</netui:anchor><br />
    <hr />
    <temp:includeSection name="actions"
        defaultPage="/template/tempRelative/noActions.jsp"/>
  </td><td valign="top">
    <span style="background-color:white">
    <table width="100%">
    <tr><td>
    <temp:includeSection name="content"/>
    </td></tr>
    </table>
    </span>
  </td>
</tr>
</table>
</body>
</html>
