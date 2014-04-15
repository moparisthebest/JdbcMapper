<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>writeTreeError.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <netui:base/>
    </head>
    <netui:body>
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>writeTreeError.jsp [goWriteTreeError.do] </h4>
        <!-- Attribute Information -->
        <div style='float:left;width:300px'>
        <table cellpadding="2" cellspacing="0" border="1" width="300px">
        <tr><th>Creation</th><td>Static</td></tr>
        <tr><th>tree</th><td>{pageFlow.tree7}</td></tr>
        <tr><th>action</th><td>postback</td></tr>
        <tr><th>runAtClient</th><td>false</td></tr>
        </table>
        </div>
        This test demonstrates the error reported when the tree cannot write to the expression being bound to.  This is an error because
        we used the contents of the tree to generate the initial representation, but then didn't have the ability to write it
        to the backing variable.
        <hr style="clear:left">
        <div class="content">
        <netui:tree dataSource="pageFlow.tree7" selectionAction="postback" tagId="tree">
            <netui:treeItem action="postback" expanded="true">
                <netui:treeLabel>0</netui:treeLabel>
                <netui:treeItem action="postback" expanded="true">0.0</netui:treeItem>
                <netui:treeItem action="postback" expanded="true">0.1</netui:treeItem>
                <netui:treeItem action="postback" expanded="true">0.2</netui:treeItem>
            </netui:treeItem>
        </netui:tree>
        </div>
    </netui:body>
</netui:html>

  

  
 
