<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>PageInput Test24</title>
   </head>
   <body>
      <h3 align="center">PageInput Test24 - Jsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <br/>
         <h2><font color="blue">Testing PageInput tags</font></h2>
         <br/><br/>
         <netui-data:declarePageInput name="string" type="java.lang.String" />

         PageInput context value:
         <font color="blue">
            <netui:span value="${pageInput.string}" />
         </font>
         <br/><br/>
         PageFlow context value:
         <netui:form action="action1" >
            <font color="blue">
               <netui:textBox dataSource="pageFlow.string" />
               <br/><br/>
               <netui:button action="action1">Continue</netui:button>
            </font>
         </netui:form>
         <br/><br/>
         <font color="green">
            Change the value in the textBox and press continue.
         </font>
      </center>
   </body>
</html>
