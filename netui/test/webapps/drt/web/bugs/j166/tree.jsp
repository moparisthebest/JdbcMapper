<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h4>Page Two</h4>
        <p style="color: green">
        </p>
	<netui:anchor action="begin">Page One</netui:anchor>&nbsp;
	<netui:anchor action="clearTree">Clear Tree</netui:anchor>
	<hr>
        <netui:tree dataSource="pageFlow.tree" selectionAction="select" tagId="pageOne">
           <netui:treeItem expanded="true">Item-PageTwo</netui:treeItem>
        </netui:tree>
	<hr>
    </netui:body>
</netui:html>
