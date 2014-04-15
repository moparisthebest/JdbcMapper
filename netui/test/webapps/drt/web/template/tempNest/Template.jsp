<%@ page language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="temp" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<html>
<head>
<title><temp:attribute name="title"/></title>
<netui:base />
</head>
<body>
<table cellspacing='0' border='1'>
<tr><th>Left</th><th>Content</th><th>Right</th></tr>
<tr>
<td><temp:includeSection name="left" defaultPage="leftDef.jsp"/></td>
<td><temp:includeSection name="content"/></td>
<td><temp:includeSection name="right" defaultPage="rightDef.jsp"/></td>
</tr>
</table>
</body>
</html>
