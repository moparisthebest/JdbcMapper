<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>attribute4.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <netui:base/>
    </head>
    <netui:body>
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>attribute4.jsp [goAttribute4.do/tree4] </h4>
        <hr style="clear:left">
        <p>
        This test has an attribute that applied to the tree and overridden by an element only
        attribute on a single tag.  The attributes are applied to the Label Anchor and match the
        value on the label.  You must look at the source to verify.
        </p>
        <div class="content">
        <netui:tree dataSource="pageFlow.tree4" selectionAction="postback" tagId="tree">
            <netui:treeItem expanded="true">
                <netui:treeLabel>0</netui:treeLabel>
                <netui:treeItem expanded="true">
                    <netui:treeLabel>0.0 [A]</netui:treeLabel>
                    <netui:treeHtmlAttribute attribute="a" value="**A**" onSelectionLink="true" applyToDescendents="true" />
                    <netui:treeItem expanded="true">
                        <netui:treeLabel>0.0.0 [-A-]</netui:treeLabel>
                        <netui:treeHtmlAttribute attribute="a" value="--A--" onSelectionLink="true"/>
                        <netui:treeItem><netui:treeLabel>0.0.0.0 [A]</netui:treeLabel></netui:treeItem>
                        <netui:treeItem><netui:treeLabel>0.0.0.1 [A]</netui:treeLabel></netui:treeItem>
                        <netui:treeItem><netui:treeLabel>0.0.0.2 [A]</netui:treeLabel></netui:treeItem>
                    </netui:treeItem>
                   <netui:treeItem expanded="true">
                        <netui:treeLabel>0.0.1 [A]</netui:treeLabel>
                        <netui:treeItem><netui:treeLabel>0.0.1.0 [A]</netui:treeLabel></netui:treeItem>                        <netui:treeItem><netui:treeLabel>0.0.1.1 [A]</netui:treeLabel></netui:treeItem>
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

  
  
