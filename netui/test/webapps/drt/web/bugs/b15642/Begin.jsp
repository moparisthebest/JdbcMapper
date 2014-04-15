<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Update through a repeater</title>
</head>
<body>
<h4>Update Through A Repeater</h4>
<netui:form action="/postback">
Checkbox <netui:checkBox dataSource="actionForm.checked"/><br />
Checkbox PageFlowController <netui:checkBox dataSource="pageFlow.checked"/><br />
<netui:button type="submit">Submit</netui:button>
</netui:form>
<br />
</body>
</html>
