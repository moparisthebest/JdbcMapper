<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>PageInput Test16</title>
   </head>
   <body>
      <h3 align="center">PageInput Test16 - Jsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <br/>
         <!--
         The case of the pageinput names is what is being tested here.
         Don't fix them.
         -->
         <netui-data:declarePageInput name="name1" type="java.lang.String" />
         <netui-data:declarePageInput name="name2" type="java.lang.String" />
         <netui-data:declarePageInput name="Name2" type="java.lang.String" />
         <netui-data:declarePageInput name="BadName" type="java.lang.String" />

         PageInput value1: <font color="blue">"<netui:span value="${pageInput.name1}" />"</font>
         <br/>
         PageInput value2: <font color="blue">"<netui:span value="${pageInput.name2}" />"</font>
         <br/>
         PageInput value3: <font color="blue">"<netui:span value="${pageInput.Name2}" />"</font>
         <br/>
         PageInput value4: <font color="blue">"<netui:span value="${pageInput.BadName}" />"</font>
         <br/>
         PageInput value5: <font color="blue">"<netui:span value="${pageInput.UndeclaredName}" />"</font>
         <br/><br/>
         <netui

      </center>
   </body>
</html>
