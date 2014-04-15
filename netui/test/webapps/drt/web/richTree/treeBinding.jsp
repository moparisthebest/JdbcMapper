<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>treeBinding.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <netui:base />
    </head>
    <netui:body>
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>treeBinding.jsp [goTreeBinding.do] </h4>
        <!-- Attribute Information -->
        <div style='float:left;width:300px'>
        <table cellpadding="2" cellspacing="0" border="1" width="300px">
        <tr><th>Creation</th><td>Binding Error</td></tr>
        <tr><th>tree</th><td>{pageFlow.badPageFlowTree}</td></tr>
        </table>
        </div>
        This binds to a Page Flow variable that doesn't exist. The result is an expression error.
        <hr style="clear:left">
        <div class="content">
        <netui:tree dataSource="pageFlow.badPageFlowTree" selectionAction="postback" tagId="tree" >
            <netui:treeItem expanded="true" action="postback">Tree Root</netui:treeItem>
        </netui:tree>
        </div>
    </netui:body>
</netui:html>
