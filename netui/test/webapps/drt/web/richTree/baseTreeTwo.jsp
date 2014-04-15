<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>baseTreeTwo.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <netui:base/>
    </head>
    <netui:body>
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>baseTreeTwo.jsp [goStaticBaseTree.do] </h4>
        <!-- Attribute Information -->
        <div style='float:left;width:300px'>
        <table cellpadding="2" cellspacing="0" border="1" width="300px">
        <tr><th>Creation</th><td>Static</td></tr>
        <tr><th>tree</th><td>{pageFlow.tree2}</td></tr>
        <tr><th>action</th><td>postback</td></tr>
        <tr><th>runAtClient</th><td>false</td></tr>
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
        Core test of the tree tag set. The tree is defined in the JSP page.  It sets the as few attributes as possible.  The Tree, selectionAction and 
        TagId are required attributes.  <i>Action</i> must be set so that there can be a postback to the tree.  
        This tree will auto expand/contract in the calling action.  <i>TagId</i> is used to uniquely identify 
        the tree on a page contain other trees.  <i>Tree</i> is used to bind to the tree variable.
        <hr style="clear:left">
        <div class="content">
        <netui:tree dataSource="pageFlow.tree2" selectionAction="postback" tagId="tree">
            <netui:treeItem expanded="true" action="postback">
                <netui:treeLabel>0</netui:treeLabel>
                <netui:treeItem expanded="true" action="postback">
                    <netui:treeLabel>0.0</netui:treeLabel>
                    <netui:treeItem action="postback">
                        <netui:treeLabel>0.0.0</netui:treeLabel>
                        <netui:treeItem action="postback">0.0.0.0</netui:treeItem>
                        <netui:treeItem action="postback">0.0.0.1</netui:treeItem>
                    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true" action="postback">
                    <netui:treeLabel>0.1</netui:treeLabel>
                    <netui:treeItem action="postback">0.1.0</netui:treeItem>
                    <netui:treeItem action="postback">0.1.1</netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true" action="postback">0.2</netui:treeItem>
            </netui:treeItem>
        </netui:tree>
        </div>
    </netui:body>
</netui:html>

  
