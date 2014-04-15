<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Databinding from an Anchor</title>
</head>
<body>
<hr />
<netui:form action="submit">
First Name: <netui:textBox dataSource="actionForm.firstName"/><br />
Last Name: <netui:textBox dataSource="actionForm.lastName"/><br />
<netui:button type="submit">Submit</netui:button>
</netui:form>
</body>
</html>
