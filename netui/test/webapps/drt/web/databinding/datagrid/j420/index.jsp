<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<netui:html>
   <head>
      <title>BEEHIVE-420 Index Page</title>
      <netui:base />
   </head>
   <netui:body>
      <p>
         BEEHIVE-420 Index Page
      </p>
      <p>
         <netui-data:dataGrid dataSource="pageFlow.myData" name="myDataGrid">
            <netui-data:rows>
                <netui-data:repeater dataSource="container.item">
	            <netui-data:spanCell value="${container.container.item.row}"/>
	            <netui-data:spanCell value="${container.container.item.stuff}"/>
                </netui-data:repeater>
            </netui-data:rows>
         </netui-data:dataGrid>
      </p>
   </netui:body>
</netui:html>
