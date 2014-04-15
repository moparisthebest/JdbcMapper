<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<html>
    <body>
        <font color="red"><netui:span value="${pageFlow.message}"/></font>

        <br>
        <br>
        <netui:form action="overload">
            <netui:button>hit overloaded action (should get String version)</netui:button>
        </netui:form>

        <br>
        <netui:anchor action="chainToString">chainToString</netui:anchor>
        <br>
        <netui:anchor action="chainToHashMap">chainToHashMap</netui:anchor>
    </body>
</html>
