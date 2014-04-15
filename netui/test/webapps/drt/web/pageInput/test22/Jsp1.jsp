<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>PageInput Test22</title>
   </head>
   <body>
      <h3 align="center">PageInput Test22 - Jsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <br/>
         <netui-data:declarePageInput name="pgInput1" type="java.lang.String" />

         PageInput value1:
         <font color="blue">
            <netui:textBox dataSource="pageInput.pgInput1" />
         </font>
         <br/><br/>
         <netui:anchor action="finish">Finish...</netui:anchor>
         <br/><br/>
         <font color="green">
            Try to change the value in the textBox then press Finish.
         </font>
      </center>
   </body>
</html>
