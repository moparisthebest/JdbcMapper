<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<netui:html>
<head>
    <title>Tree Title
    </title>
</head>
<netui:body>
<h4>Tree Title</h4>
<p style="color:green">Verification of the title attribute on a tree
item.
</p>
<netui:anchor action="reset">Reset</netui:anchor><br>
Action: ${pageFlow.action}<br>
<!--Begin scriptContainer-->
<div style="border: thin solid;height: 300px;">
    <netui:tree dataSource="pageFlow.root"
        selectionAction="select" tagId="tree"
        selectedStyle="background-color: #FFD185; font-color: #FFFFFF; text-decoration: none;"
	unselectedStyle="text-decoration: none"
    >
       <netui:treeItem title="title: 0" expanded="true">
          <netui:treeLabel>0</netui:treeLabel>
          <netui:treeItem title="title: 0.0" expanded="false">
             <netui:treeLabel>0.0</netui:treeLabel>
             <netui:treeItem title="title: 0.0.0" expanded="false">
                <netui:treeLabel>0.0.0</netui:treeLabel>
                <netui:treeItem title="title: 0.0.0.0">0.0.0.0</netui:treeItem>
                <netui:treeItem title="title: 0.0.0.1">0.0.0.1</netui:treeItem>
             </netui:treeItem>
          </netui:treeItem>
          <netui:treeItem title="title: 0.1" expanded="false">
             <netui:treeLabel>0.1</netui:treeLabel>
             <netui:treeItem title="title: 0.1.0">0.1.0</netui:treeItem>
             <netui:treeItem title="title: 0.1.1">0.1.1</netui:treeItem>
          </netui:treeItem>
          <netui:treeItem title="title: 0.2" expanded="false">
             <netui:treeLabel>0.2</netui:treeLabel>
             <netui:treeItem title="title: 0.2.0">0.2.0</netui:treeItem>
             <netui:treeItem title="title: 0.2.1">0.2.1</netui:treeItem>
	  </netui:treeItem>
      </netui:treeItem>
    </netui:tree>
</div>
   </netui:body>
</netui:html>
