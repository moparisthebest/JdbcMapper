<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Web Application Page
        </title>
        <style type="text/css">
            .selected {color: #0000cc;}
            .unselected {color: #cccccc;}
        </style>
        
        <!-- This needs to be put here by the script container -->
        <netui:scriptHeader />
    </head>
    <netui:body>
        <table width="100%" cellspacing="5"><tr><td width="20%" valign="top">
        <netui:tree selectionAction="treePost"  
                dataSource="pageFlow.tree" selectedStyleClass="selected"  unselectedStyleClass="unselected" 
                tagId="Tree1" runAtClient="true" >
            <netui:treeItem clientAction='divPanel.showPage("RootPage");' expanded="true">
                <netui:treeLabel>Tree</netui:treeLabel>
                <netui:treeItem clientAction='divPanel.showPage("Page1");' expanded="true">
                    <netui:treeLabel>1</netui:treeLabel>
                    <netui:treeItem clientAction='divPanel.showPage("Page11");' >1.1</netui:treeItem>
                    <netui:treeItem clientAction='divPanel.showPage("Page12");' >1.2</netui:treeItem>
                    <netui:treeItem clientAction='divPanel.showPage("Page13");'  expanded="true">
                        <netui:treeLabel>1.3</netui:treeLabel>
                        <netui:treeItem clientAction='divPanel.showPage("Page131");' >1.3.1</netui:treeItem>
                        <netui:treeItem clientAction='divPanel.showPage("Page132");' >1.3.2</netui:treeItem>
                    </netui:treeItem>
                    <netui:treeItem clientAction='divPanel.showPage("Page14");' >1.4</netui:treeItem>
                </netui:treeItem>
                <netui:treeItem clientAction='divPanel.showPage("Page2");' >2</netui:treeItem>
                <netui:treeItem clientAction='divPanel.showPage("Page3");' >3</netui:treeItem>
            </netui:treeItem>
        </netui:tree>
        </td><td valign="top" width="50%">
                <netui:divPanel tagId="divPanel" >
                    <netui-template:section name="RootPage" ><h4>Root of the Tree<h4></netui-template:section>
                    <netui-template:section name="Page1" ><h4>Page 1</h4></netui-template:section>
                    <netui-template:section name="Page11" ><h4>Page 1.1</h4></netui-template:section>
                    <netui-template:section name="Page12" ><h4>Page 1.12</h4></netui-template:section>
                    <netui-template:section name="Page13" ><h4>Page 1.3</h4></netui-template:section>
                    <netui-template:section name="Page131" ><h4>Page 1.3.1</h4></netui-template:section>
                    <netui-template:section name="Page132" ><h4>Page 1.3.2</h4></netui-template:section>
                    <netui-template:section name="Page14" ><h4>Page 1.4</h4></netui-template:section>
                    <netui-template:section name="Page2" ><h4>Page 2</h4></netui-template:section>
                    <netui-template:section name="Page3" ><h4>Page 3</h4></netui-template:section>
                </netui:divPanel>
        </td></tr></table>
    </netui:body>
</netui:html>
