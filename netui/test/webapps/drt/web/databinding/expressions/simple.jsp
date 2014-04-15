<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>JSP-only Expression Test</title>
  </head>
<%
    pageContext.setAttribute("stringAttr", "Hello World!");
%>
  <body>
    <h4>Test of Expression Escapes</h4>
    <p style="color:green">This is actually mostly a test of how JSP 2.0 EL escapes can be used to
        output something that looks like an expression.  In the first case we display the
        expression and then the value of that expression.  In the second we output an evaluated
        expression and the results of that expression.
    </p>
<br>
<netui:span value="\${pageScope.stringAttr}"/>:
<netui:span value="${pageScope.stringAttr}"/><br>
<hr>
<netui:span value="\${'\${'}\${pageScope.stringAttr}}"/>:
<netui:span value="${'${'}${pageScope.stringAttr}}"/><br>
<br/>
<br/>
  </body>
</html>
