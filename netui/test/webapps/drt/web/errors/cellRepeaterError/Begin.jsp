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
<b>Error in Cell Repeater - rows/cols</b>
<db:cellRepeater 
        dataSource="pageFlow.data" >
  Item: <netui:span value="${container.item}" />
</db:cellRepeater>
<hr />
<b>Error in Cell Repeater - datasource</b>
<db:cellRepeater 
        dataSource="pageFlow.foo" rows="5">
  Item: <netui:span value="${container.item}" />
</db:cellRepeater>
</body>
</html>
