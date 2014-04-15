<%@ page language="java" %>
<%@ page import="org.apache.beehive.netui.util.HtmlExceptionFormatter" %>
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
<b>Expected Failures</b><br/>
<% try{ %>
<netui-data:cellRepeater dataSource="pageScope.nullArray" rows="-42" columns="5">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<%}catch(Exception e){out.write(HtmlExceptionFormatter.format("Expected failure, rows < 0", e, false));}%>
<hr/>
<% try { %>
<br/>
<netui-data:cellRepeater dataSource="pageScope.itemArray"
                         verticalRepeat="false" tableClass="table" cellClass="altCell" alternatingCellClass="cell">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<% }catch(Exception e)
{out.write(HtmlExceptionFormatter.format("Expected failure, missing rows and columns", e, false));} %>
<hr/>
<%try{%>
<netui-data:cellRepeater dataSource="pageScope.itemArray" rows="${pageScope.badRowsStr}"
                         columns="${pageScope.columnsStr}" 
                         tableClass="table" cellClass="cell" alternatingCellClass="altCell">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<br/>
<%}catch(Exception e){out.write(HtmlExceptionFormatter.format("Expected failure, rows expression evaluates to NaN", e, false));}
%>
<hr/>
<%try{%>
<br/>
<netui-data:cellRepeater dataSource="pageScope.nullArray" rows="5" columns="-42">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<%}catch(Exception e){out.write(HtmlExceptionFormatter.format("Expected failure, columns < 0", e, false));}%>
<hr/>
<% try{ %>
<netui-data:cellRepeater dataSource="sharedFlow.root.noCellRepeaterProperty" rows="-42" columns="5">
    Item: <netui-html:span value="${container.item}"/>
</netui-data:cellRepeater>
<%}catch(Exception e){out.write(HtmlExceptionFormatter.format("Expected failure, rows < 0", e, false));}%>
  </body>
</html>
