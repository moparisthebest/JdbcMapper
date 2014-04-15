<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <netui:form action="formSubmit">
            <netui:button value="netui:form (prevent double post)"/>
        </netui:form>
        <netui:form action="begin">
            <netui:button action="formSubmit" value="netui:button (prevent double post)"/>
        </netui:form>
        <netui:anchor action="anchorSubmit">netui:anchor (prevent double post)</netui:anchor>
        <br/>
        <br/>
        <netui:imageAnchor action="anchorSubmit" src="../../resources/images/goButton.jpg">netui:imageAnchor (prevent double post)</netui:imageAnchor>
        <br/>
    </netui:body>
</netui:html>

  

