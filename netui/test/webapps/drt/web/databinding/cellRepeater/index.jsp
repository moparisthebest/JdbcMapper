<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui-html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Cell Repeater</title>
    <link rel="stylesheet" href="../default.css" type="text/css"/>
  </head>

  <body>
    <b>Cell Repeater</b>
<%
Integer[] smallItemArray = new Integer[4];
for(int i = 0; i < smallItemArray.length; i++)
{
    smallItemArray[i] = new Integer(i);
}
pageContext.setAttribute("smallItemArray", smallItemArray);
String[] itemArray = new String[17];
for(int i = 0; i < itemArray.length; i++)
{
    itemArray[i] = new String("" + i);
}
pageContext.setAttribute("itemArray", itemArray);

String[] zeroArray = new String[0];
pageContext.setAttribute("zeroArray", zeroArray);

pageContext.setAttribute("columnsStr", "4");
pageContext.setAttribute("rowsStr", "5");
pageContext.setAttribute("badRowsStr", "abcd");
%>
<br/>
<b><%= itemArray.length %> Item Array</b>
<br/>
<br/>
<b>4x5 with <%= itemArray.length %> items</b><br/>
<netui-data:cellRepeater dataSource="pageScope.itemArray" rows="4" columns="5"
                         tableClass="table" cellClass="cell" alternatingCellClass="altCell">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>4x5 with <%= itemArray.length %> items and databound row / column sizes</b><br/>
<netui-data:cellRepeater dataSource="pageScope.itemArray" rows="${pageScope.rowsStr}"
                         columns="${pageScope.columnsStr}" 
                         tableClass="table" cellClass="cell" alternatingCellClass="altCell">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>4x5 (vert) with <%= itemArray.length %> items</b><br/>
<netui-data:cellRepeater dataSource="pageScope.itemArray" rows="4" columns="5" verticalRepeat="true"
                         tableClass="table" cellClass="altCell" alternatingCellClass="cell">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>Small <%= smallItemArray.length %> Item Array</b><br/>
<br/>
<b>1x5 with <%= smallItemArray.length %> items</b><br/>
<netui-data:cellRepeater dataSource="pageScope.smallItemArray" rows="1" columns="5">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>1x5 (vert) with <%= smallItemArray.length %> items</b><br/>
<netui-data:cellRepeater dataSource="pageScope.smallItemArray" rows="1" columns="5" verticalRepeat="true">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>5x1 with <%= smallItemArray.length %> items</b><br/>
<netui-data:cellRepeater dataSource="pageScope.smallItemArray" rows="5" columns="1">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>5x1 (vert) with <%= smallItemArray.length %> items</b><br/>
<netui-data:cellRepeater dataSource="pageScope.smallItemArray" rows="5" columns="1" verticalRepeat="true">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>1x1 with <%= smallItemArray.length %> items</b><br/>
<netui-data:cellRepeater dataSource="pageScope.smallItemArray" rows="1" columns="1">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>1x1 (vert) with <%= smallItemArray.length %> items</b><br/>
<netui-data:cellRepeater dataSource="pageScope.smallItemArray" rows="1" columns="1" verticalRepeat="true">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>2x2 (horiz) with <%= smallItemArray.length %> items</b><br/>
<netui-data:cellRepeater dataSource="pageScope.smallItemArray" rows="2" columns="2">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>2x2 (vert) with <%= smallItemArray.length %> items</b><br/>
<netui-data:cellRepeater dataSource="pageScope.smallItemArray" rows="2" columns="2" verticalRepeat="true">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>Zero Item Array</b>
<br/>
<netui-data:cellRepeater dataSource="pageScope.zeroArray" rows="4" columns="5">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<netui-data:cellRepeater dataSource="pageScope.zeroArray" rows="4" columns="5" verticalRepeat="true">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<b>Zero Item Array with styles</b>
<br/>
<netui-data:cellRepeater dataSource="pageScope.zeroArray" rows="4" columns="5"
                         tableClass="table" cellClass="cell" alternatingCellClass="altCell">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>NULL Array</b>
<br/>
<netui-data:cellRepeater dataSource="pageScope.nullArray" rows="4" columns="5">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>NULL Array with styles</b>
<br/>
<netui-data:cellRepeater dataSource="pageScope.nullArray" rows="4" columns="5"
                         tableClass="table" cellClass="cell" alternatingCellClass="altCell">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<netui-data:cellRepeater dataSource="pageScope.nullArray" rows="4" columns="5" verticalRepeat="true">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>4x5 (vert) with <%= itemArray.length %> items</b><br/>
<netui-data:cellRepeater dataSource="pageScope.itemArray" rows="4"
                         verticalRepeat="true" tableClass="table" cellClass="altCell" alternatingCellClass="cell">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>4x5 (vert) with <%= itemArray.length %> items</b><br/>
<netui-data:cellRepeater dataSource="pageScope.itemArray" rows="2"
                         verticalRepeat="true" tableClass="table" cellClass="altCell" alternatingCellClass="cell">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>4x5 (vert) with <%= itemArray.length %> items</b><br/>
<netui-data:cellRepeater dataSource="pageScope.itemArray" columns="2"
                         verticalRepeat="true" tableClass="table" cellClass="altCell" alternatingCellClass="cell">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<b>4x5 (vert) with <%= itemArray.length %> items</b><br/>
<netui-data:cellRepeater dataSource="pageScope.itemArray" columns="2" 
                         verticalRepeat="false" tableClass="table" cellClass="altCell" alternatingCellClass="cell">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<br/>
<br/>
  </body>
</html>
