<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Begin</title>
</head>
<body>
<h4>Direct Binding without Form</h4>
<p style="color:green">
This test will post a form that binds directly to the page flow.  The action called does not take a form bean
so the only way to post back information is through the form bean.  This is very similar to <b>b13797</b>.
</p>
<netui:form action="/next1">
    <netui:textBox dataSource="pageFlow.string"/>
    <br>
    <netui:button type="submit">Submit</netui:button>
</netui:form>
</body>
</html>
