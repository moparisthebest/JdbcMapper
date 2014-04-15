<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="temp" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title><temp:attribute name="title"/></title>
<link rel="stylesheet" href="/coreWeb/demo/default.css" type="text/css" />
<netui:base />
</head>
<body>
<table width="300" class="wiztable">
  <tr>
    <td class="wizhead" colspan="3"><b><temp:attribute name="pageTitle"/></b></td>
  </tr>
  <tr>
    <temp:includeSection name="wizBody"/>
  </tr>
</table>
</body>
</html>
