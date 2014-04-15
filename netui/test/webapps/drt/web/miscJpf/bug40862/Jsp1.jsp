<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>MiscJpf Bug40862</title>
   </head>
   <body>
      <h3 align="center">MiscJpf Bug40862 - Jsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <br/>
         <h2><font color="blue">Testing repeater with MiscJpf</font></h2>
         <br/><br/>

         <netui-data:repeater dataSource="pageFlow.strArr">
             <netui-data:repeaterItem>
                 <netui:span value="${container.item}" />
                 <netui-data:getData resultId="item" value="${container.item}"/>
                 <%
                     String currentItem = (String) pageContext.getAttribute("item");
                     //System.out.println(currentItem);
                 %>
                 <br/>
             </netui-data:repeaterItem>
         </netui-data:repeater>


         <br/><br/>
         <netui:anchor action="finish">Finish...</netui:anchor>
      </center>
   </body>
</html>
