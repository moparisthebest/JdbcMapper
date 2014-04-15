<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<netui:html>
  <head>
    <title>Double Sum Test</title>
  </head>
  <body>
    <b>Double Sum Test</b>
<%
  databinding.callMethod.TypeWithDoubleMethod obj = new databinding.callMethod.TypeWithDoubleMethod();
  request.setAttribute("doubleSumObject", obj);
  pageContext.setAttribute("doubleArray", new double[] {1.0, 1.0, 1.0});
%>
<b>Sum the values in an array of doubles</b>
<br/>
<br/>
<b>Data</b>
<br/>
<b>Double Array without type checking</b><br/>
<netui-data:repeater dataSource="pageScope.doubleArray">
  <netui-data:repeaterHeader>
    <table>
  </netui-data:repeaterHeader>
  <netui-data:repeaterItem>
    <tr><td>&nbsp;</td></td><td>${container.item}</td></tr>
  </netui-data:repeaterItem>
  <netui-data:repeaterFooter>
    <netui-data:callMethod object="${requestScope.doubleSumObject}" resultId="doubleSum" method="sum">
      <netui-data:methodParameter value="${pageScope.doubleArray}"/>
    </netui-data:callMethod>
    <tr><td>Sum Total:</td>
    <td>
      <netui:span value="${pageScope.doubleSum}"/>
    </td>
    </tr>
    </table>
  </netui-data:repeaterFooter>
</netui-data:repeater>
<br/>
<br/>
<b>Double Array with type checking</b><br/>
<netui-data:repeater dataSource="pageScope.doubleArray">
  <netui-data:repeaterHeader>
    <table>
  </netui-data:repeaterHeader>
  <netui-data:repeaterItem>
    <tr><td>&nbsp;</td></td><td>${container.item}</td></tr>
  </netui-data:repeaterItem>
  <netui-data:repeaterFooter>
    <netui-data:callMethod object="${requestScope.doubleSumObject}" resultId="doubleSum-withtypes" method="sum">
      <netui-data:methodParameter value="${pageScope.doubleArray}" type="double[]"/>
    </netui-data:callMethod>
    <tr><td>Sum Total:</td>
    <td>
      <netui:span value="${pageScope.doubleSum-withtypes}"/>
    </td>
    </tr>
    </table>
  </netui-data:repeaterFooter>
</netui-data:repeater>
</body>
</netui:html>
