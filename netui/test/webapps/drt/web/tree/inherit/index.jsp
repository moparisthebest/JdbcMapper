<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<netui:html>
<head>
    <title>Dynamic Tree with RunAtClient and ExpandOnClient
    </title>
</head>
<netui:body>
<h4>Dynamic Tree with RunAtClient and ExpandOnClient</h4>
<p style="color:green">
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
                <netui:treeItem expanded="false" expandOnServer="true">
                    <netui:treeLabel>0.0</netui:treeLabel>
                    <netui:treePropertyOverride selectionAction="selectOverride"
		       imageRoot="/coreWeb/tree/images"
                       lastNodeCollapsedImage="plusbottom.gif"
                       nodeCollapsedImage="plus.gif"
                       lastNodeExpandedImage="minusbottom.gif"
                       nodeExpandedImage="minus.gif"
                       lineJoinImage="join.gif"
                       verticalLineImage="line.gif"
                       lastLineJoinImage="joinbottom.gif"
                       itemIcon="folder.gif"
		    />
                    <netui:treeItem expanded="false">
                        <netui:treeLabel>0.0.0</netui:treeLabel>
                        <netui:treeItem icon="alien.gif">0.0.0.0</netui:treeItem>
                        <netui:treeItem icon="alien.gif">0.0.0.1</netui:treeItem>
                    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="false" expandOnServer="true">
                    <netui:treeLabel>0.1</netui:treeLabel>
                    <netui:treeItem icon="cool.gif">0.1.0</netui:treeItem>
                    <netui:treeItem icon="cool.gif">0.1.1</netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="false" expandOnServer="true">
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
