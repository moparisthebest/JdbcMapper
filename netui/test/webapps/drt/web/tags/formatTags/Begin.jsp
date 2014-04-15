<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Format Tests</title>
</head>
<body>
<h4>Format Tests</h4>
<b>String Format</b>
<table border="1" cellspacing="0">
<tr><th>Truncate</ty><td>
<netui:span value="${pageFlow.letters}">
     <netui:formatString pattern="#-#####-#" truncate="true"/>
</netui:span>
</td><td>
<netui:span value="abcdefghijklmnopqrstuvwxyz">
     <netui:formatString pattern="#-#####-#" truncate="true"/>
</netui:span>
</td><td>&nbsp</td></tr>
<tr><th>No Truncate</th><td>
<netui:span value="${pageFlow.letters}">
     <netui:formatString pattern="#-#####-#" />
</netui:span>
</td><td>
<netui:span value="abcdefghijklmnopqrstuvwxyz">
     <netui:formatString pattern="#-#####-#"/>
</netui:span>
</td><td>&nbsp</td></tr>
<tr><th>Contains Hash</th>
<td><pre><netui:span value="${pageFlow.hash}"><netui:formatString pattern="#-#####-#" truncate="true"/></netui:span></pre></td>
<th>Contains spac</th>
<td><pre><netui:span value="${pageFlow.spaces}"><netui:formatString pattern="#-#####-#" truncate="true"/></netui:span></pre></td>
</tr>
<tr><th>Hash Escape</th>
<td><netui:span value="${pageFlow.letters}"><netui:formatString pattern="$##-#####-#" truncate="true"/></netui:span></td>
<th>Escape Chars</th>
<td><netui:span value="${pageFlow.letters}"><netui:formatString pattern="$$#-#####-#" truncate="true"/></netui:span></td>
</tr>
<tr><th>Bind to Pattern</th><td>
<netui:span value="${pageFlow.letters}">
     <netui:formatString pattern="${pageFlow.stringPattern}" truncate="true"/>
</netui:span>
</td><td>
<netui:span value="abcdefghijklmnopqrstuvwxyz">
     <netui:formatString pattern="${pageFlow.stringPattern}" truncate="true"/>
</netui:span>
</td><td>&nbsp</td></tr>
</table>
<b>Number Format</b>
<table border="1" cellspacing="0">
<tr><th>EN Currency</ty><td>
<netui:span value="12345.67">
     <netui:formatNumber language="EN" country = "US" type="currency"/>
</netui:span>
<th>GB Currency</th><td>
<netui:span value="12345.67">
     <netui:formatNumber language="EN" country = "GB" type="currency"/>
</netui:span>
</td></tr>
<tr><th>EN Number</ty><td>
<netui:span value="12345.67">
     <netui:formatNumber language="EN" country = "US" type="number"/>
</netui:span>
<th>GB Number</th><td>
<netui:span value="12345.67">
     <netui:formatNumber language="EN" country = "GB" type="number"/>
</netui:span>
</td></tr>
<tr><th>EN Percentage</ty><td>
<netui:span value=".167">
     <netui:formatNumber language="EN" country = "US" type="percent"/>
</netui:span>
<th>GB Percentage</th><td>
<netui:span value=".167">
     <netui:formatNumber language="EN" country = "GB" type="percent"/>
</netui:span>
</td></tr>
<tr><th>Pattern</ty><td>
    <netui:span value="${pageFlow.number}">
        <netui:formatNumber pattern="$#,###,###.00" />
    </netui:span>
<th>Escaped Pattern</th><td>
    <netui:span value="${pageFlow.number}">
        <netui:formatNumber pattern="\##,###,###.00" />
    </netui:span>
</td></tr>
</table>
<b>Date Format</b>
<table border="1" cellspacing="0">
<tr><th>US Date</th><td>
<netui:span value="${pageFlow.date}">
     <netui:formatDate language="EN" country = "US" pattern="M/dd/yy h:mm a"/>
</netui:span>
</td><th>GB Date</th><td>
<netui:span value="${pageFlow.date}">
     <netui:formatDate language="EN" country = "GB" pattern="M/dd/yy h:mm a"/>
</netui:span>
</td></tr>

<tr><th>yyyy.MM.dd G 'at' HH:mm:ss z</th><td>
<netui:span value="${pageFlow.date}">
     <netui:formatDate pattern="yyyy.MM.dd G 'at' HH:mm:ss z"/>
</netui:span>
</td><th>yyyy.MM.dd G 'at' HH:mm:ss z  (EN, GB)</th><td>
Removed because this is not handled by the test recorder correctly
</td></tr>

<tr><th>yyyy.MM.dd G 'at' HH:mm:ss z (FR)</th><td>
Removed because the test recorder doesn't handle the resulting encoding correctly.
</td><th>yyyy.MM.dd G 'at' HH:mm:ss z (RU)</th><td>
Removed because this is not handled by the test recorder correctly
</td></tr>
</table>
</body>
</html>
