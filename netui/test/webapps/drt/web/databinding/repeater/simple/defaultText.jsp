<%@ page language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui-html"%>
<%
    String[] ary = new String[5];
for(int i = 0; i < ary.length; i++)
{
    ary[i] = "This is String [" + i + "]";
}
pageContext.setAttribute("stringArray", ary);

pageContext.setAttribute("emptyList", new java.util.ArrayList());
pageContext.setAttribute("emptyArray", new String[0]);
pageContext.setAttribute("tenArrayAllNull", new String[10]);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Default Text Test</title>
  </head>
  <body>
    <h1>Default Text Test</h1>
<br/>
<b>defaultText attribute; null dataSource</b><br/>
<netui-data:repeater dataSource="pageScope.nullArray" defaultText="<b>No Data To Display</b>">
  <li><netui-html:span value="${container.item}"/></li>
</netui-data:repeater>
<br/>
<br/>
<b>defaultText attribute; emptyArray (zero length) dataSource</b><br/>
<netui-data:repeater dataSource="pageScope.emptyArray" defaultText="<b>No Data To Display</b>">
  <li><netui-html:span value="${container.item}"/></li>
</netui-data:repeater>
<br/>
<br/>
<b>defaultText attribute; tenArrayAllNull (10 length all null) dataSource</b><br/>
<netui-data:repeater dataSource="pageScope.tenArrayAllNull" defaultText="<b>No Data To Display</b>">
  <li><netui-html:span value="${container.item}"/></li>
</netui-data:repeater>
<br/>
<br/>
<b>defaultText attribute; tenArrayAllNull (10 length all null) dataSource, ignoreNulls</b><br/>
<netui-data:repeater dataSource="pageScope.tenArrayAllNull" ignoreNulls="true" defaultText="<b>No Data To Display</b>">
  <li><netui-html:span value="${container.item}"/></li>
</netui-data:repeater>
<br/>
<br/>
<b>defaultText attribute; header; item; footer; non-null, empty dataSource</b><br/>
<netui-data:repeater dataSource="pageScope.nullArray" defaultText="<b>No Data To Display</b>">
    <netui-data:repeaterHeader><table border=1></netui-data:repeaterHeader>
    <netui-data:repeaterItem><netui-html:span value="${container.item}"/><br/></netui-data:repeaterItem>
    <netui-data:repeaterHeader></table></netui-data:repeaterHeader>
</netui-data:repeater>
<br/>
<br/>
<b>defaultText attribute; non-null (5 length String[]) dataSource</b><br/>
<netui-data:repeater dataSource="pageScope.stringArray" defaultText="<b>No Data To Display</b>">
  <li><netui-html:span value="${container.item}"/></li>
</netui-data:repeater>
<br/>
<br/>
<b>defaultText attribute; emptyArray (zero length) dataSource, header/item/footer</b><br/>
<netui-data:repeater dataSource="pageScope.emptyArray" defaultText="<b>No Data To Display</b>">
    <netui-data:repeaterHeader><ul></netui-data:repeaterHeader>
    <netui-data:repeaterItem><li><netui-html:span value="${container.item}"/></li></netui-data:repeaterItem>
    <netui-data:repeaterFooter></ul></netui-data:repeaterFooter>
</netui-data:repeater>
<br/>
<br/>
<b>defaultText attribute; tenArrayAllNull (10 length all null) dataSource, header/item/footer</b><br/>
<netui-data:repeater dataSource="pageScope.tenArrayAllNull" defaultText="<b>No Data To Display</b>">
    <netui-data:repeaterHeader>HEADER<br/><ul></netui-data:repeaterHeader>
    <netui-data:repeaterItem><li><netui-html:span value="${container.item}"/></li></netui-data:repeaterItem>
    <netui-data:repeaterFooter></ul>FOOTER<br/></netui-data:repeaterFooter>
</netui-data:repeater>
<br/>
<br/>
<b>defaultText attribute; tenArrayAllNull (10 length all null) dataSource, ignoreNulls</b><br/>
<netui-data:repeater dataSource="pageScope.tenArrayAllNull" ignoreNulls="true" defaultText="<b>No Data To Display</b>">
    <netui-data:repeaterHeader>HEADER<br/><ul></netui-data:repeaterHeader>
    <netui-data:repeaterItem><li><netui-html:span value="${container.item}"/></li></netui-data:repeaterItem>
    <netui-data:repeaterFooter></ul>FOOTER<br/></netui-data:repeaterFooter>
</netui-data:repeater>
<br/>
<br/>
</body>
</html>
