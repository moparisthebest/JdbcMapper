<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Nested Form Return</title>
</head>
<body>
<netui:anchor action="/nest1" >Nest One</netui:anchor><br />
<netui:anchor action="/nest2" >Nest Two</netui:anchor>
<br />
<netui:form action="/done">
Value <netui:textBox dataSource="actionForm.val"/></br>
<netui:button type="submit">Submit</netui:button>
</netui:form>
<br />
Return Information: <netui:span value="${pageFlow.value}"/>
</body>
</html>
