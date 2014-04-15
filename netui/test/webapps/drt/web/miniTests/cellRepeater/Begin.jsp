<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Cell Repeater</title>
<style type="text/css">
.cellTable {
        border: 1px solid red;
        border-collapse: collapse;
        empty-cells: show;
} 
.cellCell {
        border: 1px solid red;
} 
</style>
</head>
<body>
<h4>Cell Repeater</h4>
<b> more cells than data</b>
<db:cellRepeater dataSource="pageFlow.data"
        tableClass="cellTable"
        cellClass="cellCell"
        rows="4" columns="4">
  Item: <netui:span value="${container.item}" />
</db:cellRepeater>
<hr />
<b>less cells than data</b>
<db:cellRepeater dataSource="pageFlow.data" rows="3" columns="2">
  Item: <netui:span value="${container.item}" />
</db:cellRepeater>
<hr />
<b>Veritical Repeat</b>
<db:cellRepeater verticalRepeat="true"
        dataSource="pageFlow.data" rows="2" columns="3">
  Item: <netui:span value="${container.item}" />
</db:cellRepeater>
<hr />
<b>Vertical Repeat = false</b>
<db:cellRepeater verticalRepeat="false"
        dataSource="pageFlow.data" rows="2" columns="3">
  Item: <netui:span value="${container.item}" />
</db:cellRepeater>
<hr />
<b>Databind rows</b>
<db:cellRepeater 
        dataSource="pageFlow.data" rows="${pageFlow.rows}">
  Item: <netui:span value="${container.item}" />
</db:cellRepeater>
<hr />
<b>Databind cols</b>
<db:cellRepeater 
        dataSource="pageFlow.data" columns="${pageFlow.cols}">
  Item: <netui:span value="${container.item}" />
</db:cellRepeater>
</body>
</html>
