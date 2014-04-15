<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            LongLived Page Flows
        </title>
    </head>
    <body>
        <h3>LongLived Page Flows</h3>
        <netui:anchor action="goNoFrames">without frames</netui:anchor><br>
        <netui:anchor action="goFrames">with frames</netui:anchor>
    </body>
</netui:html>
