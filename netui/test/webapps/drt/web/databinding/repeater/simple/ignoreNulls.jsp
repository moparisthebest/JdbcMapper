<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Ignore Nulls Test</title>
  </head>

  <body>
    <h1>Ignore Nulls Test</h1>
<br/>
<br/>
<%
pageContext.setAttribute("emptyArray", new String[] {});
pageContext.setAttribute("denseArray", new String[] {"one", "two", "three"});
pageContext.setAttribute("sparseArray", new String[] {"one", null, "two", null, "three", null, null});
%>

<!-- dense array -->
<br/>
<b>denseArray, ignoreNulls=true</b><br/>
<ul>
<netui-data:repeater dataSource="pageScope.denseArray" ignoreNulls="true">
    <li><netui:span value="${container.item}"/></li>
</netui-data:repeater>
</ul>
<br/>
<br/>
<b>denseArray, ignoreNulls=false</b><br/>
<ul>
<netui-data:repeater dataSource="pageScope.denseArray" ignoreNulls="false">
    <li><netui:span value="${container.item}"/></li>
</netui-data:repeater>
</ul>
<br/>

<!-- sparse array -->
<br/>
<b>sparseArray, ignoreNulls=true</b><br/>
<ul>
<netui-data:repeater dataSource="pageScope.sparseArray" ignoreNulls="true">
    <li><netui:span value="${container.item}"/></li>
</netui-data:repeater>
</ul>
<br/>
<br/>
<b>sparseArray, ignoreNulls=false</b><br/>
<ul>
<netui-data:repeater dataSource="pageScope.sparseArray" ignoreNulls="false">
    <li><netui:span value="${container.item}"/></li>
</netui-data:repeater>
</ul>
<br/>


    <hr>
    <address><a href="mailto:ekoneil@bea.com"></a></address>
<!-- Created: Thu Jul 24 13:35:16 Mountain Daylight Time 2003 -->
<!-- hhmts start -->
Last modified: Thu Jul 24 15:12:12 Mountain Daylight Time 2003
<!-- hhmts end -->
  </body>
</html>
