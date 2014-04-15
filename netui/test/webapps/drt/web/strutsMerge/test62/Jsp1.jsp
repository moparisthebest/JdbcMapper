<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
    <head>
        <title>Merge Test62</title>
    </head>
    <body>
        <h3 align="center">Merge Test62 - Jsp1.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
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
            <br/>

            String 3:
            <font color="blue">
               <netui:textBox dataSource="actionForm.string3"/>
            </font>
            <br/><br/>
            <netui:button type="submit">Continue...</netui:button>
         </netui:form>
         </center>
    </body>
</html>
