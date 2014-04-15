<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>


<br>
<br>
<table border="1">
        <tr>
            <td><i>request #</i></td>
            <td><i>description</i></td>
            <td><i>message from control</i></td>
            <td><i>request</i></td>
            <td><i>response</i></td>
            <td><i>session</i></td>
            <td><i>mapping</i></td>
            <td><i>servlet</i></td>
        </tr>
<netui-data:repeater dataSource="sessionScope.info">
    <netui-data:repeaterItem>
        <tr>
            <td><netui:span value="${container.item.requestNum}"/></td>
            <td><b><netui:span value="${container.item.label}"/></b></td>
            <td><netui:span value="${container.item.ctrlMessage}"/></td>
            <td><netui:span value="${container.item.request}"/></td>
            <td><netui:span value="${container.item.response}"/></td>
            <td><netui:span value="${container.item.session}"/></td>
            <td><netui:span value="${container.item.mapping}"/></td>
            <td><netui:span value="${container.item.servlet}"/></td>
        </tr>
    </netui-data:repeaterItem>
</netui-data:repeater>
</table>
