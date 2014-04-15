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
        This test will apply a couple of attributes to individual nodes.  These are only applied to the nodes themselves
        and not to the descedents.  All nodes are marked. To verify this test you simply run the JSP and then look at
        the attributes applied to the Label Anchor. There should be an 'a', 'b', or 'c' attribute next to each of the
        node that indicate they should have the attribute set <b>x.x.x [attributes]</b>.
        To verify this test you must look at the source and see that the attributes match the
        expected attributes defined in the label.
        </p>
        <hr style="clear:left">
        <div class="content">
        <netui:tree dataSource="pageFlow.tree1" selectionAction="postback" tagId="tree">
            <netui:treeItem expanded="true">
                <netui:treeLabel>0 [A]</netui:treeLabel>
                <netui:treeHtmlAttribute attribute="a" value="**A**" onSelectionLink="true"/>
                <netui:treeItem expanded="true">
                    <netui:treeLabel>0.0 [A,B]</netui:treeLabel>
                    <netui:treeHtmlAttribute attribute="a" value="**A**" onSelectionLink="true"/>
                    <netui:treeHtmlAttribute attribute="b" value="**B**" onSelectionLink="true" />
                    <netui:treeItem expanded="true">
                        <netui:treeLabel>0.0.0</netui:treeLabel>
                        <netui:treeItem>
                            <netui:treeLabel>0.0.0.0 [C]</netui:treeLabel>
                            <netui:treeHtmlAttribute attribute="c" value="**C**" onSelectionLink="true" />
                        </netui:treeItem>
                        <netui:treeItem><netui:treeLabel>0.0.0.1</netui:treeLabel></netui:treeItem>
                        <netui:treeItem>
                            <netui:treeLabel>0.0.0.2 [C]</netui:treeLabel>
                            <netui:treeHtmlAttribute attribute="c" value="**C**" onSelectionLink="true" />
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

  
  
