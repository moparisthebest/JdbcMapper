<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Padding Repeater Errors</title>
</head>
<body>
<h4>Padding Repeater Errors</h4>
<b>Max and min = -1</b>
<db:repeater dataSource="pageFlow.strings">
  <db:repeaterHeader><table border="1" cellpadding="0" cellspacing="0"><tr></db:repeaterHeader>
  <db:repeaterFooter></tr></table></db:repeaterFooter>
  <db:repeaterItem><td width="100pt"><netui:span value="${container.item}"/></td>
     </db:repeaterItem>
  <db:pad maxRepeat="-1" minRepeat="-1"><td width="100pt">&nbsp</td></db:pad>
</db:repeater>
<hr />
<b>max > min</b>
<db:repeater dataSource="pageFlow.strings">
  <db:repeaterHeader><table border="1" cellpadding="0" cellspacing="0"><tr></db:repeaterHeader>
  <db:repeaterFooter></tr></table></db:repeaterFooter>
  <db:repeaterItem><td width="100pt"><netui:span value="${container.item}"/></td>
     </db:repeaterItem>
  <db:pad minRepeat="16" maxRepeat="10"><td width="100pt">&nbsp</td></db:pad>
</db:repeater>
<hr />
</body>
</html>
