<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Previous Page Info</title>
</head>
<body>
<h4>Previous Page Info</h4>
<netui:content value="${pageFlow.pageInfo}"/>
Error: <netui:content value="${pageFlow.error}"/>
<hr />
<netui:form action="form">
<netui:textBox dataSource="actionForm.name"/> <br/>
<netui:button type="submit">Submit</netui:button>
</netui:form>
<netui:anchor action="postback">Postback</netui:anchor>
</body>
</html>
