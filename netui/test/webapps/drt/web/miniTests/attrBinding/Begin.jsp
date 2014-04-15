<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<netui:html>
<head>
<title>Attribute Binding</title>
</head>
<netui:body>
<h4>Attribute Binding to Base Types</h4>
<p style="color:green">
This test will bind the <b>Label value</b> property to a bunch of base types (int, boolean, float and String).
The binding is done through both single value properties and array properties using the repeater.  This is a single
page test.
</p>
<h4>Base Type Attributes</h4>
int Property: <netui:span value="${pageFlow.prop1}"/><br>
String Property: <netui:span value="${pageFlow.prop2}"/><br>
float Property: <netui:span value="${pageFlow.prop3}"/><br>
boolean Property: <netui:span value="${pageFlow.prop4}"/><br>

<h4>Base Type Array Attributes</h4>
<b>int[]</b><br>
<table><tr>
<db:repeater dataSource="pageFlow.props1">
  <td  width="50px"><netui:span value="${container.item}"/></td>
</db:repeater>
</tr></table>

<b>String[]</b></br>
<table><tr>
<db:repeater dataSource="pageFlow.props2">
  <td  width="50px"><netui:span value="${container.item}"/></td>
</db:repeater>
</tr></table>

<b>float[]</b></br>
<table><tr>
<db:repeater dataSource="pageFlow.props3">
  <td  width="50px"><netui:span value="${container.item}"/></td>
</db:repeater>
</tr></table>

<b>boolean[]</b></br>
<table><tr>
<db:repeater dataSource="pageFlow.props4">
  <td  width="50px"><netui:span value="${container.item}"/></td>
</db:repeater>
</tr></table>
</netui:body>
</netui:html>

	


			   
