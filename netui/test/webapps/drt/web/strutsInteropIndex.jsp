<html>
   <head>
     <title>Struts Interop BVT Test Suite</title>
   </head>
   <body>
         <%-------------------------------------------------------------------%>
         <%------------------- Struts Interop Tests --------------------------%>
         <%-------------------------------------------------------------------%>
         <h3 align="center"><font color="blue">Interop Tests</font></h3>
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
            <%---------------------- Test 1 ----------------------------------%>
            <%----------------------------------------------------------------%>
            <tr>
               <td width="15%" valign=top >
                  <a href="interOp/test1/Start.jsp">Test1</a>
               </td>
               <td width="10%" valign=top >
                  Automated
               </td>
               <td>
                  <% request.setAttribute("position", "Index"); %>
                  <jsp:include page="interOp/test1/TestHeader.jsp"/>
               </td>
            </tr>
            <%----------------------------------------------------------------%>
            <%---------------------- Test 2 ----------------------------------%>
            <%----------------------------------------------------------------%>
            <tr>
               <td width="15%" valign=top>
                  <a href="interOp/test2/Start.jsp">Test2</a>
               </td>
               <td width="10%" valign=top >
                  Automated
               </td>
               <td>
                  <% request.setAttribute("position", "Index"); %>
                  <jsp:include page="interOp/test2/TestHeader.jsp"/>
               </td>
            </tr>
            <%----------------------------------------------------------------%>
            <%---------------------- Test 3 ----------------------------------%>
            <%----------------------------------------------------------------%>
            <tr>
               <td width="15%" valign=top>
                  <a href="interOp/test3/Start.jsp">Test3</a>
               </td>
               <td width="10%" valign=top >
                 Automated
               </td>
               <td>
                  <% request.setAttribute("position", "Index"); %>
                  <jsp:include page="interOp/test3/TestHeader.jsp"/>
               </td>
            </tr>
            <%----------------------------------------------------------------%>
            <%---------------------- Test 4 ----------------------------------%>
            <%----------------------------------------------------------------%>
            <tr>
               <td width="15%" valign=top>
                  <a href="interOp/test4/Start.jsp">Test4</a>
               </td>
                <td width="10%" valign=top >
                  Automated
                </td>
                <td>
                  <% request.setAttribute("position", "Index"); %>
                  <jsp:include page="interOp/test4/TestHeader.jsp"/>
                </td>
            </tr>
            <%----------------------------------------------------------------%>
            <%---------------------- Test 5 ----------------------------------%>
            <%----------------------------------------------------------------%>
            <tr>
                <td width="15%" valign=top>
                  <a href="interOp/test5/Start.jsp">Test5</a>
                </td>
                <td width="10%" valign=top >
                  Automated
                </td>
                <td>
                  <% request.setAttribute("position", "Index"); %>
                  <jsp:include page="interOp/test5/TestHeader.jsp"/>
                </td>
            </tr>
       </table>
   </body>
</html>
