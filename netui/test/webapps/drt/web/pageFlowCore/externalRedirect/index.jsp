<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    
        <p>Actions for redirect...
        <ul>
            <li>
                <netui:anchor action="relativeRedirect">Relative Redirect Test</netui:anchor>
            </li>
            <li>
                <netui:anchor action="fullyQualifiedRedirect">Fully Qualified Path Redirect Test</netui:anchor>
            </li>
            <li>
                <netui:anchor action="externalRedirect">External Page Redirect Test</netui:anchor>
            </li>
        </ul>
        <hr>
        <p>Simple Actions for redirect...
        <ul>        
            <li>
                <netui:anchor action="simpleRelative">Simple Action Relative Redirect Test</netui:anchor>
            </li>
            <li>
                <netui:anchor action="simpleFullyQualified">Simple Action Fully Qualified Path Redirect Test</netui:anchor>
            </li>
            <li>
                <netui:anchor action="simpleExternal">Simple Action External Page Redirect Test</netui:anchor>
            </li></ul>
        </ul>
            
    </netui:body>
</netui:html>

