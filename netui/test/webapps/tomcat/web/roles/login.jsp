<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Roles Test - Login
        </title>
    </head>
    <body>
        <h3>Roles Test - Login</h3>
        <font color="Red">
            <netui:exceptions showStackTrace="false" showMessage="true"/> 
        </font>
        <br>
        <netui:anchor action="logIn">log in</netui:anchor>
        <br>
    </body>
</netui:html>

