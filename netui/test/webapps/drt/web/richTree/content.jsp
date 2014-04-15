<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>content.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <style type="text/css">
        .alien {
            position: absolute;
            left: 150pt;
        }
        </style>
        <netui:base/>
    </head>
    <netui:body>
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>content.jsp [goContent.do] </h4>
        <!-- Attribute Information -->
        <div style='float:left;width:300px'>
        <table cellpadding="2" cellspacing="0" border="1" width="300px">
        <tr><th>Creation</th><td>Static</td></tr>
        <tr><th>tree</th><td>{pageFlow.tree12}</td></tr>
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
        This sample demonstrates the use of both the &lt;treeLabel> and the
        &lt;treeContent> tags.  The tree content in this case is the absolutely
        positioned images.
        <hr style="clear:left">
        <div class="content">
        <netui:tree dataSource="pageFlow.tree12" selectionAction="postback" tagId="tree">
            <netui:treeItem expanded="true">
                <netui:treeLabel>0</netui:treeLabel>
                <netui:treeContent><span class='alien'><img src="omg.gif" border="0"></span></netui:treeContent>
                <netui:treeItem expanded="true">
                    <netui:treeLabel>0.0</netui:treeLabel>
                    <netui:treeContent><span class='alien'><img src="alien.gif" border="0"></span></netui:treeContent>
                    <netui:treeItem expanded="true">
                        <netui:treeLabel>0.0.0</netui:treeLabel>
                        <netui:treeContent><span class='alien'><img src="cool.gif" border="0"></span></netui:treeContent>
                        <netui:treeItem>
                            <netui:treeLabel>0.0.0.0</netui:treeLabel>
                            <netui:treeContent><span class='alien'><img src="ninja.gif" border="0"></span></netui:treeContent>
                        </netui:treeItem>
                        <netui:treeItem>
                            <netui:treeLabel>0.0.0.1</netui:treeLabel>
                            <netui:treeContent><span class='alien'><img src="ninja.gif" border="0"></span></netui:treeContent>
                        </netui:treeItem>
                    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem>
                    <netui:treeLabel>0.1</netui:treeLabel>
                    <netui:treeContent><span class='alien'><img src="alien.gif" border="0"></span></netui:treeContent>
                    <netui:treeItem>
                        <netui:treeLabel>0.1.0</netui:treeLabel>
                        <netui:treeContent><span class='alien'><img src="cool.gif" border="0"></span></netui:treeContent>
                    </netui:treeItem>
                    <netui:treeItem>
                        <netui:treeLabel>0.1.1</netui:treeLabel>
                        <netui:treeContent><span class='alien'><img src="cool.gif" border="0"></span></netui:treeContent>
                    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem>
                    <netui:treeLabel>0.2</netui:treeLabel>
                    <netui:treeContent><span class='alien'><img src="alien.gif" border="0"></span></netui:treeContent>
                </netui:treeItem>
            </netui:treeItem>
        </netui:tree>
        </div>
    </netui:body>
</netui:html>

  
  
