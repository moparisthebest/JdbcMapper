<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>


<html>
<head>
<title>Page 1</title>
</head>
<body bgcolor="white">

<h3>Page 1</h3>

<netui:anchor action="doNothing">run an empty action</netui:anchor>
<br>
<netui:anchor action="nest">create a nesting stack</netui:anchor>
<br>
<netui:anchor action="leave">leave this pageflow</netui:anchor>
<br>
<netui:anchor action="clear">clear the history</netui:anchor>

<jsp:include page="noflow/history.jsp"/>

</body>
</html>
