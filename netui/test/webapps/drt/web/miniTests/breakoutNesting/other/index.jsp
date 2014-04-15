<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Other Controller
        </title>
    </head>
    <body>
        <h3>Other Controller</h3>

        page flow stack: <b><netui:span value="${pageInput.pfStack}"/></b>

        <br>
        <br>
        <netui:anchor action="startOver">start over</netui:anchor>
    </body>
</netui:html>
