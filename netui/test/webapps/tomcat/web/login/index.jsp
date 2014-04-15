<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>${pageFlow.URI}</h3>

        <span style="color:red">
            <netui:exceptions showStackTrace="false" showMessage="true"/>
        </span>

        <netui:form action="doLogin">
            <table>
                <tr>
                    <td>username:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.username"/>
                        <span style="color:red"><netui:error key="username"/></span>
                    </td>
                </tr>
                <tr>
                    <td>password:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.password" password="true"/>
                        <span style="color:red"><netui:error key="password"/></span>
                    </td>
                </tr>
            </table>

            <netui:button value="submit"/>
        </netui:form>

        <netui:anchor action="mustBeLoggedIn">mustBeLoggedIn</netui:anchor>
    </netui:body>
</netui:html>

  

