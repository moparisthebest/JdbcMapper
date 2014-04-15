<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>attribute3.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <netui:base/>
    </head>
    <netui:body>
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>attribute3.jsp [goAttribute3.do/tree3] </h4>
        <p>
        This test verifies that a multiple attributes can be applied to descendents. You must view source and verify that the label
        names and attributes match.
        </p>
        <hr style="clear:left">
        <div class="content">
        <netui:tree dataSource="pageFlow.tree3" selectionAction="postback" tagId="tree">
            <netui:treeItem expanded="true">
                <netui:treeLabel>0</netui:treeLabel>
                <netui:treeItem expanded="true">
                    <netui:treeLabel>0.0</netui:treeLabel>
                    <netui:treeItem expanded="true">
                        <netui:treeLabel>0.0.0 [d='a', i='a', sl='a']</netui:treeLabel>
                        <netui:treeHtmlAttribute attribute='a' value='A' onIcon="true" onSelectionLink="true" onDiv="true" applyToDescendents="true" />
                        <netui:treeItem>
                            <netui:treeLabel>0.0.0.0 [d='a', i='a', sl='a']</netui:treeLabel>
                        </netui:treeItem>
                        <netui:treeItem><netui:treeLabel>0.0.0.1 [d='a', i='a', sl='a']</netui:treeLabel></netui:treeItem>
                        <netui:treeItem>
                            <netui:treeLabel>0.0.0.2 [d='a', i='a', sl='a']</netui:treeLabel>
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

  
  
