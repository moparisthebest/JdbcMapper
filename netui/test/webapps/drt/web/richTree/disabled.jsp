<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>treeStyle.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <netui:base/>
    </head>
    <netui:body style="border: 20pt 5%">
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>disabled.jsp [goDisabled.do] </h4>
        <!-- Attribute Information -->
        <div style='float:left;width:300px'>
        <table cellpadding="2" cellspacing="0" border="1" width="300px">
        <tr><th>Creation</th><td>Static</td></tr>
        <tr><th>tree</th><td>{pageFlow.tree16}</td></tr>
        <tr><th>action</th><td>postback</td></tr>
        <tr><th>runAtClient</th><td>false</td></tr>
        <tr><th>selectionClass</th><td>treeSelected</td></tr>
        <tr><th>disabledClass</th><td>treeDisabled</td></tr>
        <tr><th>treeClass</th><td>treeStyle</td></tr>
        </table>
        </div>
        <!-- Postback information -->
        <div style='float:right;width:250px;border:solid 1pt gray;margin:2 4;'>
        <p style="font-weight:bold;text-align:center;margin: 0;padding:0;">Tree Postback Information</p>
        <table cellpadding='0' cellspacing='2' width="100%">
        <tr><th style="text-align:right" width="100pt">Expand:</th><td><netui:content value="${pageFlow.expand}"/></td></tr>
        <tr><th style="text-align:right" width="100pt">Selection:</th><td><netui:content value="${pageFlow.node}"/></td></tr>
        </table>
        </div>
        Disabling a <b>treeItem</b> means that it is not selectable.  You may still expand it to see children.  Children may be
        expanded and selected.  There is a set of styles that allow a tree to be disabled.  In this case the gray nodes are disabled and 
        may not be selected.
        <hr style="clear:left">
        <div class="content">
        <netui:tree dataSource="pageFlow.tree16" selectionAction="postback" tagId="tree" selectedStyleClass="treeSelected" treeStyleClass="treeStyle" disabledStyleClass="treeDisabled" unselectedStyleClass="treeUnselected">
            <netui:treeItem expanded="true" action="postback">
                <netui:treeLabel>Node 0</netui:treeLabel>
                <netui:treeItem expanded="true">
                    <netui:treeLabel>Node 0.0</netui:treeLabel>
                    <netui:treeItem action="postback">
                        <netui:treeLabel>Node 0.0.0</netui:treeLabel>
                        <netui:treeItem>Node 0.0.0.0</netui:treeItem>
                        <netui:treeItem>Node 0.0.0.1</netui:treeItem>
                        <netui:treeItem>Node 0.0.0.2</netui:treeItem>
                    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true" disabled="true">
                    <netui:treeLabel>Node 0.1</netui:treeLabel>
                    <netui:treeItem>Node 0.1.0</netui:treeItem>
                    <netui:treeItem>Node 0.1.1</netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true">
                    <netui:treeLabel>Node 0.2</netui:treeLabel>
                    <netui:treeItem disabled="true">Node 0.2.0</netui:treeItem>
                    <netui:treeItem>Node 0.2.1</netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true">Node 0.2</netui:treeItem>
            </netui:treeItem>
        </netui:tree>
        </div>
    </netui:body>
</netui:html>
