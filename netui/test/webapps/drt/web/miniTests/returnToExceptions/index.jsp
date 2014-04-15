<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Return-to Exceptions
        </title>
    </head>
    <body>
        <h3>Return-to Exceptions</h3>
        
        <font color="red">
            <netui:exceptions showMessage="true" showStackTrace="false"/>
        </font>
        <br>

        <netui:anchor action="doReturnToPreviousPage">doReturnToPreviousPage</netui:anchor>
        <br>
        <netui:anchor action="doReturnToCurrentPage">doReturnToCurrentPage</netui:anchor>
        <br>
        <netui:anchor action="doReturnToPreviousAction">doReturnToPreviousAction</netui:anchor>
    </body>
</netui:html>
