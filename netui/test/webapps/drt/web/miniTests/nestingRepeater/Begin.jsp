<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Nesting Repeater</title>
</head>
<body>
<h4>Nesting Repeater</h4>
<table><tr>
<db:repeater dataSource="pageFlow.data">
  <db:repeaterItem>
    <tr>
    <td><b><netui:span value="${container.item.name}"/></b></td>
    <db:repeater dataSource="container.item.values">
       <db:repeaterItem>
         <td><netui:span value="${container.item}"/></td>
       </db:repeaterItem>
    </db:repeater>
    <td><b><netui:span value="${container.item.end}"/></b></td>
    </tr>
  </db:repeaterItem>
</db:repeater>
</tr></table>
</body>
</html>
