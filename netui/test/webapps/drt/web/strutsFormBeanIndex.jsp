<html>
   <head>
     <title>Struts Interop BVT Test Suite</title>
   </head>
   <body>
            <%----------------------------------------------------------------%>
            <%----------------- Struts Form Bean tests -----------------------%>
            <%----------------------------------------------------------------%>
            <h3 align="center">
               <font color="blue">Struts Form Bean Tests</font>
            </h3>
            <table border="1" width="100%" cellspacing="0">
            <%----------------------------------------------------------------%>
            <%---------------------- Table headings --------------------------%>
            <%----------------------------------------------------------------%>
            <tr>
               <th align="left">Test Name</th>
               <th align="left">Test Status</th>
               <th align="left">Test Description</th>
            </tr>

            <%----------------------------------------------------------------%>
            <%-------------------- Struts FormBean Test 1 --------------------%>
            <%----------------------------------------------------------------%>
            <tr>
               <td width="15%" valign=top>
                  <a href="formBean/test1/StartTest.jsp">
                     <img  src="resources/images/bullet.gif" border="0"
                           alt="Run the test"> Test1
                  </a>
               </td>
               <td width="10%" valign=top >
                  Automated
               </td>
               <td>
                  <jsp:include page="formBean/test1/TestDescription.txt"/>
               </td>
            </tr>
            <%----------------------------------------------------------------%>
            <%-------------------- Struts FormBean Test 2 --------------------%>
            <%----------------------------------------------------------------%>
            <tr>
               <td width="15%" valign=top>
                  <a href="formBean/test2/StartTest.jsp">
                     <img  src="resources/images/bullet.gif" border="0"
                           alt="Run the test"> Test2
                  </a>
               </td>
               <td width="10%" valign=top >
                  Automated
               </td>
               <td>
                  <jsp:include page="formBean/test2/TestDescription.txt"/>
               </td>
            </tr>
            </table>
   </body>
</html>
