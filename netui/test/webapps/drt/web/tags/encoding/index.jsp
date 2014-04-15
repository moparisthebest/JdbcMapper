<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Test Multibyte Characters and Tags</title>
<netui:base />
</head>
<body>

<p>Test NetUI tag support of multibyte characters and escaped characters for proper URI syntax...</p>
<ul>
<li>
    <netui:anchor action="beginUTF8">Test escaping UTF-8 character set
    </netui:anchor>
</li>
<li>
    <netui:anchor action="beginEUCJP">Test escaping EUC_JP character set
    </netui:anchor>
</li>
<li>
    <netui:anchor action="escaping">Test other escaped characters
    </netui:anchor>
</li>
</ul>
</body>
</html>
