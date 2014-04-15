<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="formBean.test1.FormBeanTest1Form1" %>


<html>
   <head>
      <title>FormBean Test1</title>
   </head>
   <body>
      <h3 align="center">FormBean Test1 - Jsp2.jsp</h3>
      <hr width="95%"/>
      <br/>
      <font color="blue">
         <h3 align="center">Struts jsp page.</h3>
      </font>
      <center>
         Field1 Value:&nbsp;
         <%
            FormBeanTest1Form1  foo = (FormBeanTest1Form1) request.getSession().getAttribute("formBeanTest1Form1");
            out.write(foo.getField1());
         %>
         <br/><br/>
         <a href="/coreWeb/formBeanTest1/strutsAction2.do">Continue</a>
      </center>
   </body>
</html>
