<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
	<h4>Page One</h4>
        <p style="color: green">
        </p>
	<netui:anchor action="tree">Page Two</netui:anchor>&nbsp;
	<netui:anchor action="clearTree">Clear Tree</netui:anchor>
	<hr>
        <netui:tree dataSource="pageFlow.tree" selectionAction="select" tagId="pageTwo">
           <netui:treeItem expanded="true">Item-PageOne</netui:treeItem>
        </netui:tree>
	<hr>
    </netui:body>
</netui:html>

  
