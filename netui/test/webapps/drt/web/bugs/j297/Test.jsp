<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<netui:html>
<head>
    <title>Dynamic Tree with RunAtClient and ExpandOnClient
    </title>
</head>
<netui:body>
<h4>Verify Action Override</h4>
<p style="color:green">Verify that the action on a treeItem
overrides the inheritted action and is not applied to the children.
</p>
<netui:anchor action="reset">Reset</netui:anchor><br>
Action: ${pageFlow.action}<br>
<!--Begin scriptContainer-->
<div style="border: thin solid;height: 300px;">
<netui:scriptContainer>
    <netui:scriptHeader/>
    <netui:tree dataSource="pageFlow.root" escapeForHtml="true"
        selectionAction="select" tagId="tree" runAtClient="true"
	iconRoot="/coreWeb/tree/images"
        selectedStyle="background-color: #FFD185; font-color: #FFFFFF; text-decoration: none;"
	unselectedStyle="text-decoration: none"
    >
            <netui:treeItem expanded="true">
                <netui:treeLabel>0</netui:treeLabel>
                <netui:treeItem expanded="true">
                    <netui:treeLabel>0.0 [sts]</netui:treeLabel>
                    <netui:treePropertyOverride selectionAction="subTreeSelect" />
                    <netui:treeItem action="selectOverride" expanded="true">
                        <netui:treeLabel>0.0.0 [O]</netui:treeLabel>
                        <netui:treeItem icon="alien.gif">0.0.0.0 [sts]</netui:treeItem>
                        <netui:treeItem icon="alien.gif">0.0.0.1 [sts]</netui:treeItem>
                    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true" action="selectOverride">
                    <netui:treeLabel>0.1 [0]</netui:treeLabel>
                    <netui:treeItem icon="cool.gif">0.1.0</netui:treeItem>
                    <netui:treeItem icon="cool.gif">0.1.1</netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true"><netui:treeLabel>0.2</netui:treeLabel>
                    <netui:treeItem icon="cool.gif" action="selectOverride">0.2.0 [O]</netui:treeItem>
                    <netui:treeItem icon="cool.gif">0.2.1</netui:treeItem>
		</netui:treeItem>
            </netui:treeItem>
    </netui:tree>
</netui:scriptContainer>
</div>
<!--End scriptContainer-->
   </netui:body>
</netui:html>
