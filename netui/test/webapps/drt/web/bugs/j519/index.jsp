<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html documentType="xhtml1-transitional">
    <head>
        <title>
            Netui Tree Requirements - runAtClient and expandOnServer
        </title>
	<netui:scriptHeader />
    </head>
    <netui:body>
<br />
<p>JSP Tree</p>
<netui:anchor action="resetTrees">Reset Trees</netui:anchor>
<netui:scriptContainer>
<netui:tree runAtClient="true" dataSource="pageFlow.expandModesJspTree11"
	selectionAction="postback" tagId="expandModesJspTree11" >
   <netui:treeItem><netui:treeLabel>TreeItem0</netui:treeLabel>
      <netui:treeItem expandOnServer="true">
         <netui:treeLabel>TreeItem0.1.2.4.2</netui:treeLabel>
 	 <netui:treeContent>EXPAND ME AND WATCH tree item 0.1.2.4.3 become expanded too! (only on JSP tree)</netui:treeContent>
         <netui:treeItem expanded="false">
	    <netui:treeLabel>TreeItem0.1.2.4.2.1</netui:treeLabel>
         </netui:treeItem>
      </netui:treeItem>
      <netui:treeItem>
         <netui:treeLabel>me too?</netui:treeLabel>
         <netui:treeItem><netui:treeLabel>can you see me?</netui:treeLabel>
      </netui:treeItem>
      </netui:treeItem>
   </netui:treeItem>
</netui:tree>
</netui:scriptContainer>
<br /><br />
<p>adding another tree with the datasource pageFlow.expandModesDynTree11 will give the dynamic version of the same tree.  The
dynamic version does not reproduce the error.</p>
</netui:body>
</netui:html>
