<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<html>
  <head>
    <title>Nested Repeaters</title>
  </head>

  <body>
    <b>Nested Repeaters</b>
<table>
<netui-data:repeater dataSource="pageInput.data">
    <tr><td><netui:span value="${container.item.name}"/></td></tr>
    <tr>
        <td>
        <table>
        <netui-data:repeater dataSource="container.item.numbers">
            <tr><td><netui:span value="${container.item}"/></td></tr>
        </netui-data:repeater>
        </table> 
        </td>
    </tr>
</netui-data:repeater>
</table>
  </body>
</html>
