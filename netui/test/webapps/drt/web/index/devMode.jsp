<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.beehive.netui.pageflow.internal.InternalUtils"%>
<html>
<head><title>Production Mode Test</title></head>
<body>
<h4>Production Mode Test</h4>
<%
    boolean sa = InternalUtils.getServerAdapter().isInProductionMode();
    pageContext.setAttribute("prodMode",new Boolean(sa));
%>
<p>
Dev Mode: <b>${!pageScope.prodMode}</b><br>
Production Mode: <b>${pageScope.prodMode}</b>
</p>
</body>
</html>
