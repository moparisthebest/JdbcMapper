<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%--------------------------------------------------%>
<%-- Displays link for the user to start the test --%>
<%--------------------------------------------------%>
<html>
    <% request.setAttribute("position", "1"); %>
    <jsp:include page="TestHeader.jsp"/>
        <center>
            <a href="Controller1/Jpf1.jpf">Start test</a>
        </center>
    </body>
</html>
