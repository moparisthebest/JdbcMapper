<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<netui:html>
<head>
    <title>Multiple Attributes</title>
</head>
<netui:body>
<h4>Multiple Attributes</h4>
<p style="color:green">This will set multiple custom attributes on an 
a set of tree elements.
</p>
<netui:anchor action="reset">Reset</netui:anchor><br>
Action: ${pageFlow.action}<br>
<!--Begin scriptContainer-->
<div style="border: thin solid;height: 300px;">
<netui:scriptContainer>
    <netui:scriptHeader/>
    <netui:tree dataSource="pageFlow.root"
        selectionAction="select" tagId="tree" runAtClient="true"
        selectedStyle="background-color: #FFD185; font-color: #FFFFFF; text-decoration: none;"
	unselectedStyle="text-decoration: none"
        iconRoot="/coreWeb/tree/images"
    >
      <netui:attribute name="tree1" value="tree1"/>
      <netui:attribute name="tree2" value="tree2"/>
      <netui:treeItem expanded="true">
        <netui:treeHtmlAttribute attribute="a" value="A" onDiv="true"/>
        <netui:treeHtmlAttribute attribute="b" value="B" onDiv="true"/>
	<netui:treeLabel>0</netui:treeLabel>
        <netui:treeItem expanded="false">
           <netui:treeLabel>0.0</netui:treeLabel>
           <netui:treeItem expanded="false">
              <netui:treeLabel>0.0.0</netui:treeLabel>
           </netui:treeItem>
        </netui:treeItem>
        <netui:treeItem expanded="false">
            <netui:treeLabel>0.1</netui:treeLabel>
            <netui:treeItem icon="cool.gif">0.1.0</netui:treeItem>
            <netui:treeItem icon="cool.gif">0.1.1</netui:treeItem>
        </netui:treeItem>
        <netui:treeItem expanded="false">
            <netui:treeLabel>0.2</netui:treeLabel>
            <netui:treeItem icon="cool.gif">0.2.0</netui:treeItem>
            <netui:treeItem icon="cool.gif">0.2.1</netui:treeItem>
        </netui:treeItem>
       </netui:treeItem>
    </netui:tree>

</netui:scriptContainer>
</div>
<!--End scriptContainer-->
   </netui:body>
</netui:html>
