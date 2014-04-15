<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Lifecycle</title>
</head>
<body>
<h4>Lifecycle</h4>
<p style="color:green">
This test will create appends a value to a global variable stored in the sharedFlow for each lifecycle event.
When you go to the results page, it will call an action in sharedFlow which will forward to the <b>results/Controller.jpf</b> which
displays that global variable.
</p>
<netui:anchor action="postback">Postback</netui:anchor>
<netui:anchor action="globalAction_results">Results</netui:anchor>
<hr />
<netui:content value="${pageFlow.lifecycle}"/>
</body>
</html>

	


			   
