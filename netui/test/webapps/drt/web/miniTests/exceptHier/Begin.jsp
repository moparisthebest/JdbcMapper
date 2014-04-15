<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<html>
<head>
<title>Exception Hierarchy</title>
</head>
<body>
<h4>Exception Hierarchy</h3>
<netui:anchor action="throwBase">Throw a ExceptBase - base handler</netui:anchor><br />
<netui:anchor action="throwSub">Throw a ExceptSub - base handler</netui:anchor><br />
<netui:anchor action="throwSub2">Throw a ExceptSub2 - sub2 handler</netui:anchor><br />
<netui:anchor action="throwSubLocal">Throw a ExceptSub with local - local sub handler</netui:anchor><br />
<netui:anchor action="throwRuntime">Throw a Runtime - global handler</netui:anchor><br />
<hr />
Message <netui:span value="${pageFlow.message}"/>
</body>
</html>
