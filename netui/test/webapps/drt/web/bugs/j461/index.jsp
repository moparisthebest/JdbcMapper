<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Netui Tree - Tree Creation - basic pageFlow tree
        </title>
    </head>
    <netui:body>
		<netui:anchor action="resetTree">Reset </netui:anchor><BR />
		<netui:anchor action="removeKid">REMOVE CHILD </netui:anchor><br/>

	<p>
		<netui:tree dataSource="pageFlow.productTree" selectionAction="select" tagId="tree1"/>
		
	</p>
	</netui:body>
</netui:html>