<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<netui:html>
<head>
    <title>Setting an Params on a treeItem</title>
</head>
<netui:body>
<h4>Settiing Params on a TreeItem</h4>
<p style="color:green">This is a test of setting the a parameter on treeItems
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
            <netui:treeItem expanded="true">
                <netui:treeLabel>0</netui:treeLabel>
		<netui:parameter name="treeItemValue" value="From 0"/>
                <netui:treeItem expanded="false" expandOnServer="true">
                    <netui:treeLabel>0.0</netui:treeLabel>
		    <netui:parameter name="treeItemValue" value="From 0.0"/>
                    <netui:treeItem expanded="false">
                        <netui:treeLabel>0.0.0</netui:treeLabel>
                        <netui:parameter name="treeItemValue" value="From 0.0.0"/>
                        <netui:treeItem icon="alien.gif">0.0.0.0
                           <netui:parameter name="treeItemValue" value="From 0.0.0.0"/>
			</netui:treeItem>
                        <netui:treeItem icon="alien.gif">0.0.0.1
                           <netui:parameter name="treeItemValue" value="From 0.0.0.1"/>
			</netui:treeItem>
                    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="false" expandOnServer="true">
                    <netui:treeLabel>0.1</netui:treeLabel>
                        <netui:parameter name="treeItemValue" value="From 0.1"/>
                    <netui:treeItem icon="cool.gif">0.1.0
                        <netui:parameter name="treeItemValue" value="From 0.1.0"/>
		    </netui:treeItem>
                    <netui:treeItem icon="cool.gif">0.1.1
                        <netui:parameter name="treeItemValue" value="From 0.1.1"/>
		    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="false" expandOnServer="true">
                    <netui:treeLabel>0.2</netui:treeLabel>
                        <netui:parameter name="treeItemValue" value="From 0.2"/>
                    <netui:treeItem icon="cool.gif">0.2.0
                        <netui:parameter name="treeItemValue" value="From 0.2.0"/>
		    </netui:treeItem>
                    <netui:treeItem icon="cool.gif">0.2.1
                        <netui:parameter name="treeItemValue" value="From 0.2.1"/>
		    </netui:treeItem>
		</netui:treeItem>
            </netui:treeItem>
    </netui:tree>

</netui:scriptContainer>
</div>
<!--End scriptContainer-->
   </netui:body>
</netui:html>
