<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Multi Select Tests</title>
</head>
<body>
<h4>Multi Select</h4>
<netui:form action="/postback">
<table>
<tr><td><b>Select 1, Fully Bound</b></td><td>
  <netui:select multiple="true" dataSource="pageFlow.selected"
    optionsDataSource="${pageFlow.options}"
    defaultValue="${pageFlow.defaultValue}"/>
  </td></tr>
<netui:button type="submit">Submit</netui:button>
</td></tr>
</table>
</netui:form>
<br />
<b>Select 1:</b><br />
<db:repeater dataSource="pageFlow.selected">
<db:repeaterItem>
  <netui:span value="${container.item}"/><br />
</db:repeaterItem>
</db:repeater>
<br />
</body>
</html>
