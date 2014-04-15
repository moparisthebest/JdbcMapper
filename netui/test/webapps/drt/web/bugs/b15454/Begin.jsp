<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Update through a repeater</title>
</head>
<body>
<h4>Update Through A Repeater</h4>
<netui:form action="/postback">
<db:repeater dataSource="pageFlow.info">
<db:repeaterItem>
  <netui:textBox dataSource="container.item"/><br />
</db:repeaterItem>
</db:repeater>
<br />
<netui:button type="submit">Submit</netui:button>
</netui:form>
<br />
<b>Results:</b><br />
<db:repeater dataSource="pageFlow.info">
<db:repeaterItem>
  <netui:span value="${container.item}"/><br />
</db:repeaterItem>
</db:repeater>
<br />
</body>
</html>
