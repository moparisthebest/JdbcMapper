<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<%-----------------------------------------%>
<%-- Struts jsp page called from a Jpf1  --%>
<%-----------------------------------------%>
<html>
    <% request.setAttribute("position", "2"); %>
    <jsp:include page="../TestHeader.jsp"/>

        <netui:form action="toLegacy">
            <center>
                <netui:button>Goto Global.app</netui:button>
            </center>
        </netui:form>
    </body>
</html>
