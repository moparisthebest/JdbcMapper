<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Action Chaining
        </title>
    </head>
    <body>
        <h3>Action Chaining</h3>
        
        <netui:anchor action="action1a">pageflow-scoped action -> non-pageflow-scoped action</netui:anchor>
        <br>
        <netui:anchor action="action2a">non-pageflow-scoped action -> pageflow-scoped action</netui:anchor>
        <br>
        <netui:anchor action="action3a">pageflow-scoped action -> non-pageflow-scoped action, with form explicitly passed</netui:anchor>
        <br>
        <netui:anchor action="action4a">non-pageflow-scoped action -> pageflow-scoped action, with form explicitly passed</netui:anchor>
        <br>
        <netui:anchor action="action5a">pass an incompatible form</netui:anchor>
    </body>
</netui:html>
