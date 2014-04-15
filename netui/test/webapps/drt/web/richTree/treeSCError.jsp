<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<html>
    <head>
        <title>treeSCError.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <netui:base/>
    </head>
    <netui:body>
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>treeSCError.jsp [goTreeSCError.do] </h4>
        <!-- Attribute Information -->
        <div style='float:left;width:300px'>
        <table cellpadding="2" cellspacing="0" border="1" width="300px">
        <tr><th>Creation</th><td>Static</td></tr>
        <tr><th>tree</th><td>{pageFlow.tree32 and 33}</td></tr>
        <tr><th>runAtClient</th><td>true</td></tr>
        <tr><th>action</th><td>postback</td></tr>
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
	In this test, the second tree doesn't appear inside a ScriptContainer
	which is an error because the proper initialization code is written
	out by the container.
        <hr style="clear:left">
        <div class="content">
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
	<tr><td widht="50%" valign="top">
	<netui:scriptContainer generateIdScope="true">
        <netui:scriptHeader />
        <netui:tree dataSource="pageFlow.tree32" selectionAction="postback"
	        selectedStyle="color:red" unselectedStyle="color:blue"
		tagId="tree" renderTagIdLookup="true" runAtClient="true">
            <netui:treeItem tagId="Item0" expanded="true" action="postback">
                <netui:treeLabel>0</netui:treeLabel>
                <netui:treeItem tagId="item00" expanded="true" action="postback">
                    <netui:treeLabel>0.0</netui:treeLabel>
                </netui:treeItem>
                <netui:treeItem expanded="true" action="postback">
                    <netui:treeLabel>0.1</netui:treeLabel>
                    <netui:treeItem action="postback">0.1.0</netui:treeItem>
                    <netui:treeItem action="postback">0.1.1</netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true" action="postback">0.2</netui:treeItem>
            </netui:treeItem>
        </netui:tree>
	</netui:scriptContainer>
	</td><td width="50%" valign="top">
        <netui:tree dataSource="pageFlow.tree33" selectionAction="postback"
	        selectedStyle="color:red" unselectedStyle="color:blue"
		tagId="tree" renderTagIdLookup="true" runAtClient="true">
            <netui:treeItem tagId="Item0" expanded="true" action="postback">
                <netui:treeLabel>0</netui:treeLabel>
                <netui:treeItem tagId="item00" expanded="true" action="postback">
                    <netui:treeLabel>0.0</netui:treeLabel>
                </netui:treeItem>
                <netui:treeItem expanded="true" action="postback">
                    <netui:treeLabel>0.1</netui:treeLabel>
                    <netui:treeItem action="postback">0.1.0</netui:treeItem>
                    <netui:treeItem action="postback">0.1.1</netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true" action="postback">0.2</netui:treeItem>
            </netui:treeItem>
        </netui:tree>
	</td></tr>
	</table>
        </div>
    </netui:body>
<html>
