<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Nested
        </title>
    </head>
    <body>
        <h3>Nested</h3>

        <netui:anchor action="done">exit legitimately</netui:anchor>
        <br>
        <netui:anchor action="breakout">break out</netui:anchor>
        <br>
        <netui:anchor action="nestDeeper">nest deeper</netui:anchor>
        <br>
        <netui:anchor action="utterBreakout">break out of the nesting stack completely</netui:anchor>
    </body>
</netui:html>
