<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            JIRA-971: Netui TreeElement - removeChild(), clearChildren()
        </title>
    </head>
    <netui:body>
		<netui:anchor action="resetTree">Reset</netui:anchor><BR />
		<netui:anchor action="removeKid">REMOVE Child</netui:anchor><br/>
		<netui:anchor action="clearFromLeaf">Try clearChildren() on node with no children</netui:anchor><br/>
		<netui:anchor action="clearFromRoot">REMOVE All Children</netui:anchor><br/>

	<p>
		<netui:tree dataSource="pageFlow.productTree" selectionAction="select" tagId="tree1"/>
		
	</p>
	</netui:body>
</netui:html>
