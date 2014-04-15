<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<netui:html>
<head>
<title>Report Exception</title>
</head>
<netui:body>
<h4>Report Exception</h4>
<p style="color:green">
Report the results of the exception.  The first line below is a message that we added to the page flow when
the exception was caught.  The second two lines are the value of the <b>Exceptions</b> tag.  The first has
the property <b>showMessage</b> set to true.  In the second <b>showMessage</b> is not set, but <b>showStackTrace</b>
is explicitly set to false.  Both lines should be the same.
</p>
<hr>
<b>Page Flow Message:</b> <netui:span value="${pageFlow.message}"/><hr>
<b>Exception Tag Message:</b> <br><netui:exceptions showMessage="true"/><hr>
<b>Exception Tag No StackTrace:</b> <br><netui:exceptions showStackTrace="false"/><hr>
</netui:body>
</netui:html>
