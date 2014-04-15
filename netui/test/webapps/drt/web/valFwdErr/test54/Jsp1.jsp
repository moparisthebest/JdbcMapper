<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>ValFwdErr Test54</title>
   </head>
   <body>
      <h3 align="center">ValFwdErr Test54 - Jsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <h3><font color="blue">
            Testing Validation Error Forward
         </font></h3>
         <br/>
         <netui:form action="action1">
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
               1) Please don't change any values and press Continue.
                 <br/>
                 You will get an error message that says you must change the
                 values.
               <br/><br/>
               2) Change both values and press Continue.
                <br/>
                You will be taken to page Jsp2.jsp.
               <br/><br/>
               3) After page Jsp2.jsp you will come back to this page for a
               third time.  When you do, enter "exit" in String 1 and just change
               the value in String 2 ot any value then press Continue.
            </font>
            <br/><br/>
            <netui:button type="submit">Continue...</netui:button>
         </netui:form>

         <netui:errors/>
         <hr width="95%"/>
      </center>
   </body>
</html>
