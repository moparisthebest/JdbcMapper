<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Validation Test</title>
</head>
<body>
<netui:form action="/postback">
Text: <netui:textBox dataSource="actionForm.text"/>
<netui:button type="submit">Submit</netui:button>
</netui:form>
<netui:errors />
</body>
</html>
