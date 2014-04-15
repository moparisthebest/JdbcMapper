<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui:html>
    <head>
        <title>
            Netui Tree Requirements - runAtClient and expandOnServer
        </title>
    </head>
    <netui:body>

<netui:scriptHeader></netui:scriptHeader>
<h3>Expansion/Contraction Modes II</h3>
<netui:anchor action="resetTrees">Reset Trees</netui:anchor><BR />
<hr />
<div style="height: 600px">
<table width="100%">
<tr><td valign="top" width="50%">
<netui:scriptContainer>
<netui:tree runAtClient="true" dataSource="pageFlow.expandModesDynTree11" selectionAction="postback" tagId="expandModesDynTree11" />
</netui:scriptContainer>
</td><td valign="top" width="50%">
<netui:scriptContainer>
<netui:tree runAtClient="true" dataSource="pageFlow.expandModesJspTree11" selectionAction="postback" tagId="expandModesJspTree11" >
   <netui:treeItem><netui:treeLabel>TreeItem0</netui:treeLabel>
         <netui:treeItem expandOnServer="true"><netui:treeLabel>TreeItem0.1</netui:treeLabel>
               <netui:treeItem><netui:treeLabel>TreeItem0.1.1</netui:treeLabel>
                  <netui:treeItem><netui:treeLabel>TreeItem0.1.1.1</netui:treeLabel></netui:treeItem>
                  <netui:treeItem><netui:treeLabel>TreeItem0.1.1.2</netui:treeLabel></netui:treeItem>
               </netui:treeItem>
               <netui:treeItem expandOnServer="true"><netui:treeLabel>TreeItem0.1.2</netui:treeLabel>
                  <netui:treeItem><netui:treeLabel>TreeItem0.1.2.1</netui:treeLabel></netui:treeItem>
                  <netui:treeItem expandOnServer="true" expanded="true">TreeItem0.1.2.2
                     <netui:treeItem><netui:treeLabel>TreeItem0.1.2.2.1</netui:treeLabel></netui:treeItem>  
                     <netui:treeItem expandOnServer="true"><netui:treeLabel>TreeItem0.1.2.2.2</netui:treeLabel></netui:treeItem>
                     <netui:treeItem><netui:treeLabel>TreeItem0.1.2.2.3</netui:treeLabel></netui:treeItem>
                  </netui:treeItem>
                  <netui:treeItem expanded="true"><netui:treeLabel>TreeItem0.1.2.3</netui:treeLabel>
                     <netui:treeItem><netui:treeLabel>TreeItem0.1.2.3.1</netui:treeLabel></netui:treeItem>  
                     <netui:treeItem expandOnServer="true"><netui:treeLabel>TreeItem0.1.2.3.2</netui:treeLabel>
                        <netui:treeItem expandOnServer="true"><netui:treeLabel>TreeItem0.1.2.3.2.1</netui:treeLabel></netui:treeItem>
                     </netui:treeItem>
                     <netui:treeItem><netui:treeLabel>TreeItem0.1.2.3.3</netui:treeLabel></netui:treeItem>
                  </netui:treeItem>
                  <netui:treeItem expanded="false"><netui:treeLabel>TreeItem0.1.2.4</netui:treeLabel>
                     <netui:treeItem><netui:treeLabel>TreeItem0.1.2.4.1</netui:treeLabel></netui:treeItem>  
                     <netui:treeItem expandOnServer="true"><netui:treeLabel>TreeItem0.1.2.4.2</netui:treeLabel>
                        <netui:treeItem expandOnServer="true" expanded="false"><netui:treeLabel>TreeItem0.1.2.4.2.1</netui:treeLabel>
                            <netui:treeItem><netui:treeLabel>TreeItem0.1.2.4.2.1.1</netui:treeLabel></netui:treeItem>
                        </netui:treeItem>
                     </netui:treeItem>
                     <netui:treeItem expandOnServer="false"><netui:treeLabel>TreeItem0.1.2.4.3</netui:treeLabel>
                        <netui:treeItem><netui:treeLabel>TreeItem0.1.2.4.3.1</netui:treeLabel></netui:treeItem>
                     </netui:treeItem>
                  </netui:treeItem>
               </netui:treeItem>
               <netui:treeItem><netui:treeLabel>TreeItem0.1.3</netui:treeLabel></netui:treeItem>
         </netui:treeItem>
   </netui:treeItem>
</netui:tree>
</netui:scriptContainer>
</td></tr>
</table>
</div>
</netui:body>
</netui:html>
