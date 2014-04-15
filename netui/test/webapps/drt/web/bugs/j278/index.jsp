<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <p style="color:green">This will not compile because ScriptHeader
	is defined to be empty.</p>
	<netui:scriptHeader>I'm not empty.</netui:scriptHeader> 
    </netui:body>
</netui:html>
