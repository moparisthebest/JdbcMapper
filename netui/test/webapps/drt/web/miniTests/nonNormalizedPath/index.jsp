<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Non-normalized Paths
        </title>
    </head>
    <body>
        <netui:anchor action="begin">index.jsp</netui:anchor>
        <br>
        <netui:anchor action="ok1">./index.jsp</netui:anchor>
        <br>
        <netui:anchor action="ok2">../nonNormalizedPath/index.jsp</netui:anchor>
        <br>
        <netui:anchor action="bad1">../../index.jsp (bad)</netui:anchor>
    </body>
</netui:html>
