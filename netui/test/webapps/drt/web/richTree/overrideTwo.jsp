<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>overrideTwo.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <netui:base/>
    </head>
    <netui:body style="border: 20pt 5%">
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>overrideTwo.jsp [goOverrideTwo.do] </h4>
        <!-- Attribute Information -->
        <div style='float:left;width:300px'>
        <table cellpadding="2" cellspacing="0" border="1" width="300px">
        <tr><th>Creation</th><td>Static</td></tr>
        <tr><th>tree</th><td>{pageFlow.tree18}</td></tr>
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
        <tr><th style="text-align:right" width="100pt">Action:</th><td><netui:content value="${pageFlow.action}"/></td></tr>
        </table>
        </div>
        In this example, we use the <b>treePropertyOverride</b> on the root <b>treeItem</b> to set a different selection action than the
        expansion action set on the <b>tree</b> tag.  This is how you can create two actions, one for expansion and one for selection on a tree.
        <hr style="clear:left">
        <div class="content">
        <netui:tree dataSource="pageFlow.tree18" selectionAction="postback" tagId="tree" selectedStyleClass="treeSelected" treeStyleClass="treeStyle" disabledStyleClass="treeDisabled" unselectedStyleClass="treeUnselected">
            <netui:treeItem expanded="true">
                <netui:treePropertyOverride selectionAction="postbackTwo" />
                <netui:treeLabel>0 [2]</netui:treeLabel>
                <netui:treeItem expanded="true">
                    <netui:treeLabel>0.0 [2]</netui:treeLabel>
                    <netui:treeItem expanded="true">
                        <netui:treeLabel>0.0.0 [2]</netui:treeLabel>
                        <netui:treeItem>0.0.0.0[2]</netui:treeItem>
                        <netui:treeItem action="postbackThree">0.0.0.1 [3]</netui:treeItem>
                        <netui:treeItem>0.0.0.2 [2]</netui:treeItem>
                    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true">
                    <netui:treeLabel>0.1 [2]</netui:treeLabel>
                    <netui:treeItem>0.1.0 [2]</netui:treeItem>
                    <netui:treeItem>0.1.1 [2]</netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true">
                    <netui:treeLabel>0.2 [2]</netui:treeLabel>
                    <netui:treeItem>0.2.0 [2]</netui:treeItem>
                    <netui:treeItem>0.2.1 [2]</netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true">0.2 [2]</netui:treeItem>
            </netui:treeItem>
        </netui:tree>
        </div>
    </netui:body>
</netui:html>
