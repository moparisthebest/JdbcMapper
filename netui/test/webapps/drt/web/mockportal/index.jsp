<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="mockportal.tld" prefix="mp" %>

<html>
<head>
<title>Mock Portal Tests</title>
</head>

<body>
<h3>Mock Portal Tests</h3>

<netui:anchor href="MockPortal.jsp">basic MockPortal test</netui:anchor><br>
<netui:anchor href="listenTo/ListenToPortal.jsp">tests "listenTo" and exception-handling</netui:anchor><br>
<netui:anchor href="scoping/ScopingController.jpf">tests "jpfScopeID" parameter in portlets</netui:anchor><br>
<netui:anchor href="scoping2/ScopingController.jpf">tests "jpfScopeID" parameter in portlets using netui:anchor tag to launch popup</netui:anchor><br>
<netui:anchor href="unwrapmultipart/index.jsp">test ScopedServletUtils.getOuterRequest() with a MultipartRequestWrapper</netui:anchor><br>
<netui:anchor href="treetest/TreesMockPortal.jsp">trees in MockPortal</netui:anchor><br>

</body>
</html>
