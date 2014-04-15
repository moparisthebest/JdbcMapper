<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<netui:html>
<head>
    <title>Tree Title
    </title>
</head>
<netui:body>
<h4>Tree Title</h4>
<p style="color:green">Verification of the title attribute on a tree
item.  In this case the title should become the alt text on the itemIcon
because the label contains HTML markup.
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
          <netui:treeLabel><b>0</b></netui:treeLabel>
          <netui:treeItem title="title: 0.0" expanded="false">
             <netui:treeLabel><b>0.0</b></netui:treeLabel>
             <netui:treeItem title="title: 0.0.0" expanded="false">
                <netui:treeLabel><b>0.0.0</b></netui:treeLabel>
                <netui:treeItem title="title: 0.0.0.0"><b>0.0.0.0</b></netui:treeItem>
                <netui:treeItem title="title: 0.0.0.1"><b>0.0.0.1</b></netui:treeItem>
             </netui:treeItem>
          </netui:treeItem>
          <netui:treeItem title="title: 0.1" expanded="false">
             <netui:treeLabel><b>0.1</b></netui:treeLabel>
             <netui:treeItem title="title: 0.1.0"><b>0.1.0</b></netui:treeItem>
             <netui:treeItem title="title: 0.1.1"><b>0.1.1</b></netui:treeItem>
          </netui:treeItem>
          <netui:treeItem expanded="false">
             <netui:treeLabel><b>0.2</b></netui:treeLabel>
             <netui:treeItem><b>0.2.0</b></netui:treeItem>
             <netui:treeItem><b>0.2.1</b></netui:treeItem>
	  </netui:treeItem>
      </netui:treeItem>
    </netui:tree>
</div>
   </netui:body>
</netui:html>
