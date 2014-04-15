<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui:html documentType="xhtml1-transitional">
<head>
   <title>JavaScript Bug</title>
</head>
<netui:body>
<script>
    function test (element)
    {
        var testAttrId = lookupIdByTagId ("testElem", element);
        alert ("XML:" + testAttrId);
        testAttrId = getNetuiTagName ("testElem", element);
        alert ("XML:" + testAttrId);
        var testAttr = document.getElementById (testAttrId);
        alert ("XML:" + testAttr.innerHTML);
    }
</script>
<netui:anchor href="Test.jsp">Html Document</netui:anchor>
<netui:scriptContainer generateIdScope="true">
<table>
    <tr>
        <td>
                <a href="TestXml.jsp" onClick="test (this);">bar</a><br>
                <netui:span tagId="testElem" value="span" />
        </td>
    </tr>
</table>
</netui:scriptContainer>
</netui:body>
</netui:html>


