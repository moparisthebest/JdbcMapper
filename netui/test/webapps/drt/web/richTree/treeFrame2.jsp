<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>treeFrame2.jsp</title>
    </head>
    <netui:body style="border: 20pt 5%;background-color:#ccffcc;">
        <div class="content" style="height:300px">
        <netui:tree dataSource="pageFlow.tree37"
		selectionAction="goSelectFrame"
		expansionAction="goSelectTree"
		tagId="tree"
		selectedStyleClass="treeSelected"
		treeStyleClass="treeStyle" 
                disabledStyleClass="treeDisabled"
		unselectedStyleClass="treeUnselected"
		selectionTarget="contentFrame"
	>
            <netui:treeItem expanded="true" disabled="true">
                <netui:treeLabel>SelectionTarget Test</netui:treeLabel>
                <netui:treeItem target="_top" action="begin">Home</netui:treeItem>
                <netui:treeItem action="goSelectFrameOverride">
                   <netui:treeLabel >Page One</netui:treeLabel>
                   <netui:treeItem>Page One A</netui:treeItem>
                   <netui:treeItem>Page One B</netui:treeItem>
		</netui:treeItem>
                <netui:treeItem>Page Two</netui:treeItem>
                <netui:treeItem>Page Three</netui:treeItem>
            </netui:treeItem>
        </netui:tree>
        </div>
    </netui:body>
</netui:html>
  
