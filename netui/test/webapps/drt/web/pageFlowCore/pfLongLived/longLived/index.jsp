<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            LongLived Page Flow
        </title>
    </head>
    <body>
        <h3>LongLived Page Flow</h3>
        
        <netui:form action="begin">
            page flow val: <netui:textBox dataSource="pageFlow.val" />
            <netui:button value="edit"/>
        </netui:form>
        <br>
        <netui:anchor action="go">go to non-longLived</netui:anchor>
        <br>
        <netui:anchor action="deleteAndGo">delete me, and go to non-longLived</netui:anchor>
        <br>
        <netui:anchor action="deleteAndStay">delete me, and stay here</netui:anchor>
    </body>
</netui:html>
