<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>ReturnTo Test63</title>
    </head>
    <body>
        <h3 align="center">ReturnTo Test63 - Jsp1.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <netui:form action="action2">
               String 1: <netui:textBox dataSource="actionForm.string1" />
               <br/>
               String 2: <netui:textBox dataSource="actionForm.string2" />
               <br/><br/>
               <font color="green">
                  We will visit this page several times.
                  <br/>
                  1st visit just press continue.
                  <br/>
                  2nd visit, change both values then press continue.
                  <br/>
                  3nd visit, just press continue.
                  <br/>
                  4th visit, change both values then press continue.
               </font>
               <br/><br/>
                  <netui:errors/>
               <br/><br/>
               <netui:button>Continue...</netui:button>
            </netui:form>
        </center>
    </body>
</html>
