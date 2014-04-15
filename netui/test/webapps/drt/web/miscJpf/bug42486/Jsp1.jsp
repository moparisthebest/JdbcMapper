<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>MiscJpf Bug42486</title>
   </head>
   <body>
      <h3 align="center">MiscJpf Bug42486 - Jsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <br/>
         <h2><font color="blue">Testing infinite loop</font></h2>
         <br/><br/>

         <netui-data:repeater dataSource="pageFlow.foo">
            <netui-data:repeaterItem>
                <netui-data:getData value="${container.item.key}" resultId="curVal"/>
                 <br/>
            </netui-data:repeaterItem>
         </netui-data:repeater>

         <br/><br/>
         <netui:anchor action="finish">Finish...</netui:anchor>
      </center>
   </body>
</html>
