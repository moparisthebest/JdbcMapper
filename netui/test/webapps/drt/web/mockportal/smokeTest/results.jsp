<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>

    <head>
        <title>
            Mock Portal Smoke Test
        </title>
    </head>
    <body>
        <h3>Mock Portal Smoke Test</h3>

        data: <b><netui:span value="${pageFlow.data}"/></b>
        <br/>
        <br/>
        <netui:anchor action="begin">go back</netui:anchor>
    </body>
</netui:html>
