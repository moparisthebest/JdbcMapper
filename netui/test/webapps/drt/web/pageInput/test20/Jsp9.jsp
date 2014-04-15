<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>PageInput Test20</title>
   </head>
   <body>
      <h3 align="center">PageInput Test20 - Jsp9.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <br/>
         <h2><font color="blue">Testing repeater with Integer PageInput</font></h2>
         <br/><br/>
         <netui-data:declarePageInput name="Repeater" type="java.lang.Integer" />
         <netui-data:repeater dataSource="pageInput.Repeater" >
            <netui-data:repeaterItem>
               <netui:content value="${container.item}" />
               &nbsp;&nbsp;--&nbsp;&nbsp;
            </netui-data:repeaterItem>
         </netui-data:repeater >

         <br/><br/>
         <netui:anchor action="action9">Continue...</netui:anchor>
      </center>
   </body>
</html>
