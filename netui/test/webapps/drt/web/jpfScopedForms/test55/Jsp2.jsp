<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>JpfScopedForms Test55</title>
   </head>
   <body>
      <h3 align="center">JpfScopedForms Test55 - Jsp2.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <h3><font color="blue">
            Testing Page Flow Scoped Forms
         </font></h3>
         <br/>
         <netui:form action="action2">
            String 1:
            <font color="blue">
               <netui:textBox dataSource="actionForm.string1"/>
            </font>
            <br/>

            String 2:
            <font color="blue">
               <netui:textBox dataSource="actionForm.string2"/>
            </font>
            <br/><br/>
            <font color="green">
               Do not change the values above, just press continue.
            </font>
            <br/><br/>
            <netui:button type="submit">Continue...</netui:button>
         </netui:form>

         <netui:errors/>
         <hr width="95%"/>
      </center>
   </body>
</html>
