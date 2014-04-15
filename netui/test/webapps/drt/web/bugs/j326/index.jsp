<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui:html>
    <head>
        <title>
            Netui Tree Requirements
        </title>
    </head>
    <netui:body>
<H3>Mixed Content</H3>
<netui:anchor action="resetTrees">Reset Trees</netui:anchor><BR />
<p style="color:green">This test verifies that labels defined in bodies don't
actually get rendered if the treeItem also contains children.  This is a 
a mixed content situation, and we don't allow it.  Instead, the tree needs
to use treeLabel to define it's labels.  Below you should only see labels for TreeItem1.1.1.  All other labels should be blank.
</p>
Action: ${pageFlow.action}<br>
<netui:tree dataSource="pageFlow.selectExpandJspTree1"
	selectionAction="myTreeAction" expansionAction="myTreeAction"
	tagId="selectExpandJspTree1" >
   <netui:treeItem>TreeItem1
      <netui:treePropertyOverride selectionAction="mySelectionAction"
		expansionAction="myExpansionAction" />
      <netui:treeItem>TreeItem1.1
        <netui:treeItem>TreeItem1.1.1</netui:treeItem>
      </netui:treeItem>
   </netui:treeItem>
</netui:tree>

<BR />

</netui:body>

</netui:html>       