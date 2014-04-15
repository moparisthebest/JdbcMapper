<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Web Application Page
        </title>
    </head>
    <netui:body>
        <netui:form action="postForm">
            <table>
                <tr valign="top">
                    <td>Type:</td>
                    <td>
                    <netui:select dataSource="actionForm.type" optionsDataSource="${pageFlow.options}" tagId="select"/>
                    </td>
                </tr>
            </table>
            <br/>&nbsp;
            <netui:button value="postForm" type="submit"/>
        </netui:form>
        <hr />
        Type: <netui:span value="${pageFlow.type}"></netui:span>
    </netui:body>
    <script language="JavaScript" type="text/JavaScript">
        var name = getNetuiTagName("select");
        document.writeln("<br/>Select Name:" + name + "<br/>");
        var select = document.getElementsByName(name);
        document.writeln("Select: " + select[0].selectedIndex);
    </script>
</netui:html>
