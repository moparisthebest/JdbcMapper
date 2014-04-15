<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Web Application Page
        </title>
        <netui:base />
    </head>
    <body>
    <netui:anchor action="next">
            <netui:parameter name="foo" value="This is a value with spaces and '&' and '?' in it"/>
            Next</netui:anchor>
    </body>
</netui:html>
