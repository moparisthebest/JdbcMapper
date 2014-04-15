<%@ page language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="temp" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title><temp:attribute name="title"/></title>
<link rel="stylesheet" href="/coreWeb/site/style.css">
<netui:base />
</head>
<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0">
<table class="header" width="100%">
<tr><td width="150" style="padding:4,0,0,4">
  <img src="/coreWeb/images/launch.jpg" height="66" width="48"/>
</td><td valign="top">
<h1 class="title"><temp:attribute name="title"/></h1>
</td></tr>
<tr><td colspan="2">
<hr />
</td></tr>
</table>

<table class="outer" cellpadding="0" cellspacing="0">
<tr>
<span>
<td class="toc" valign="top" width="150">
<temp:includeSection name="toc" defaultPage="/testToc.jsp"/>
</td>
<td valign="top">
<div class="body">
<temp:includeSection name="body"/>
</div>
</td>
</tr>
</table>
</body>
</html>
