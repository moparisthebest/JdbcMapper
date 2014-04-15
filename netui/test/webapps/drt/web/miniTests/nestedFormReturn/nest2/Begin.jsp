<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Nest Two</title>
</head>
<body>
<h4>Nest Two</h4>
<netui:form action="/submit">
Type <netui:textBox dataSource="actionForm.type"/></br>
<netui:button type="submit">Submit</netui:button>
</netui:form>
</body>
</html>
