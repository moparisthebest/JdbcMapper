<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>PageInput Test26</title>
   </head>
   <body>
      <h3 align="center">PageInput Test26 - Jsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <br/>
         <h2><font color="blue">Testing PageInput data-binding</font></h2>
         <br/><br/>
         <netui-data:declarePageInput name="string" type="java.lang.String" />

         <jsp:include page="include1.jsp"/>

         <%@ include file="include2.jsp" %>

         <font color="deeppink">
            PageInput from this jsp: <netui:span value="${pageInput.string}" />
         </font>
         <br/><br/>

         <netui:anchor action="finish">Finish...</netui:anchor>
      </center>
   </body>
</html>
