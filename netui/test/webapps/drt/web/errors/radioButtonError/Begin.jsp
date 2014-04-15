<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Radio Tests</title>
</head>
<body>
<h4>Radio</h4>
<netui:form action="/postback">
<netui:radioButtonOption value="${pageFlow.radio1}"/><br />
<netui:button type="submit">Submit</netui:button>
</netui:form>
<br />
Selected: <netui:span value="${pageFlow.radioSelect}" />
</body>
</html>
