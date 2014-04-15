<%--------------------------------------------------------------------%>
<%-- Header file that writes the path based on here the user is in  --%>
<%-- test.  Also writes out the full path for the index.jsp page    --%>
<%--------------------------------------------------------------------%>
    <head>
        <title>InterOp Test3</title>
    </head>

    <body>
        <% if (request.getAttribute("position") != "Index") { %>
           <h3>Interop Test3</h3>
           <br/>
        <% } %>

        <% if (request.getAttribute("position") == "1") { %>
            Start.jsp...
        <%} else if (request.getAttribute("position") == "2") { %>
            Start.jsp ... JpfNestable.jpf/begin.do -> /interOp/tests/gotoStrutsJSP.do -> Struts1.jsp ...
        <%} else { %>
            Start.jsp ... JpfNestable.jpf/begin.do -> /interOp/tests/gotoStrutsJSP.do -> Struts1.jsp ...
                                               JpfNestable/done.do -> Done.jsp
        <% } %>

        <% if (request.getAttribute("position") != "Index") { %>
          <hr width="95%"/>
          <br/>
        <% } %>
