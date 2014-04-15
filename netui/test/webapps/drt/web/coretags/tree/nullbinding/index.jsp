<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Null Binding to the Tree tag</h4>
    <p style="color:green">This test binds null to the attributes of the tree.  The first three tests should generate errors
    because these attributes, <b>selectionAction</b>, <b>expansionAction</b> and <b>tagId</b> are all required.  The final
    three tests bind null to the all of the attributes that don't raise errors and are ignored by the tag.
    <br>
    This is a single page test.
    </p>
    <p>
    </p>
    <ul>
    <li><netui:tree selectionAction="${pageFlow.nullValue}" dataSource="pageFlow.tree" tagId="t1" /> -- selectionAction</li>
    <li><netui:tree selectionAction="being" dataSource="pageFlow.tree" tagId="t2" expansionAction="${pageFlow.nullValue}" /> -- expansionAction</li>
    <li><netui:tree selectionAction="being" dataSource="pageFlow.tree" tagId="${pageFlow.nullValue}" /> -- tagId</li>
    </ul>
    <netui:tree selectionAction="begin" dataSource="pageFlow.tree[0]" tagId="t1" selectionTarget="${pageFlow.nullValue}">
        <netui:treeItem expanded="true">selectionTarget</netui:treeItem></netui:tree>
    <netui:tree selectionAction="begin" dataSource="pageFlow.tree[1]" tagId="t1" disabledStyleClass="${pageFlow.nullValue}" disabledStyle="${pageFlow.nullValue}" selectedStyleClass="${pageFlow.nullValue}" selectedStyle="${pageFlow.nullValue}" treeStyleClass="${pageFlow.nullValue}" treeStyle="${pageFlow.nullValue}" unselectedStyleClass="${pageFlow.nullValue}" unselectedStyle="${pageFlow.nullValue}">
        <netui:treeItem expanded="true">class and styles</netui:treeItem></netui:tree>
    <netui:tree selectionAction="begin" dataSource="pageFlow.tree[2]" tagId="t1" itemIcon="${pageFlow.nullValue}" lastNodeExpandedImage="${pageFlow.nullValue}" nodeExpandedImage="${pageFlow.nullValue}" lastNodeCollapsedImage="${pageFlow.nullValue}" nodeCollapsedImage="${pageFlow.nullValue}" lastLineJoinImage="${pageFlow.nullValue}" lineJoinImage="${pageFlow.nullValue}" verticalLineImage="${pageFlow.nullValue}" imageRoot="${pageFlow.nullValue}">
        <netui:treeItem expanded="true">images</netui:treeItem></netui:tree>
    </netui:body>
</netui:html>

  
