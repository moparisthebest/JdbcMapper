<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Errors in the TreeItem</h4>
    <p style="color:green">This test will create a set of errors that are raised by the TreeItem tag.  The
    errors are should be reported with in the tree.
    <br>
    This is a single page test.
    </p>
    <p>
    </p>
    <netui:tree selectionAction="begin" dataSource="pageFlow.tree[0]" tagId="t1" selectionTarget="${pageFlow.nullValue}">
        <netui:treeItem expanded="true"><netui:treeLabel>Null Bindings</netui:treeLabel>
            <netui:treeItem expanded="true"><netui:treeLabel>Errors</netui:treeLabel>
                <netui:treeItem expanded="true" action="begin" href="begin.do">action and href</netui:treeItem>
                <netui:treeItem expanded="true" action="badAction" >badAction</netui:treeItem>
            </netui:treeItem>
        </netui:treeItem>        
    </netui:tree>
    <hr>
    <netui:treeItem expanded="true">Bad Parent</netui:treeItem>
    </netui:body>
</netui:html>

  
