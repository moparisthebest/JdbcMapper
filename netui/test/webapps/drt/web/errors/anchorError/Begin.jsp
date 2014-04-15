<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<netui:html>
<head>
<title>Anchor Errors</title>
<netui:base />
</head>
<netui:body>
<h4>Anchor Errors</h4>
<p style="color:green">
This test verifies a set of errors that may occur within anchor tags.  There are a bunch of types 
of errors.  Common errors include, calling an action that doesn't exist, setting more than one of
<b>linkName</b>, <b>action</b>, <b>href</b>, and <b>clientAction</b>.  This test only displays
this page.
</p>
<ul>
<li><netui:anchor action="badAction">Navigate</netui:anchor> -- Bad Action </li>
<li><netui:anchor action="begin" href="Begin.jsp">Navigate</netui:anchor> -- Action and Href Defined </li>
<li><netui:anchor action="begin" clientAction="Begin.jsp">Navigate</netui:anchor> -- Action and ClientAction Defined </li>
<li><netui:anchor action="begin" linkName="Foo">Navigate</netui:anchor> -- Action and linName Defined </li>
</netui:body>
</netui:html>
