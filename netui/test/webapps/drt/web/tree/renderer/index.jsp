<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<netui:html>
<head>
    <title>Extended Tree Renderer</title>
</head>
<netui:body>
    <h4>Extended Tree Renderer for Trees with RunAtClient and ExpandOnClient</h4>
    <p style="color:green">This test uses a custom TreeRenderer for
        trees that both runAtClient and expandOnServer.  The children
        of the dynamic tree are created during the onExpand method
        while the children of the other tree are declared in the JSP.
    </p>
    <p>
        This is just a manual test. To run the test, edit the NetUI config
        file, beehive-netui-config.xml, and the following new element 
        &lt;tree-renderer-class&gt; within the 
        &lt;jsp-tag-config&gt;...&lt;/jsp-tag-config&gt; section.
        It should have the Java class name of the custom TreeRenderer you
        want to test. For example, with the implementation in this test, use...
    </p>
    <code>&lt;tree-renderer-class&gt;tree.renderer.TestTreeRenderer&lt;/tree-renderer-class&gt;

    <br>
    <netui:anchor action="reset">Reset</netui:anchor>

    <hr>
    <table width="100%">
        <tr>
        <td valign="top" width="50%">
        <p>Dynamic Tree</p>
        <netui:scriptContainer>
            <netui:scriptHeader/>
            <netui:tree 
                dataSource="pageFlow.root" escapeForHtml="true"
                selectionAction="select" tagId="dynTree" runAtClient="true"
                selectedStyle="background-color: #FFD185; font-color: #FFFFFF;"/>
        </netui:scriptContainer>
        </td>

        <td valign="top" width="50%">
        <p>JSP Tree</p>
        <netui:scriptContainer>
        <netui:tree 
                dataSource="pageFlow.myJspTree" selectionAction="postback"
                runAtClient="true" tagId="jspTree" >
            <netui:treeItem><netui:treeLabel>TreeItem0</netui:treeLabel>
                <netui:treeItem expandOnServer="true">
                    <netui:treeLabel>TreeItem0.1.2.4.2</netui:treeLabel>
                    <netui:treeContent>CONTENT for this tree item</netui:treeContent>
                    <netui:treeItem expanded="false">
                       <netui:treeLabel>TreeItem0.1.2.4.2.1</netui:treeLabel>
                    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem>
                    <netui:treeLabel>another tree label</netui:treeLabel>
                    <netui:treeItem><netui:treeLabel>can you see me?</netui:treeLabel></netui:treeItem>
                </netui:treeItem>
            </netui:treeItem>
        </netui:tree>
        </netui:scriptContainer>
        </td>
        </tr>
    </table>
    </netui:body>
</netui:html>
