<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<html>
    <head>
        <netui:base />
    </head>
    <body>
    <h4>Null Binding to the TreeItem tag</h4>
    <p style="color:green">This is a duplicate of the nullbinding error test except that errors are not 
    reported inline.  This means they should be reported by the tree tag.
    <br>
    This is a single page test.
    </p>
    <p>
    </p>
    <netui:tree selectionAction="begin" dataSource="pageFlow.tree[0]" tagId="t1" selectionTarget="${pageFlow.nullValue}">
        <netui:treeItem expanded="true"><netui:treeLabel>Null Bindings</netui:treeLabel>
            <netui:treeItem expanded="true"><netui:treeLabel>Errors</netui:treeLabel>
                <netui:treeItem expanded="true" action="${pageFlow.nullValue}" >action</netui:treeItem>
                <netui:treeItem expanded="true" clientAction="${pageFlow.nullValue}" >clientAction</netui:treeItem>
                <netui:treeItem expanded="true" href="${pageFlow.nullValue}" >href</netui:treeItem>
                <netui:treeItem expanded="true" tagId="${pageFlow.nullValue}" >tagId</netui:treeItem>
            </netui:treeItem>
        </netui:treeItem>        
    </netui:tree>
    </body>
</html>

  
