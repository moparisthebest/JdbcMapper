<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>final property test</title>
</head>
<body>
<br/>
A final string: <b>'<netui:span value="${pageFlow.finalString}"/>'</b>
<br/>
A non-final string: <b>'<netui:span value="${pageFlow.nonFinalString}"/>'</b>
<br />
</body>
</html>
