<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Simple JSP -&gt; JPF Test</title>
    <link rel="stylesheet" href="../default.css" type="text/css"/>
  </head>

  <body>
    <b>Call Page Flow Tests</b><br/>
<netui-data:callPageFlow resultId="cart" method="getCart"/>

<netui-data:repeater dataSource="pageFlow.cart.lineItemList">
    <netui-data:repeaterHeader>
        <table class="table">
        <tr class="tablehead"><td>Name</td><td>Quantity</td><td>Price</td></tr>
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
        <tr class="row">
        <td><netui:span value="${container.item.name}"/></td>
        <td><center><netui:span value="${container.item.quantity}"/></center></td>
        <td><netui:span value="${container.item.price}">
                <netui:formatNumber pattern="$#,###,###.00"/>
            </netui:span>
        </td>
        </tr>
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
        <tr>
            <td></td>
            <td>Sum:</td>
            <td>
            <netui-data:callPageFlow resultId="cartSum" method="sumCartItems">
                <netui-data:methodParameter value="${pageFlow.cart.lineItemList}"/>
            </netui-data:callPageFlow>
            <netui:span value="${pageScope.cartSum}">
                <netui:formatNumber pattern="$#,###,###.00"/>
            </netui:span>
            </td>
         </tr>
        </table>
    </netui-data:repeaterFooter>
</netui-data:repeater>
<br/>
<br/>
<netui:anchor action="begin">Home</netui:anchor>
  </body>
</html>
