<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Format bug</title>
</head>
<body>
<h4>Format bug</h4>
<netui:span value="${pageFlow.letters}">
        <netui:formatString pattern="$##-#####-#" truncate="true"/>
</netui:span>
<br />
<netui:span value="${pageFlow.letters}">
        <netui:formatString pattern="$$#-#####-#" truncate="true"/>
</netui:span>
<hr />
<b>Should be</b><br />
#A-BCDEF-G<br />
$A-BCDEF-G<br />
</body>
</html>
