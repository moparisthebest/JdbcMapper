<%@ page language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template" %>

<netui:html>
  <head>
    <title>NetUI Data Grid Samples</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/databinding/datagrid/site/css/default.css" type="text/css"/>
  </head>
  <netui:base/>
  <netui:body>
    <p>
    <b><netui-template:attribute name="title"/></b>
    <table width="100%">
    <tr><td></td></tr>
    <tr><td>
        <netui-template:includeSection name="body"/>
    </td></tr>
    </p>
    <table>
    <tr><td><netui:anchor href="/netuiDRT/databinding/datagrid/basic/index.jsp">Home</netui:anchor></td></tr>
    </table>
    </p>
  </netui:body>
</netui:html>
