<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Padding Repeater Errors</title>
</head>
<body>
<h4>Padding Repeater Errors</h4>
<b>Binding Errors</b>
<db:repeater dataSource="pageFlow.strings">
  <db:repeaterHeader><table border="1" cellpadding="0" cellspacing="0"><tr></db:repeaterHeader>
  <db:repeaterFooter></tr></table></db:repeaterFooter>
  <db:repeaterItem><td width="100pt"><netui:span value="${container.item}"/></td>
     </db:repeaterItem>
  <db:pad minRepeat="${min}" maxRepeat="${max}" padText="${text}"/>
</db:repeater>
<hr />
</body>
</html>
