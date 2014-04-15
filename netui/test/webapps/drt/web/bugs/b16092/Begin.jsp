<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Repeater Error</title>
</head>
<h4>Repeater Error</h4>
<b>With repeaterItem</b><br />
<db:repeater dataSource="pageFlow.checks">
    <db:repeaterItem>
       Value: <netui:span value="${container.item}"/><br />
    </db:repeaterItem>
</db:repeater>
<hr />
<b>Without repeaterItem</b><br />
<db:repeater dataSource="pageFlow.checks">
       Value: <netui:span value="${container.item}"/><br />
</db:repeater>
</body>
</html>

	


			   
