<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Padding Repeater</title>
</head>
<body>
<h4>Padding Repeater</h4>
<b>Repeater to Six</b>
<db:repeater dataSource="pageFlow.strings">
  <db:repeaterHeader><table border="1" cellpadding="0" cellspacing="0"><tr></db:repeaterHeader>
  <db:repeaterFooter></tr></table></db:repeaterFooter>
  <db:repeaterItem><td width="100pt"><netui:span value="${container.item}"/></td>
     </db:repeaterItem>
  <db:pad maxRepeat="6" minRepeat="6"><td width="100pt">&nbsp</td></db:pad>
</db:repeater>
<hr />
<b>Max to three</b>
<db:repeater dataSource="pageFlow.strings">
  <db:repeaterHeader><table border="1" cellpadding="0" cellspacing="0"><tr></db:repeaterHeader>
  <db:repeaterFooter></tr></table></db:repeaterFooter>
  <db:repeaterItem><td width="100pt"><netui:span value="${container.item}"/></td>
     </db:repeaterItem>
  <db:pad maxRepeat="3"><td width="100pt">&nbsp</td></db:pad>
</db:repeater>
<hr />
<b>Min to three</b>
<db:repeater dataSource="pageFlow.strings">
  <db:repeaterHeader><table border="1" cellpadding="0" cellspacing="0"><tr></db:repeaterHeader>
  <db:repeaterFooter></tr></table></db:repeaterFooter>
  <db:repeaterItem><td width="100pt"><netui:span value="${container.item}"/></td>
     </db:repeaterItem>
  <db:pad minRepeat="3"><td width="100pt">&nbsp</td></db:pad>
</db:repeater>
<hr />
<b>Min to six</b>
<db:repeater dataSource="pageFlow.strings">
  <db:repeaterHeader><table border="1" cellpadding="0" cellspacing="0"><tr></db:repeaterHeader>
  <db:repeaterFooter></tr></table></db:repeaterFooter>
  <db:repeaterItem><td width="100pt"><netui:span value="${container.item}"/></td>
     </db:repeaterItem>
  <db:pad minRepeat="6"><td width="100pt">&nbsp</td></db:pad>
</db:repeater>
<hr />
<b>Use the padText Attribute</b>
<db:repeater dataSource="pageFlow.strings">
  <db:repeaterHeader><table border="1" cellpadding="0" cellspacing="0"><tr></db:repeaterHeader>
  <db:repeaterFooter></tr></table></db:repeaterFooter>
  <db:repeaterItem><td width="100pt"><netui:span value="${container.item}"/></td>
     </db:repeaterItem>
  <db:pad minRepeat="6" padText='<td width="100pt">&nbsp;</td>' />
</db:repeater>
<hr />
<b>Databind Min and Text</b>
<db:repeater dataSource="pageFlow.strings">
  <db:repeaterHeader><table border="1" cellpadding="0" cellspacing="0"><tr></db:repeaterHeader>
  <db:repeaterFooter></tr></table></db:repeaterFooter>
  <db:repeaterItem><td width="100pt"><netui:span value="${container.item}"/></td>
     </db:repeaterItem>
  <db:pad minRepeat="${pageFlow.min}" padText="${pageFlow.text}" />
</db:repeater>
<hr />
<b>Databind Max and Text</b>
<db:repeater dataSource="pageFlow.strings">
  <db:repeaterHeader><table border="1" cellpadding="0" cellspacing="0"><tr></db:repeaterHeader>
  <db:repeaterFooter></tr></table></db:repeaterFooter>
  <db:repeaterItem><td width="100pt"><netui:span value="${container.item}"/></td>
     </db:repeaterItem>
  <db:pad maxRepeat="${pageFlow.max}" padText="${pageFlow.text}" />
</db:repeater>
</body>
</html>
