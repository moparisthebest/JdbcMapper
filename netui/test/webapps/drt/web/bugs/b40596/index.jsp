<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
  <head>
    <title>Web Application Page</title>
  </head>
  <body>
<netui:anchor href="index.jsp">
  Link
  <netui:parameter name="foo" value="bar"/>
  <netui:parameter name="123" value="456"/>
  <netui:parameter name="123" value="qwe"/>
  <netui:parameter name="abc" value="def"/>
</netui:anchor>
<br/>
<br/>
<hr/>
<%
java.util.Set set = request.getParameterMap().entrySet();
pageContext.setAttribute("set", set);
%>
<netui-data:repeater dataSource="pageScope.set">

    <netui:span value="${container.item.key}"/><br/>

    <netui-data:repeater dataSource="container.item.value">
        &nbsp;&nbsp;<netui:span value="${container.item}"/><br/>
    </netui-data:repeater>

</netui-data:repeater>
  </body>
</netui:html>
