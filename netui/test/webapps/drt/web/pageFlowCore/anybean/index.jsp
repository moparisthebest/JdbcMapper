<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base/>
        <title>Any Bean Test</title>
    </head>
    <body>
        <h3>Start</h3>

        <netui:anchor action="showSubmit">showSubmit</netui:anchor>
        <br>
        <netui:anchor action="goNested">goNested</netui:anchor>
        <br>
        <netui:anchor action="chain">chain actions</netui:anchor>
    </body>
</netui:html>

  
