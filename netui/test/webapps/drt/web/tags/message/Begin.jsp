<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<html>
<head>
<title>Message Tags</title>
<netui:base />
</head>
<body>
<netui-data:message value="${bundle.default.MessageStringTwo}" resultId="two" /> 
<netui-data:message value="${bundle.default.MessageStringThree}" resultId="three" /> 
<netui-data:message value="${bundle.default.MessageString}" resultId="baz"> 
    <netui-data:messageArg value="${pageScope.two}"/> 
    <netui-data:messageArg value="${pageScope.three}"/> 
</netui-data:message> 
BAZ: <netui:span value="${pageScope.baz}"/><br>

<netui-data:message value="{1} of {0}" resultId="baz"> 
    <netui-data:messageArg value="${pageScope.two}"/> 
    <netui-data:messageArg value="${pageScope.three}"/> 
</netui-data:message> 
BAZ: <netui:span value="${pageScope.baz}"/>
</body>
</html>
