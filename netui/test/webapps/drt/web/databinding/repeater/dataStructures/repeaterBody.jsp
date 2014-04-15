<%@ page import="java.util.ArrayList,java.util.List" %>
<!-- 
A generic page that will consume data from some data source 
This page expects the following data structures:

- multiItemDataSource
- singleItemDataSource
- zeroItemDataSource
- nullItemDataSource

 -->
<hr/>
<b>Data Structures</b><br/>
<%
out.write("request.getAttribute(\"multiItemDataSource\"): " +
      (request.getAttribute("multiItemDataSource") == null ? "null" :
      "not null.  type: " + request.getAttribute("multiItemDataSource").getClass().getName() + "<br/>"));
out.write("request.getAttribute(\"singleItemDataSource\"): " +
      (request.getAttribute("singleItemDataSource") == null ? "null" :
      "not null.  type: " + request.getAttribute("singleItemDataSource").getClass().getName() + "<br/>"));
out.write("request.getAttribute(\"zeroItemDataSource\"): " +
      (request.getAttribute("zeroItemDataSource") == null ? "null" :
      "not null.  type: " + request.getAttribute("zeroItemDataSource").getClass().getName() + "<br/>"));
out.write("request.getAttribute(\"nullItemDataSource\"): " +
      (request.getAttribute("nullItemDataSource") == null ? "null" :
      "not null.  type: " + request.getAttribute("nullItemDataSource").getClass().getName() + "<br/>"));
%>
<hr/>    
<p><b>Repeater Test</b>
<table><tr><td><a href="index.jsp">Data Structures Home</a></td></tr></table>
<br>
<h3>Body Content Tests</h3>
<%
List dataSources = new ArrayList();
dataSources.add("multiItemDataSource");
dataSources.add("singleItemDataSource");
dataSources.add("zeroItemDataSource");
dataSources.add("nullItemDataSource");
for(int i = 0; i < dataSources.size(); i++)
{
    String currentSource = (String)dataSources.get(i);
    out.write("<br/><br/><h2>Data Set: " + currentSource + "</h2><br/><br/>");
//     List foo = (List)request.getAttribute(currentSource);
//     if(foo != null)
//     {
//         for(int j = 0; j < foo.size(); j++)
//         {
//             System.out.println("foo[" + j + "]: " + foo.get(j));
//         }
//     }

    if(request.getAttribute(currentSource) != null)
        pageContext.setAttribute("currentSource", request.getAttribute(currentSource));
%>
<h3>Body Contet Tests</h3>
<!-- ====================================================================== -->
<b>Basic Data Source Test -- body content</b><br/>
<table border=1><tr><td><center>Column 1</center></td><td><center>Column 2</center></td><td><center>Column 3</center></td></tr>
<netui-databinding:repeater dataSource="pageScope.currentSource">
        <tr>
            <td><netui-html:span value="${container.item.textProperty}"/></td>
            <td><center><netui-html:span value="${container.item.index}"/></center></td>
            <td><netui-html:textBox dataSource="container.item.index" size="7"/></td>
        </tr>
</netui-databinding:repeater>
<tr><td colspan=3><center>This space intentionally left blank.</center></td></tr>
</table>
<hr/>
<h3>Item Contet Tests</h3>
<!-- ====================================================================== -->
<b>Basic Data Source Test -- items, no header, no footer</b><br/>
<table border=1><tr><td><center>Column 1</center></td><td><center>Column 2</center></td><td><center>Column 3</center></td></tr>
<netui-databinding:repeater dataSource="pageScope.currentSource">
    <netui-databinding:repeaterItem>
        <tr>
            <td><netui-html:span value="${container.item.textProperty}"/></td>
            <td><center><netui-html:span value="${container.item.index}"/></center></td>
            <td><netui-html:textBox dataSource="container.item.index" size="7"/></td>
        </tr>
    </netui-databinding:repeaterItem>
</netui-databinding:repeater>
<tr><td colspan=3><center>This space intentionally left blank.</center></td></tr>
</table>
<!-- ====================================================================== -->
<br/><b>Basic Data Source Test -- items, header, no footer</b><br/>
<netui-databinding:repeater dataSource="pageScope.currentSource">
    <netui-databinding:repeaterHeader>
        <table border=1><tr><td><center>Column 1</center></td><td><center>Column 2</center></td><td><center>Column 3</center></td></tr>
    </netui-databinding:repeaterHeader>
    <netui-databinding:repeaterItem>
        <tr>
            <td><netui-html:span value="${container.item.textProperty}"/></td>
            <td><center><netui-html:span value="${container.item.index}"/></center></td>
            <td><netui-html:textBox dataSource="container.item.index" size="7"/></td>
        </tr>
    </netui-databinding:repeaterItem>
</netui-databinding:repeater>
<tr><td colspan=3><center>This space intentionally left blank.</center></td></tr>
</table>
<!-- ====================================================================== -->
<br/><b>Basic Data Source Test -- items, no header, footer</b><br/>
<table border=1><tr><td><center>Column 1</center></td><td><center>Column 2</center></td><td><center>Column 3</center></td></tr>
<netui-databinding:repeater dataSource="pageScope.currentSource">
    <netui-databinding:repeaterItem>
        <tr>
            <td><netui-html:span value="${container.item.textProperty}"/></td>
            <td><center><netui-html:span value="${container.item.index}"/></center></td>
            <td><netui-html:textBox dataSource="container.item.index" size="7"/></td>
        </tr>
    </netui-databinding:repeaterItem>
    <netui-databinding:repeaterFooter>
        <tr><td colspan=3><center>This space intentionally left blank.</center></td></tr>
        </table>
    </netui-databinding:repeaterFooter>
</netui-databinding:repeater>
<!-- ====================================================================== -->
<br/><b>Basic Data Source Test -- items, header, footer</b><br/>
<netui-databinding:repeater dataSource="pageScope.currentSource">
    <netui-databinding:repeaterHeader>
        <table border=1><tr><td><center>Column 1</center></td><td><center>Column 2</center></td><td><center>Column 3</center></td></tr>
    </netui-databinding:repeaterHeader>
    <netui-databinding:repeaterItem>
        <tr>
            <td><netui-html:span value="${container.item.textProperty}"/></td>
            <td><center><netui-html:span value="${container.item.index}"/></center></td>
            <td><netui-html:textBox dataSource="container.item.index" size="7"/></td>
        </tr>
    </netui-databinding:repeaterItem>
    <netui-databinding:repeaterFooter>
        <tr><td colspan=3><center>This space intentionally left blank.</center></td></tr>
        </table>
    </netui-databinding:repeaterFooter>
</netui-databinding:repeater>
<hr/>    
<h3>ErrorTests</h3></br>
<!-- ====================================================================== -->
<b>Basic Data Source Test -- body content, header, no footer</b><br/>
<netui-databinding:repeater dataSource="pageScope.currentSource">
    <netui-databinding:repeaterHeader>
        <table border=1><tr><td><center>Column 1</center></td><td><center>Column 2</center></td><td><center>Column 3</center></td></tr>
    </netui-databinding:repeaterHeader>
        <tr>
            <td><netui-html:span value="${container.item.textProperty}"/></td>
            <td><center><netui-html:span value="${container.item.index}"/></center></td>
            <td><netui-html:textBox dataSource="container.item.index" size="7"/></td>
        </tr>
</netui-databinding:repeater>
<tr><td colspan=3><center>This space intentionally left blank.</center></td></tr>
</table>
<!-- ====================================================================== -->
<b>Basic Data Source Test -- body content, no header, footer</b><br/>
<table border=1><tr><td><center>Column 1</center></td><td><center>Column 2</center></td><td><center>Column 3</center></td></tr>
<netui-databinding:repeater dataSource="pageScope.currentSource">
        <tr>
            <td><netui-html:span value="${container.item.textProperty}"/></td>
            <td><center><netui-html:span value="${container.item.index}"/></center></td>
            <td><netui-html:textBox dataSource="container.item.index" size="7"/></td>
        </tr>
    <netui-databinding:repeaterFooter>
        <tr><td colspan=3><center>This space intentionally left blank.</center></td></tr>
        </table>
    </netui-databinding:repeaterFooter>
</netui-databinding:repeater>
</table>
<!-- ====================================================================== -->
<b>Basic Data Source Test -- body content, header, footer</b><br/>
<netui-databinding:repeater dataSource="pageScope.currentSource">
    <netui-databinding:repeaterHeader>
        <table border=1><tr><td><center>Column 1</center></td><td><center>Column 2</center></td><td><center>Column 3</center></td></tr>
    </netui-databinding:repeaterHeader>
        <tr>
            <td><netui-html:span value="${container.item.textProperty}"/></td>
            <td><center><netui-html:span value="${container.item.index}"/></center></td>
            <td><netui-html:textBox dataSource="container.item.index" size="7"/></td>
        </tr>
    <netui-databinding:repeaterFooter>
        <tr><td colspan=3><center>This space intentionally left blank.</center></td></tr>
        </table>
    </netui-databinding:repeaterFooter>
</netui-databinding:repeater>
</table>
<%}%>
