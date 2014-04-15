<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<netui:html idScope="html">
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <p style="color:green">This will output javascript using the
	ScriptBlock and the script will appear inline.
        </p>
	<netui:label tagId="forceJavaScript" value="Force Framework JavaScript"/>
        <hr>
<netui:scriptBlock placement="inline">
    document.write("<p><b>This is from the first inline element</b></p>");
</netui:scriptBlock>

        <p>This is content from the page</p>

<netui:scriptBlock placement="inline">
    document.write("<p><b>This is from the second inline element</b></p>");
</netui:scriptBlock>
        <hr>
    </netui:body>
</netui:html>

  
