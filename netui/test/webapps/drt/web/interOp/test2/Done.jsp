<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%------------------------------------------------------------%>
<%-- The last file displayed in the test to show completion --%>
<%------------------------------------------------------------%>
<html>
    <% request.setAttribute("position", "4"); %>
    <jsp:include page="TestHeader.jsp"/>
        <center>
            <h2>Test completed successfully</h2>
        </center>
    </body>
</html>
