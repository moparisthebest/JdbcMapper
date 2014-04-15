<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Roles Test
        </title>
    </head>
    <body>
        <h3>Roles Test</h3>
        <font color="Red">
            <netui:exceptions showStackTrace="false" showMessage="true"/> 
        </font>
        <br>
        <netui:anchor action="goodRoleAction">goodRoleAction</netui:anchor>
        <br>
        <netui:anchor action="badRoleAction">badRoleAction</netui:anchor>
        <br>
        <netui:anchor action="allRolesAction">allRolesAction</netui:anchor>
        <br>
        <netui:anchor action="logOut">log out</netui:anchor>
        <br>
    </body>
</netui:html>
