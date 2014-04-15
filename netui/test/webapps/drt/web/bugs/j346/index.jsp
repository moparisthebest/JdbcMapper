<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Netui Tree Requirements
        </title>
    </head>
    <netui:body>
<H3>If this text and the tree appears below, the test passes. Normally there is an NPE when you hit this page.</H3>
<BR />
<netui:tree dataSource="pageFlow.tree1" selectionAction="postback" tagId="tree1" >
   <netui:treeItem>Hello.</netui:treeItem>
   <netui:treeItem>Goodbye.</netui:treeItem>                                        
</netui:tree>

</netui:body>
</netui:html>       