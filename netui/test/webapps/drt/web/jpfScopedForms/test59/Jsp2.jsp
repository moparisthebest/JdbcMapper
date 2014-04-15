<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>JpfScopedForms Test59</title>
   </head>
   <body>
      <h3 align="center">JpfScopedForms Test59 - Jsp2.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <h3>
            Testing page flow scoped forms.
         </h3>
         <br/>
         <netui:form action="action1">
            <font color="blue">
               String 1: <netui:span value="${actionForm.string1}"/>
            </font>
            <br/>

            <font color="blue">
               String 2: <netui:span value="${actionForm.string2}"/>
            </font>
            <br/><br/>
            <font color="green">
               The values should be the values you entered on page 1.  If they
               are not the test has failed.
            </font>
         </netui:form>
         <br/><br/>
         <netui:anchor action="finish">Finish...</netui:anchor>

      </center>
   </body>
</html>
