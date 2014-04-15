<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui-template:template templatePage="../site/template.jsp">
    <netui-template:setAttribute name="title" value="Data Grid TagID Tests"/>
    <netui-template:section name="body">
    <p>
    <table>
        <tr><td>Anchor Cell</td></tr>
        <tr><td><netui:anchor href="legacy-anchorcell.jsp" value="Legacy AnchorCell"/></td></tr>
        <tr><td><netui:anchor href="scriptcontainer-anchorcell.jsp" value="ScriptContainer AnchorCell"/></td></tr>
        <tr><td>Image Cell</td></tr>
        <tr><td><netui:anchor href="legacy-imagecell.jsp" value="Legacy ImageCell"/></td></tr>
        <tr><td><netui:anchor href="scriptcontainer-imagecell.jsp" value="ScriptContainer ImageCell"/></td></tr>
        <tr><td>ImageAnchor Cell</td></tr>
        <tr><td><netui:anchor href="legacy-imageanchorcell.jsp" value="Legacy ImageAnchorCell"/></td></tr>
        <tr><td><netui:anchor href="scriptcontainer-imageanchorcell.jsp" value="ScriptContainer ImageAnchorCell"/></td></tr>
        <tr><td>Span Cell</td></tr>
        <tr><td><netui:anchor href="legacy-spancell.jsp" value="Legacy SpanCell"/></td></tr>
        <tr><td><netui:anchor href="scriptcontainer-spancell.jsp" value="ScriptContainer SpanCell"/></td></tr>
        <tr><td>THead</td></tr>
        <tr><td><netui:anchor href="legacy-thead.jsp" value="Legacy THead"/></td></tr>
        <tr><td><netui:anchor href="scriptcontainer-thead.jsp" value="ScriptContainer THead"/></td></tr>
        <tr><td>TBody</td></tr>
        <tr><td><netui:anchor href="legacy-tbody.jsp" value="Legacy TBody"/></td></tr>
        <tr><td><netui:anchor href="scriptcontainer-tbody.jsp" value="ScriptContainer TBody"/></td></tr>
        <tr><td>TFoot</td></tr>
        <tr><td><netui:anchor href="legacy-tfoot.jsp" value="Legacy TFoot"/></td></tr>
        <tr><td><netui:anchor href="scriptcontainer-tfoot.jsp" value="ScriptContainer TFoot"/></td></tr>
        <tr><td>Caption</td></tr>
        <tr><td><netui:anchor href="legacy-caption.jsp" value="Legacy Caption"/></td></tr>
        <tr><td><netui:anchor href="scriptcontainer-caption.jsp" value="ScriptContainer Caption"/></td></tr>
        <tr><td>Table</td></tr>
        <tr><td><netui:anchor href="legacy-table.jsp" value="Legacy Table"/></td></tr>
        <tr><td><netui:anchor href="scriptcontainer-table.jsp" value="ScriptContainer Table"/></td></tr>
        <tr><td>Th</td></tr>
        <tr><td><netui:anchor href="legacy-th.jsp" value="Legacy Th"/></td></tr>
        <tr><td><netui:anchor href="scriptcontainer-th.jsp" value="ScriptContainer Th"/></td></tr>
        <tr><td>Td</td></tr>
        <tr><td><netui:anchor href="legacy-td.jsp" value="Legacy Td"/></td></tr>
        <tr><td><netui:anchor href="scriptcontainer-td.jsp" value="ScriptContainer Td"/></td></tr>
        <tr><td>Tr</td></tr>
        <tr><td><netui:anchor href="legacy-tr.jsp" value="Legacy Tr"/></td></tr>
        <tr><td><netui:anchor href="scriptcontainer-tr.jsp" value="ScriptContainer Tr"/></td></tr>
        <tr><td>All</td></tr>
        <tr><td><netui:anchor href="legacy-all.jsp" value="Legacy All"/></td></tr>
        <tr><td><netui:anchor href="scriptcontainer-all.jsp" value="ScriptContainer All"/></td></tr>
    </table>
    </p>
    </netui-template:section>
</netui-template:template>
