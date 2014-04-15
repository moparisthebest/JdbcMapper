<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="temp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<netui:html>
<body>
<h4><netui:span value="${pageFlow.title}"/></h4>
<c:if test="${pageFlow.visible}">
        <p>Text from the Visible Tag - Should See</p>
</c:if>
<c:if test="${!pageFlow.visible}">
        <p>Negated Visible Tag - Should Not See</p>
</c:if>
<c:if test="${pageFlow.notVisible}">
        <p>Not Visible Tag - Should Not See</p>
</c:if>
<c:if test="${!pageFlow.notVisible}">
        <p>Negate Not Visible Tag - Should See</p>
</c:if>
</body>
</netui:html>
