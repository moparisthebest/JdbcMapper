<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Simple JSP -&gt; JPF Test</title>
  </head>
  <body>
    <b>Call Page Flow Tests</b><br/>
    <netui-data:callPageFlow resultId="value" method="nullTest">
        <netui-data:methodParameter value="${pageScope.noSuchValue}" null="${pageScope.noSuchValue == null}"/>
    </netui-data:callPageFlow>
    <br/>
    <br/>
    Test result: <netui:span value="${pageScope.value}"/><br/>
    <br/>
    <br/>
<%--
    <netui-data:callPageFlow resultId="value" method="nullTest">
        <netui-data:methodParameter value="${pageScope.noSuchValue}"/>
    </netui-data:callPageFlow>
--%>
  </body>
</html>
