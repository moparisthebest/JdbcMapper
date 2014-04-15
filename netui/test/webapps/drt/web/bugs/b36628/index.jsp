<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Web Application Page
        </title>
    </head>
    <body>
        <netui:anchor action="throwRT">Throw Runtime Exception</netui:anchor>
        <br>
        <netui:anchor action="throwIA">Throw Illegal Argument Exception</netui:anchor>
        <br>
    </body>
</netui:html>
