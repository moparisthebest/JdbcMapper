<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>JpfScopedForms Test49</title>
   </head>
   <body>
      <h3 align="center">JpfScopedForms Test49 - Jsp3.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <h3>
            Testing page flow scoped forms.
         </h3>
         <br/>
         <netui:form action="action3">
            <font color="blue">
               String 1: <netui:textBox dataSource="actionForm.string1"/>
            </font>
            <br/>

            <font color="blue">
               String 2: <netui:textBox dataSource="actionForm.string2"/>
            </font>
            <br/><br/>
            <font color="green">
               Do not change any value just press Continue...
            </font>
            <br/><br/>
            <netui:button type="submit">Continue...</netui:button>
         </netui:form>
      </center>
   </body>
</html>
