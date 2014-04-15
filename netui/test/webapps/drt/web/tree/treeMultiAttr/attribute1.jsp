<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>attribute1.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <netui:base/>
    </head>
    <netui:body>
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>attribute1.jsp [goAttribute1.do/tree1] </h4>
        <p>
        This test verifies that a treeHmtlAttribute tag can apply attributes to the four parts of a treeItem.  The four places are
        the &lt;div> that incloses the item, the &lt;a> that incloses the Icon, the &lt;img> that is the icon and the &lt;a>
        that incloses the label.  You need to verify that the source matches the label.
        </p>
        <hr style="clear:left">
        <div class="content">
        <netui:tree dataSource="pageFlow.tree1" selectionAction="postback" tagId="tree">
            <netui:treeItem expanded="true">
                <netui:treeLabel>0</netui:treeLabel>
                <netui:treeItem expanded="true">
                    <netui:treeLabel>0.0 [d='a', i='a', sl='a']</netui:treeLabel>
                    <netui:treeHtmlAttribute attribute='a' value='A' onIcon="true" onSelectionLink="true" onDiv="true" />
                    <netui:treeItem expanded="true">
                        <netui:treeLabel>0.0.0</netui:treeLabel>
                        <netui:treeItem>
                            <netui:treeLabel>0.0.0.0</netui:treeLabel>
                        </netui:treeItem>
                        <netui:treeItem><netui:treeLabel>0.0.0.1</netui:treeLabel></netui:treeItem>
                        <netui:treeItem>
                            <netui:treeLabel>0.0.0.2</netui:treeLabel>
                        </netui:treeItem>
                    </netui:treeItem>
                   <netui:treeItem expanded="true">
                        <netui:treeLabel>0.0.1</netui:treeLabel>
                        <netui:treeItem><netui:treeLabel>0.0.1.0</netui:treeLabel></netui:treeItem>
                        <netui:treeItem><netui:treeLabel>0.0.1.1</netui:treeLabel></netui:treeItem>
                        <netui:treeItem><netui:treeLabel>0.0.1.2</netui:treeLabel></netui:treeItem>
                    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true">
                    <netui:treeLabel>0.1</netui:treeLabel>
                    <netui:treeItem>0.1.0</netui:treeItem>
                    <netui:treeItem>0.1.1</netui:treeItem>
                </netui:treeItem>
            </netui:treeItem>
        </netui:tree>
        </div>
    </netui:body>
</netui:html>

  
  
