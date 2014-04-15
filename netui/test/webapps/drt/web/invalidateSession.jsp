<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession"%>

<%
    HttpSession sess = request.getSession( false );
    if ( sess != null ) sess.invalidate();
%>

<html>
    <body>
        OK - invalidated session.
    </body>
</html>
