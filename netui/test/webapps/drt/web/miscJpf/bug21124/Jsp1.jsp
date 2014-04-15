<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<html>
    <head>
        <title>MiscJpf Bug 21124 test</title>
    </head>
    <body>
        <h3 align="center">MiscJpf Bug 21124 test - Jsp1.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <netui:anchor action="act1">throw Wrapped Checked</netui:anchor>
            <br/><br/>
            <netui:anchor action="act2">throw Wrapped Runtime</netui:anchor>
            <br/><br/>
            <netui:anchor action="act3">throw Unwrapped Runtime</netui:anchor>
            <br/><br/>
            <netui:anchor action="act4">throw Wrapped Error</netui:anchor>
            <br/><br/>
            <netui:anchor action="act5">throw Unwrapped Error</netui:anchor>
            <br/><br/>
            <netui:anchor action="act6">throw Wrapped, unspecified Runtime</netui:anchor>
            <br/><br/>
            <netui:anchor action="act7">throw Unwrapped, unspecified Runtime</netui:anchor>
            <br/><br/>
            <netui:anchor action="done">Done</netui:anchor>
        </center>
    </body>
</html>
