<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>treeFrame.jsp</title>
    </head>
    <netui:body style="border: 20pt 5%;background-color:#ccffcc;">
        <div class="content">
        <netui:tree dataSource="pageFlow.tree19" selectionAction="goTreeFrame" tagId="tree" selectedStyleClass="treeSelected" treeStyleClass="treeStyle" 
                disabledStyleClass="treeDisabled" unselectedStyleClass="treeUnselected"
		selectionTarget="contentFrame">
            <netui:treeItem expanded="true" disabled="true">
                <netui:treeLabel>HREF Test Tree</netui:treeLabel>
                <netui:treeItem action="begin" target="_top">Home</netui:treeItem>
                <netui:treeItem href="PageOne.html">Page One</netui:treeItem>
                <netui:treeItem href="PageTwo.html">Page Two</netui:treeItem>
                <netui:treeItem href="PageThree.html">Page Three</netui:treeItem>
            </netui:treeItem>
        </netui:tree>
        </div>
    </netui:body>
</netui:html>
  
