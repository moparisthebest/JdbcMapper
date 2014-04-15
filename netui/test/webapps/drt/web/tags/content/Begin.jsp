<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Content and Literal</title>
</head>
<body>
<h4>Content And Literal</h4>
<h4>Label</h4>
nbsp [<netui:span value="${pageFlow.nbsp}"/>]<br />
& [<netui:span value="${pageFlow.amp}"/>]<br />
< [<netui:span value="${pageFlow.lessThan}"/>]<br />
html [<netui:span value="${pageFlow.html}"/>]<br />
text [<netui:span value="${pageFlow.text}"/>]<br />
<h4>Content</h4>
nbsp [<netui:content value="${pageFlow.nbsp}"/>]<br />
& [<netui:content value="${pageFlow.amp}"/>]<br />
< [<netui:content value="${pageFlow.lessThan}"/>]<br />
html [<netui:content value="${pageFlow.html}"/>]<br />
text [<netui:content value="${pageFlow.text}"/>]<br />
</body>
</html>

	


			   
