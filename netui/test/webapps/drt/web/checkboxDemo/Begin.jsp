<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Checkbox Demo</title>
</head>
<body>
<h4>Checkbox Demo</h4>
<netui:form action="/postback">
<db:repeater dataSource="pageFlow.fileInfo.files">
<db:repeaterHeader>
<table>
</db:repeaterHeader>
<db:repeaterItem>
<tr><td><netui:span value="${container.item.file}"/></td><td>
<td><netui:checkBox dataSource="container.item.selected"/></td></tr>
</db:repeaterItem>
<db:repeaterFooter>
</table>
</db:repeaterFooter>
</db:repeater>
<br />
<netui:button type="submit">Submit</netui:button>
</netui:form>
<b>Results:</b><br />
<db:repeater dataSource="pageFlow.fileInfo.files">
<db:repeaterHeader>
<ul>
</db:repeaterHeader>
<db:repeaterItem>
    <db:callMethod object="${pageFlow}" method="displayElement" resultId="result">
       <db:methodParameter value="${container.item.selected}"/>
    </db:callMethod>
    <c:if test="${pageScope.result == 'include'}">
       <li><netui:span value="${container.item.file}"/></li>
    </c:if>
</db:repeaterItem>
<db:repeaterFooter>
</ul>
</db:repeaterFooter>
</db:repeater>
</body>
</html>
