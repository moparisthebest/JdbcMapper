<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Action Chaining
        </title>
    </head>
    <body>
        Forms were the same?  <b><netui:span value="${pageInput.isSameForm}"/></b>
        <br>
        <br>
        <netui:anchor action="begin">back</netui:anchor>
    </body>
</netui:html>
