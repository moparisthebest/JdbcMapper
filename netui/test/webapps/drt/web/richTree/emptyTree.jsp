<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>emptyTree.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <netui:base />
    </head>
    <netui:body>
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>emptyTree.jsp [goEmptyTree.do] </h4>
        <!-- Attribute Information -->
        <div style='float:left;width:300px'>
        <table cellpadding="2" cellspacing="0" border="1" width="300px">
        <tr><th>Creation</th><td>Null TreeNode Root</td></tr>
        <tr><th>tree</th><td>{pageFlow.tree9}</td></tr>
        <tr><th>action</th><td>postback</td></tr>
        <tr><th>runAtClient</th><td>false</td></tr>
        </table>
        </div>
        This binds to a Page Flow variable that is never initialized.  In addition, the JSP page does
        not define &lt;node> elements.  The result is a &lt;tree> that doesn't bind to a data Structure.
        <hr style="clear:left">
        <div class="content">
        <netui:tree dataSource="pageFlow.tree9" selectionAction="postback" tagId="tree" />
        </div>
    </netui:body>
</netui:html>
