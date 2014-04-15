<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Content and Literal</title>
<style>
.blue {color: blue}
.red {color: red}
</style>
</head>
<body>
<h4>Content And Literal</h4>
<h4>Label With Style Attributes</h4>
text [<netui:span value="${pageFlow.text}" style="color:blue"/>]<br />
text 2 [<netui:span value="${pageFlow.text2}"  style="color:red"/>]<br />
html [<netui:span value="${pageFlow.html}"  style="color:blue"/>]<br />
<h4>Label With Styles</h4>
text [<netui:span value="${pageFlow.text}" styleClass="red"/>]<br />
text 2 [<netui:span value="${pageFlow.text2}" styleClass="blue"/>]<br />
html [<netui:span value="${pageFlow.html}" styleClass="red"/>]<br />
</body>
</html>

	


			   
