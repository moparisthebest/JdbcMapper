<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<netui:html>
    <head>
        <title>J533 Test</title>
    </head>

    <netui:body>
        <p>Simple test of the new requested constructor for 
        TreeHtmlAttributeInfo</p>

        <netui:anchor action="resetTrees">Reset Trees</netui:anchor>

        <p>Dynamic pageFlow tree - images are set in the JSP.  
        TreeHtmlAttributeInfo is also defined in the pageFlow.</p>

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

    </netui:body>

</netui:html>       
