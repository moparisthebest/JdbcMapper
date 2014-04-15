<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Select Tests</title>
</head>
<body>
<h4>Select</h4>
<netui:form action="/postback">
<table>
<tr><td><b>Select 1, Fully Bound</b></td><td>
  <netui:select dataSource="pageFlow.selected"
    optionsDataSource="${pageFlow.options}"
    defaultValue="${pageFlow.defaultValue}"/>
  </td></tr>
<tr><td><b>Select 1, Fully Bound, Option = collection</b></td><td>
  <netui:select dataSource="pageFlow.selected2"
    optionsDataSource="${pageFlow.colOptions}"
    defaultValue="${pageFlow.defaultValue}"/>
  </td></tr>
<tr><td colspan=2>
<netui:button type="submit">Submit</netui:button>
</td></tr>
</table>
</netui:form>
<br />
Select 1: <netui:span value="${pageFlow.selected}"/><br />
Select 2: <netui:span value="${pageFlow.selected2}"/>
</body>
</html>
