<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>contentAnchor.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <style type="text/css">
        .anchor {
            position: absolute;
            left: 150pt;
        }
        </style>
        <netui:base/>
    </head>
    <netui:body>
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>contentAnchor.jsp [goContentAnchor.do] </h4>
        <!-- Attribute Information -->
        <div style='float:left;width:300px'>
        <table cellpadding="2" cellspacing="0" border="1" width="300px">
        <tr><th>Creation</th><td>Static</td></tr>
        <tr><th>tree</th><td>{pageFlow.tree14}</td></tr>
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
        <tr><th style="text-align:right" width="100pt">Status:</th><td><netui:content value="${pageFlow.status}"/><netui:content value="${param.status}"/></td></tr>
        </table>
        </div>
        This sample demonstrates the use of anchors in the &lt;treeContent>
        tag.  The anchors will navigate cause a postback to the same page.  
        When the anchors are pressed, the Status will be set in the
        Tree Postback Information box.
        <hr style="clear:left">
        <div class="content">
        <netui:tree dataSource="pageFlow.tree14" selectionAction="postback" tagId="tree">
            <netui:treeItem expanded="true">
                <netui:treeLabel>0</netui:treeLabel>
                <netui:treeContent></netui:treeContent>
                <netui:treeItem expanded="true">
                    <netui:treeLabel>0.0</netui:treeLabel>
                    <netui:treeContent>
                        <netui:anchor action="contentPostback" styleClass="anchor">[Content PostBack]
                            <netui:parameter name="status" value="0.0"/>
                        </netui:anchor>
                    </netui:treeContent>
                    <netui:treeItem expanded="true">
                        <netui:treeLabel>0.0.0</netui:treeLabel>
                        <netui:treeContent></netui:treeContent>
                        <netui:treeItem>
                            <netui:treeLabel>0.0.0.0</netui:treeLabel>
                            <netui:treeContent>
                                <netui:anchor action="contentPostbackTwo" styleClass="anchor">[Content PostBack Two]
                                    <netui:parameter name="status" value="0.0.0.0"/>
                                </netui:anchor>
                            </netui:treeContent>
                        </netui:treeItem>
                        <netui:treeItem>
                            <netui:treeLabel>0.0.0.1</netui:treeLabel>
                            <netui:treeContent></netui:treeContent>
                        </netui:treeItem>
                    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem>
                    <netui:treeLabel>0.1</netui:treeLabel>
                    <netui:treeContent></netui:treeContent>
                    <netui:treeItem>
                        <netui:treeLabel>0.1.0</netui:treeLabel>
                        <netui:treeContent></netui:treeContent>
                    </netui:treeItem>
                    <netui:treeItem>
                        <netui:treeLabel>0.1.1</netui:treeLabel>
                        <netui:treeContent></netui:treeContent>
                    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem>
                    <netui:treeLabel>0.2</netui:treeLabel>
                    <netui:treeContent></netui:treeContent>
                </netui:treeItem>
            </netui:treeItem>
        </netui:tree>
        </div>
    </netui:body>
</netui:html>
