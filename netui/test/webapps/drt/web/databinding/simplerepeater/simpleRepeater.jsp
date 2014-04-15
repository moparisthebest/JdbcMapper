<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
  <head>
    <title>Simple Repeater Test</title>
  </head>
<%
    String[] strings = {"foo", "bar", "baz", "blee"};
pageContext.setAttribute("strings", strings);
%>
  <body>
    <b>Simple Repeater Test</b>
<br/>
<netui-data:repeater dataSource="pageScope.strings">
    <netui-data:repeaterHeader>
        <table>
    </netui-data:repeaterHeader>
    <netui-data:repeaterItem>
        <tr>
        <td>
             <netui:span value="${container.item}"/>
        </td>
        </tr>
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter>
        </table>
    </netui-data:repeaterFooter>
</netui-data:repeater>

    some random whitespace
    <br/>

    dashes ---- dashes    and dots ... dots

        <br/>

    <hr>
  </body>
</netui:html>
