<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
                    

        <netui:anchor action="done">return from nesting</netui:anchor>
        <br/>
        <netui:anchor action="backButton">simulate back button</netui:anchor>
        <br/>
        <netui:anchor action="goNestedTwo">goNestedTwo</netui:anchor>

                
    </netui:body>
</netui:html>

  
