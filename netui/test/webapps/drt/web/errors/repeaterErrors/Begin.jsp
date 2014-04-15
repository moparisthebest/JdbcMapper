<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Repeater Errors</title>
</head>
<body>
<h4>Repeater Errors</h4>
<br />
<b>standalone repeaterItem</b></br>
<db:repeaterItem>
  <netui:span value="${container.item}"/><br />
</db:repeaterItem>

<b>standalone repeaterHeader</b></br>
<db:repeaterHeader>
  <table><tr>
</db:repeaterHeader>

<b>standalone repeaterFooter</b></br>
<db:repeaterFooter>
  <table><tr>
</db:repeaterFooter>

<b>Expression Error</b></br>
<db:repeater dataSource="pageFlow.foo" >
<db:repeaterItem>
  <netui:span value="${container.item}"/><br />
</db:repeaterItem>
</db:repeater>
<br />
</body>
</html>
