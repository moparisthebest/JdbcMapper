<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html documentType="html4-loose">
    <head>
        <%-- Base tag --%> 
        <%-- Test netui expression --%>
        <%
            pageContext.setAttribute("color", new String("red"));
        %>

        <netui:base target="_top">
            <%-- Test override --%>
            <netui:attribute name="target" value="_myAttributeTop" />
            <netui:attribute name="customAttr" value="${pageScope.color}"/>

            <%-- Test a regular old attribute --%>
            <netui:attribute name="anotherCustomAttr" value="anotherCustomValue"/> 

            <%-- Test disallowed attributes --%>
            <%-- these cause errors --%>          
            <netui:attribute name="href" value="whatever"/>
        </netui:base>
        <title>
            Test Attribute Tag
        </title>
    </head>
    <body>
        <p>
            base Tag Errors
        </p>      
    </body>
</netui:html>
