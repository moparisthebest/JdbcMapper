<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>ValFwdErr Test58</title>
   </head>
   <body>
      <h3 align="center">ValFwdErr Test58 - Jsp2.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <h3><font color="blue">
            Testing addValidationError Method
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
               The first time you visit this page you should see an error
               message below repeated twice. Don't change any of the values just
               press Continue.  You will come back to this page with a different
               error message. If this does not happen the test has failed. To
               end the test just change both of the values above and press
               Continue and you will go to the finish page.
            </font>
            <br/><br/>
            <netui:button type="submit">Continue...</netui:button>
         </netui:form>
         <br/><br/>
         <netui:error key="changeError"/>
         <netui:error key="addError"/>
         <br/><br/>
         <netui:errors/>
         <hr width="95%"/>
      </center>
   </body>
</html>
