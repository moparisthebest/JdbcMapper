<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Stateful Controller
        </title>
    </head>
    <body>
        <h3>Stateful Controller</h3>

        state: <b><netui:span value="${pageFlow.someState}"/></b>

        <br>
        <br>
        <netui:anchor action="goNested">go nested</netui:anchor>
        <br>
        <netui:anchor action="begin">start over</netui:anchor>
    </body>
</netui:html>
