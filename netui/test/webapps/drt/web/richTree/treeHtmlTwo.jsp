<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>treeHtml.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <style type="text/css">
        .leaf {
            font-family: "new century schoolbook", serif ; 
            font-size: 10pt;
            color: #c90000;
        }
        .container {
            font-family: "new century schoolbook", serif ; 
            font-size: 12pt;
            color: #990000;
        }
        </style>
        <netui:base/>
    </head>
    <netui:body>
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>treeHtmlTwo.jsp [goTreeHtmlTwo.do] </h4>
        <!-- Attribute Information -->
        <div style='float:left;width:300px'>
        <table cellpadding="2" cellspacing="0" border="1" width="300px">
        <tr><th>Creation</th><td>Static</td></tr>
        <tr><th>tree</th><td>{pageFlow.tree11}</td></tr>
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
        This is an additional test of specifying the label content through the body content of the &lt;treeItem>
        and &lt;treeLabel> tags.  In this case, there is HTML markup and other netui tags inside the body content.  All expressions will
        only be evaluated when the tree is created, which happens the first time when the tree variable is null.  Otherwise the tree
        is generated from the TreeNodes.
        <hr style="clear:left">
        <div class="content">
        <netui:tree dataSource="pageFlow.tree11" selectionAction="postback" tagId="tree">
            <netui:treeItem expanded="true">
                <netui:treeLabel><span class="container"><netui:content value="${pageFlow.containerPrefix}"/>&nbsp;<b>0</b></span></netui:treeLabel>
                <netui:treeItem expanded="true">
                    <netui:treeLabel><span class="container"><netui:content value="${pageFlow.containerPrefix}"/>&nbsp;<b>0.0</b></span></netui:treeLabel>
                    <netui:treeItem expanded="true">
                        <netui:treeLabel><span class="container"><netui:content value="${pageFlow.containerPrefix}"/>&nbsp;<b>0.0.0</b></span></netui:treeLabel>
                        <netui:treeItem><span class="leaf"><netui:content value="${pageFlow.leafPrefix}"/>&nbsp;<b>0.0.0.0</b></span></netui:treeItem>
                        <netui:treeItem><span class="leaf"><netui:content value="${pageFlow.leafPrefix}"/>&nbsp;<b>0.0.0.1</b></span></netui:treeItem>
                    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem>
                    <netui:treeLabel><span class="container"><netui:content value="${pageFlow.containerPrefix}"/>&nbsp;<b>0.1</b></span></netui:treeLabel>
                    <netui:treeItem><span class="leaf"><netui:content value="${pageFlow.leafPrefix}"/>&nbsp;<b>0.1.0</b></span></netui:treeItem>
                    <netui:treeItem><span class="leaf"><netui:content value="${pageFlow.leafPrefix}"/>&nbsp;<b>0.1.1</b></span></netui:treeItem>
                </netui:treeItem>
                <netui:treeItem><span class="container"><netui:content value="${pageFlow.containerPrefix}"/>&nbsp;<b>0.2</b></span></netui:treeItem>
            </netui:treeItem>
        </netui:tree>
        </div>
    </netui:body>
</netui:html>

  
  
