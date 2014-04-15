<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Basic DivPanel Without RunAtClient container
        </title>
    </head>
    <netui:body>
    <h4>Basic DivPanel</h4>
    <p style="color:green">This test verifies that a DivPanel must be found inside
    a ScriptContainer that has runAtClient on.
    </p>
        <netui:divPanel tagId="divPanel">
            <netui-template:section name="page1">
            <table><tr>
                <td colspan="2" align="center">Page 1</td>
            </tr><tr>
                <td width="100pt">&nbsp</td>
                <td width="100pt"><netui:anchor clientAction='divPanel.showPage("page2");'>Next</netui:anchor></td>
            </tr></table>
            </netui-template:section>
            <netui-template:section name="page2">
            <table><tr>
                <td colspan="2" align="center">Page 2</td>
            </tr><tr>
                <td width="100pt"><netui:anchor clientAction='divPanel.showPage("page1");'>Previous</netui:anchor></td>
                <td width="100pt"><netui:anchor clientAction='divPanel.showPage("page3");'>Next</netui:anchor></td>
            </tr></table>
            </netui-template:section>
             <netui-template:section name="page3">
            <table><tr>
                <td colspan="2" align="center">Page 3</td>
            </tr><tr>
                <td width="100pt"><netui:anchor clientAction='divPanel.showPage("page2");'>Previous</netui:anchor></td>
                <td width="100pt">&nbsp;</td>
            </tr></table>
            </netui-template:section>
        </netui:divPanel>
    </netui:body>
</netui:html>
