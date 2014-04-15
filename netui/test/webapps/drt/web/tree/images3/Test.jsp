<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<netui:html>
<head>
    <title>Replace the spacer gif with another
    </title>
</head>
<netui:body>
<h4>Replace the spacer.gif with another</h4>
<p style="color:green">This test creates a dynamic tree that is both runAtClient and expandOnServer.  The
children are created during the onExpand method.</p>
<netui:anchor action="reset">Reset</netui:anchor>
<!--Begin scriptContainer-->
<div style="border: thin solid;height: 300px;">
<netui:scriptContainer>
    <netui:scriptHeader/>
    <netui:tree dataSource="pageFlow.root" escapeForHtml="true"
        selectionAction="select" tagId="tree" runAtClient="true"
        selectedStyle="background-color: #FFD185; font-color: #FFFFFF; text-decoration: none;"
	unselectedStyle="text-decoration: none"
        imageRoot="/coreWeb/tree/images"
        lastNodeCollapsedImage="plusbottom.gif"
        nodeCollapsedImage="plus.gif"
        lastNodeExpandedImage="minusbottom.gif"
        nodeExpandedImage="minus.gif"
        rootNodeCollapsedImage="rootplus.gif"
        rootNodeExpandedImage="rootminus.gif"
        spacerImage="spacerTwo.gif"
        lineJoinImage="join.gif"
        verticalLineImage="line.gif"
        lastLineJoinImage="joinbottom.gif"
	itemIcon="folder.gif"
    >
            <netui:treeItem expanded="true" action="select">
                <netui:treeLabel>0</netui:treeLabel>
                <netui:treeItem expanded="true" action="select">
                    <netui:treeLabel>0.0</netui:treeLabel>
                    <netui:treeItem expanded="true" action="select">
                        <netui:treeLabel>0.0.0</netui:treeLabel>
                        <netui:treeItem icon="alien.gif" action="select">0.0.0.0</netui:treeItem>
                        <netui:treeItem icon="alien.gif" action="select">0.0.0.1</netui:treeItem>
                    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true" action="select">
                    <netui:treeLabel>0.1</netui:treeLabel>
                    <netui:treeItem icon="cool.gif" action="select">0.1.0</netui:treeItem>
                    <netui:treeItem icon="cool.gif" action="select">0.1.1</netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true" action="select"><netui:treeLabel>0.2</netui:treeLabel>
                    <netui:treeItem icon="cool.gif" action="select">0.2.0</netui:treeItem>
                    <netui:treeItem icon="cool.gif" action="select">0.2.1</netui:treeItem>
		</netui:treeItem>
            </netui:treeItem>
    </netui:tree>

</netui:scriptContainer>
</div>
<!--End scriptContainer-->
   </netui:body>
</netui:html>
