<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<netui:html>
<head>
<title>Basic Binding to Global App</title>
</head>
<netui:body>
<h4>Basic Binding to Global App</h4>
<p style="color:green">
This test is a basic test of binding to the SharedFlow.  It binds to the <b>appState</b>  and <b>appInfo</b> properties
in the global app in two different ways.  The first is a direct binding through the EL.  The second
binding is done the page flow.  The page flow exposes a a property that passes through the property
on the global app.  This is a single page test.
</p>
App State: <netui:span value="${globalApp.appState}"/><br>
App State through page flow: <netui:span value="${pageFlow.appInfo}"/>
</netui:body>
</netui:html>
