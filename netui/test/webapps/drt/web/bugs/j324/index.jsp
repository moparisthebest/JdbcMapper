<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui:html>
    <head>
        <title>
            Netui Tree Requirements
        </title>
    </head>
    <netui:body>
<H3>Override expansionAction</H3>
<netui:anchor action="resetTrees">Reset Trees</netui:anchor><BR />
<p style="color:green">This test will verify that a treePropertyOverride
works to override both the selectionAction and expansionAction.
</p>
Action: ${pageFlow.action}<br><br>
<netui:tree dataSource="pageFlow.selectExpandJspTree1" selectionAction="myTreeAction" expansionAction="myTreeAction" tagId="selectExpandJspTree1" >
   <netui:treeItem><netui:treeLabel>TreeItem1</netui:treeLabel>
      <netui:treePropertyOverride selectionAction="mySelectionAction" expansionAction="myExpansionAction" />
      <netui:treeItem><netui:treeLabel>TreeItem1.1</netui:treeLabel>
        <netui:treeItem>TreeItem1.1.1</netui:treeItem>
      </netui:treeItem>
   </netui:treeItem>
</netui:tree>

<BR />

</netui:body>

</netui:html>       