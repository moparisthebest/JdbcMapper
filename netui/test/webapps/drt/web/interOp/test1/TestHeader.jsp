<%--------------------------------------------------------------------%>
<%-- Header file that writes the path based on here the user is in  --%>
<%-- test.  Also writes out the full path for the index.jsp page    --%>
<%--------------------------------------------------------------------%>
    <head>
        <title>InterOp Test1</title>
    </head>

    <body>
        <% if (request.getAttribute("position") != "Index") { %>
           <h3>Interop Test1</h3>
           <br/>
        <% } %>

        <% if (request.getAttribute("position") == "1") { %>
            Start.jsp...
        <%} else if (request.getAttribute("position") == "2") { %>
            Start.jsp ... Jpf1.jpf/begin.do -> /interOp/tests/gotoStrutsJSP.do -> Struts1.jsp ...
        <%} else { %>
            Start.jsp ... Jpf1.jpf/begin.do -> /interOp/tests/gotoStrutsJSP.do -> Struts1.jsp ...
                                               gotoJpf2.do -> Jpf2.jpf/begin.do -> Done.jsp
        <% } %>

        <% if (request.getAttribute("position") != "Index") { %>
          <hr width="95%"/>
          <br/>
        <% } %>
