<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base/>
        <title>Any Bean Test</title>
    </head>
    <body>
        <h3>Nesting Result</h3>

        msg: <netui:span value="${pageInput.result.msg}"/>
        <br>
        <br>
        <netui:anchor action="begin">go to start</netui:anchor>
    </body>
</netui:html>

  

