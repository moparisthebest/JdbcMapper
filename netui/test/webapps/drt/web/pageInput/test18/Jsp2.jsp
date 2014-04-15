<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>PageInput Test18</title>
   </head>
   <body>
      <h3 align="center">PageInput Test18 - Jsp2.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <br/>
         <h2><font color="blue">Testing repeater with PageInput</font></h2>
         <br/><br/>
         <netui-data:declarePageInput name="RepeaterB" type="java.lang.String" />

         <netui-data:repeater dataSource="pageInput.RepeaterB" >
            <ul>
               <netui-data:repeaterItem>
                  <li><netui:content value="${container.item}" /></li>
               </netui-data:repeaterItem>
            </ul>
         </netui-data:repeater >

         <br/><br/>
         <netui:anchor action="finish">Finish...</netui:anchor>
      </center>
   </body>
</html>
