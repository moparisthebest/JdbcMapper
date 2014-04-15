<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Pass State betweeen page flows groups</title>
</head>
<body>
<h4>Pass State betweeen page flows groups</h4>
<p style="color:green">
This test passing state between page flows using the SharedFlow.  Each action will append state to the sharedFlow.getPageFlowState
property.  Then a nested page flow is called and will display the same state variable.  The nested page flow will return
to the action or page.  These are different page flow actions.   
</p>
<netui:anchor action="nest" >Nest Page Flow</netui:anchor>
<hr>
Global State:<b><netui:content value="${pageFlow.state}"/></b>
</body>
</html>
