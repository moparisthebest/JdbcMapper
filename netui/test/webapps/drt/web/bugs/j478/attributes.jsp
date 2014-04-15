<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>Root Image Testing</title>
        <link href="attributesStyle.css" rel="stylesheet" type="text/css">
    </head>
    <netui:body>
<H3>Root Image Testing</H3>
<netui:anchor action="resetTrees">Reset Trees</netui:anchor>
<p>JSP static tree - Root node images are set correctly</p>
<netui:tree
	rootNodeCollapsedImage="rootplus.gif"
	rootNodeExpandedImage="rootminus.gif"
	itemIcon="alien.gif"
	disabledStyle="color: #00FF7F; font-weight:bold;"
	lastNodeCollapsedImage="plusbottom.gif"
	lastNodeExpandedImage="minusbottom.gif"
	nodeCollapsedImage="plus.gif"
	nodeExpandedImage="minus.gif"
	verticalLineImage="line.gif"
	lineJoinImage="join.gif"
	lastLineJoinImage="joinbottom.gif"
	imageRoot="."
	dataSource="pageFlow.attrJspTree1"
	selectionAction="postback"
	tagId="attrJspTree1">
    <netui:treeItem expanded="true"><netui:treeLabel>My Attribute Tree</netui:treeLabel>
       <netui:treeItem expanded="true"><netui:treeLabel>TreeItem1</netui:treeLabel>
          <netui:treeItem>TreeItem1.1</netui:treeItem>
       </netui:treeItem>
    </netui:treeItem>
</netui:tree>
<br />

<p>Dynamic pageFlow tree - Root node images and all others are set in the JSP.  Root node images do not appear in the rendered HTML but the other lastNode, itemIcon, etc. images do appear as expected</p>
<netui:tree
	rootNodeCollapsedImage="rootplus.gif"
	rootNodeExpandedImage="rootminus.gif"
	itemIcon="alien.gif"
	disabledStyle="color: #00FF7F; font-weight:bold;"
	lastNodeCollapsedImage="plusbottom.gif"
	lastNodeExpandedImage="minusbottom.gif"
	nodeCollapsedImage="plus.gif"
	nodeExpandedImage="minus.gif"
	verticalLineImage="line.gif"
	lineJoinImage="join.gif"
	lastLineJoinImage="joinbottom.gif"
	imageRoot="."
	dataSource="pageFlow.attrDynTree1"
	selectionAction="postback"
	tagId="attrDynTree1" />

<p>None ITreeRootElement used for the root.  Set the root here</p>
<netui:tree
	rootNodeCollapsedImage="rootplus.gif"
	rootNodeExpandedImage="rootminus.gif"
	itemIcon="alien.gif"
	disabledStyle="color: #00FF7F; font-weight:bold;"
	lastNodeCollapsedImage="plusbottom.gif"
	lastNodeExpandedImage="minusbottom.gif"
	nodeCollapsedImage="plus.gif"
	nodeExpandedImage="minus.gif"
	verticalLineImage="line.gif"
	lineJoinImage="join.gif"
	lastLineJoinImage="joinbottom.gif"
	imageRoot="."
	dataSource="pageFlow.attrDynTree3"
	selectionAction="postback"
	tagId="attrDynTree3" >
</netui:tree>
    </netui:body>

</netui:html>       