<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        message for prop1: <b><netui:error key="prop1"/></b><br/>
        message for prop2: <b><netui:error key="prop2"/></b><br/>
        message for prop3: <b><netui:error key="prop3"/></b><br/>
        message for prop4: <b><netui:error key="prop4"/></b><br/>
        message for prop5: <b><netui:error key="prop5"/></b><br/>
        message for prop6: <b><netui:error key="prop6"/></b><br/>
        <br/>

        <netui:anchor action="addMessages">addMessages (prop1, prop2, prop3)</netui:anchor>
        <br/>
        <netui:anchor action="submitMyBean">submitMyBean (prop4, prop5, prop6)</netui:anchor>
    </netui:body>
</netui:html>

  

