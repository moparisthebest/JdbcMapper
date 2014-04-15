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
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>attribute2.jsp [goAttribute1.do/tree2] </h4>
        <p>
        This is a basic test of applying an attribute that affects the descendents.  The attributes
        are applied to the Label anchor and are indicated in the label.  To verify that the attributes
        are correct you must view the source.  The attributes defined on the Label Anchor should
        match the label name.
        </p>
        <hr style="clear:left">
        <div class="content">
        <netui:tree dataSource="pageFlow.tree2" selectionAction="postback" tagId="tree">
            <netui:treeItem expanded="true">
                <netui:treeLabel>0</netui:treeLabel>
                <netui:treeItem expanded="true">
                    <netui:treeLabel>0.0 [A]</netui:treeLabel>
                    <netui:treeHtmlAttribute attribute="ax" value="**A**" onSelectionLink="true" applyToDescendents="true" />
                    <netui:treeItem expanded="true">
                        <netui:treeLabel>0.0.0 [A,C]</netui:treeLabel>
                        <netui:treeHtmlAttribute attribute="cx" value="**C**" onSelectionLink="true" applyToDescendents="true" />
                        <netui:treeItem><netui:treeLabel>0.0.0.0 [A,C]</netui:treeLabel></netui:treeItem>
                        <netui:treeItem><netui:treeLabel>0.0.0.1 [A,C] </netui:treeLabel></netui:treeItem>
                        <netui:treeItem><netui:treeLabel>0.0.0.2 [A,C]</netui:treeLabel></netui:treeItem>
                    </netui:treeItem>
                   <netui:treeItem expanded="true">
                        <netui:treeLabel>0.0.1 [A]</netui:treeLabel>
                        <netui:treeItem><netui:treeLabel>0.0.1.0 [A]</netui:treeLabel></netui:treeItem>
                        <netui:treeItem>
                        <netui:treeHtmlAttribute attribute="bx" value="**B**" onSelectionLink="true" applyToDescendents="true" />
                            <netui:treeLabel>0.0.1.1 [A,B]</netui:treeLabel>
                        </netui:treeItem>
                        <netui:treeItem><netui:treeLabel>0.0.1.2 [A]</netui:treeLabel></netui:treeItem>
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

  
  
