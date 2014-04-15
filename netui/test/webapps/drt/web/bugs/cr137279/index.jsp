<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <body>
        <p style="color:#339900;">This test demonstrates two bad action errors within a &lt;netui:tree>.  There are
        three trees.  The first is a working tree.  The second has a bad selectionAction in the &lt;tree>
        tag.  The third has a bad action the first &lt;node> contained inside the tree.  The second
        and third should result in errors being displayed.
        <hr>
        <netui:tree selectionAction="begin"  dataSource="pageFlow.tree" tagId="treeOne">
            <netui:treeItem>
                <netui:treeLabel>1.0</netui:treeLabel>
                <netui:treeItem>1.1</netui:treeItem>
                <netui:treeItem>1.2</netui:treeItem>
            </netui:treeItem>
        </netui:tree>
        <hr>
        <h4>Tree Action Error</h4>
        <netui:tree selectionAction="badAction" dataSource="pageFlow.treeTwo" tagId="treeTwo">
            <netui:treeItem>
                <netui:treeLabel>1.0</netui:treeLabel>
                <netui:treeItem>1.1</netui:treeItem>
                <netui:treeItem>1.2</netui:treeItem>
            </netui:treeItem>
        </netui:tree>
        <hr>
        <h4>TreeNode Action Error</h4>
        <netui:tree selectionAction="begin" dataSource="pageFlow.treeThree" tagId="treeThree">
            <netui:treeItem expanded="true" >
                <netui:treeLabel>1.0</netui:treeLabel>
                <netui:treeItem action="badAction">1.1</netui:treeItem>
                <netui:treeItem>1.2</netui:treeItem>
            </netui:treeItem>
        </netui:tree>
        <hr>
    </body>
</netui:html>

  
