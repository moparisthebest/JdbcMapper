<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>runAtClient2.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <netui:scriptHeader />
        <netui:base/>
    </head>
    <netui:body>
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>runAtClient2.jsp [goRunAtClient2.do] </h4>
        <!-- Attribute Information -->
        <div style='float:left;width:300px'>
        <table cellpadding="2" cellspacing="0" border="1" width="300px">
        <tr><th>Creation</th><td>Static</td></tr>
        <tr><th>tree</th><td>{pageFlow.tree25}</td></tr>
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
	This is a test of the runAtClient feature where the images
	of the tree differ from the default images.
        <hr style="clear:left">
        <div class="content">
	<netui:anchor action="postback">PostBack</netui:anchor>
	<table width="100%"><tr><td width="50%" valign="top">
        <netui:tree dataSource="pageFlow.tree25" selectionAction="postback"
		runAtClient="true" tagId="treeOne">
            <netui:treeItem expanded="true">
                <netui:treeLabel>0</netui:treeLabel>
                <netui:treeItem>
                    <netui:treeLabel>0.0</netui:treeLabel>
                    <netui:treeItem>
                        <netui:treeLabel>0.0.0</netui:treeLabel>
                        <netui:treeItem>0.0.0.0</netui:treeItem>
                        <netui:treeItem>0.0.0.1</netui:treeItem>
                    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem>
                    <netui:treeLabel>0.1</netui:treeLabel>
                    <netui:treeItem>0.1.0</netui:treeItem>
                    <netui:treeItem>0.1.1</netui:treeItem>
                </netui:treeItem>
                <netui:treeItem>
                    <netui:treeLabel>0.2</netui:treeLabel>
                    <netui:treeItem>0.2.0</netui:treeItem>
		</netui:treeItem>
                <netui:treeItem>
                    <netui:treeLabel>0.3</netui:treeLabel>
                    <netui:treeItem>0.3.0</netui:treeItem>
                    <netui:treeItem>0.3.1</netui:treeItem>
		</netui:treeItem>
            </netui:treeItem>
        </netui:tree>
	</td><td width="50%" valign="top">
        <netui:tree dataSource="pageFlow.tree26" tagId="treeTwo" selectionAction="postback"
		runAtClient="true">
            <netui:treeItem expanded="true">
                <netui:treeLabel>0</netui:treeLabel>
                <netui:treeItem>
                    <netui:treeLabel>0.0</netui:treeLabel>
                    <netui:treeItem>0.0.0</netui:treeItem>
                    <netui:treeItem>0.0.1</netui:treeItem>
                </netui:treeItem>
                <netui:treeItem>
                    <netui:treeLabel>0.1</netui:treeLabel>
                    <netui:treeItem>
                        <netui:treeLabel>0.1.0</netui:treeLabel>
                        <netui:treeItem>0.1.0.0</netui:treeItem>
                        <netui:treeItem>0.1.0.1</netui:treeItem>
                    </netui:treeItem>
                </netui:treeItem>
            </netui:treeItem>
        </netui:tree>
        </td></tr></table>
        </div>
    </netui:body>
</netui:html>

  
