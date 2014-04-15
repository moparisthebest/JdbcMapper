<%@ page language="java" contentType="text/html;charset=EUC_JP"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Test EUC_JP Multibyte Characters and Tags</title>
<netui:base />
</head>
<body>
<h4>Anchor Tests</h4>
<p style="color:green">
This is a set of tests of anchors. The following anchors will navigate to a target page using support for <b>action</b> and <b>href</b> as the primary means of navigation.  
</p>
<p>Multibyte Text</p>
<netui:span value="${pageFlow.foo}"/>

<h2>Navigate To EUC_JP Through...</h2>
<ul>
<li><netui:anchor action="navigateEUCJP">an action
    <netui:parameter name="foo" value="${pageFlow.foo}"/>
    </netui:anchor>
</li>
<li><netui:anchor href="navigateEUCJP.do">an href to the action
    <netui:parameter name="foo" value="${pageFlow.foo}"/>
    </netui:anchor>
</li>
<li><netui:anchor href="eucjp.jsp">an href directly to the JSP
    <netui:parameter name="foo" value="${pageFlow.foo}"/>
    </netui:anchor>
</li>
</ul>

</p>
</body>
</html>
