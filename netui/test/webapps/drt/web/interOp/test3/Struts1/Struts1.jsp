<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>

<%-----------------------------------------%>
<%-- Struts jsp page called from a Jpf1  --%>
<%-----------------------------------------%>
<html>
    <% request.setAttribute("position", "2"); %>
    <jsp:include page="../TestHeader.jsp"/>

        <html:form action="/test3/gotoDone">
            <center>
                <html:submit>Goto Done</html:submit>
            </center>
        </html:form>
    </body>
</html>
