<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Multi Form Test</title>
</head>
<body>
<netui:form action="/formOne">
<netui:textBox dataSource="actionForm.name" /><br />
<netui:button type="submit" >Submit</netui:button>
</netui:form>
<hr />
<netui:form action="/formTwo">
<netui:textBox dataSource="actionForm.stuff" /><br />
<netui:button type="submit" >Submit</netui:button>
</netui:form>
<br />
Last Action: <netui:span value="${pageFlow.action}"/>
</body>
</html>
