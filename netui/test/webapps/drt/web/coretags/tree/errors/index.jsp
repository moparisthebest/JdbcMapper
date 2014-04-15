<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Null Binding to the Tree tag</h4>
    <p style="color:green">This test produces the common set of errors in the tree tag.  Each of the tests
    below should result in an error.  The error is described in the header for each item.
    <br>
    This is a single page test.
    </p>
    <p>
    </p>
    <h4 style="color:green">invalid data source</h4>
    <netui:tree selectionAction="begin" dataSource="pageFlow.badTree" tagId="t1">
        <netui:treeItem expanded="true">BadTree</netui:treeItem></netui:tree>
    <h4 style="color:green">bad selectionAction</h4>
    <netui:tree selectionAction="badAction" dataSource="pageFlow.tree[0]" tagId="t1">
        <netui:treeItem expanded="true">BadTree</netui:treeItem></netui:tree>
    <h4 style="color:green">bad expansionAction</h4>
    <netui:tree selectionAction="begin" dataSource="pageFlow.tree[1]" tagId="t1" expansionAction="badAction">
        <netui:treeItem expanded="true">BadTree</netui:treeItem></netui:tree>     
    <h4 style="color:green">readonly property</h4>
    <netui:tree selectionAction="begin" dataSource="pageFlow.readTree" tagId="t2">
        <netui:treeItem expanded="true">expandsionTarget</netui:treeItem></netui:tree>
    <h4 style="color:green">Bind to String</h4>
    <netui:tree selectionAction="begin" dataSource="pageFlow.string" tagId="t3">
        <netui:treeItem expanded="true">expandsionTarget</netui:treeItem></netui:tree>
    </netui:body>
</netui:html>

  
