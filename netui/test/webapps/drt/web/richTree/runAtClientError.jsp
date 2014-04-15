<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>runAtClientError.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <netui:base />
    </head>
    <netui:body>
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>runAtClientError.jsp [goRunAtClientError.do] </h4>
        <!-- Attribute Information -->
        <div style='float:left;width:300px'>
        <table cellpadding="2" cellspacing="0" border="1" width="300px">
        <tr><th>Creation</th><td>Null TreeNode Root</td></tr>
        <tr><th>tree</th><td>{pageFlow.tree23}</td></tr>
        <tr><th>action</th><td>postback</td></tr>
        <tr><th>runAtClient</th><td>false</td></tr>
        </table>
        </div>
        This binds to a Page Flow variable that is never initialized.  In addition, the JSP page does
        not define &lt;node> elements.  The result is a &lt;tree> that doesn't bind to a data Structure.
        <hr style="clear:left">
        <div class="content">
        <netui:tree dataSource="pageFlow.tree23" selectionAction="postback"
		tagId="tree" runAtClient="true">
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
