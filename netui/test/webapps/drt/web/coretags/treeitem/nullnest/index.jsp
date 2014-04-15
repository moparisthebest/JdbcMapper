<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Null Binding to the Nested TreeItems tag</h4>
    <p style="color:green">
    <br>This test binds null to the treeItem.  In this version of the test, the treeItems are nested within 
    each other.  This verifies that error reporting happens correclty within nested trees.
    <br>
    This is a single page test.
    </p>
    <p>
    </p>
    <netui:tree selectionAction="begin" dataSource="pageFlow.tree[0]" tagId="t1" selectionTarget="${pageFlow.nullValue}">
        <netui:treeItem expanded="true"><netui:treeLabel>Null Bindings</netui:treeLabel>
            <netui:treeItem expanded="true"><netui:treeLabel>Errors</netui:treeLabel>
                <netui:treeItem expanded="true" action="${pageFlow.nullValue}" ><netui:treeLabel>Action</netui:treeLabel>
                    <netui:treeItem expanded="true" clientAction="${pageFlow.nullValue}" ><netui:treeLabel>clientAction</netui:treeLabel>
                        <netui:treeItem expanded="true" href="${pageFlow.nullValue}" ><netui:treeLabel>href</netui:treeLabel>
                            <netui:treeItem expanded="true" tagId="${pageFlow.nullValue}" >tagId</netui:treeItem>
            </netui:treeItem>
            </netui:treeItem>
            </netui:treeItem>
            </netui:treeItem>
            </netui:treeItem>
    </netui:tree>
    </netui:body>
</netui:html>

  
