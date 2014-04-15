<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>results</title>
</head>
<body>
<h2>results</h2>
<p style="color:green">
This displays the contents of the global property <b>sharedFlow.results</b>.  The Lifecycle test calls this PageFlow through
the sharedFlow so that is can capture the onDestroy event within the lifecycle.
</p>
<hr />
<netui:content value="${globalApp.results}"/>
<hr />
</body>
</html>

	


			   
