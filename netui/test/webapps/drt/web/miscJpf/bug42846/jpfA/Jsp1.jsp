<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>MiscJpf Bug42846</title>
   </head>
   <body>
      <h3 align="center">MiscJpf Bug42846 - Jsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <br/>
         <h2><font color="blue">Testing pageflow scope</font></h2>
         Change the values below then submit.  Make sure that the changes do not
         effect the other browser window.
         <br/><br/>
         <netui:form action="action1">
            <netui:textBox dataSource="pageFlow.field1" />
            <br/>
            <netui:textBox dataSource="pageFlow.field2" />
            <br/><br/>
            <netui:button action="action1" value="Action1" type="submit"/>
         </netui:form>
         <br/><br/>
         <netui:anchor action="finish">Finish</netui:anchor>
      </center>
   </body>
</html>
