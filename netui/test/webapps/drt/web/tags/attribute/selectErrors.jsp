<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html documentType="html4-loose">
    <head>
        <title>
            Test Attribute Tag
        </title>
    </head>
    <body>
        <p>
        select Tag Errors
        </p>
        <netui:form action="submitIt">
            <netui:select dataSource="actionForm.selectedItems" 
                          defaultValue="${pageFlow.defaultSelectedItems}" 
                          optionsDataSource="pageFlow.defaultOptions"
                          style="theStyle" 
                          styleClass="theClass" 
                          multiple="true">
                <%-- Test override --%>
                <netui:attribute name="class" value="MyAttributeClass"/>
                <netui:attribute name="style" value="MyAttributeStyle"/>        

                <%-- Test netui expression --%>
                <%
                    pageContext.setAttribute("color", new String("red"));
                %>
                <netui:attribute name="customAttr" value="${pageScope.color}"/>
                <%-- Test a regular old attribute --%>
                <netui:attribute name="anotherCustomAttr" value="anotherCustomValue"/>

                <%-- Test disallowed attributes --%>
                <%-- these cause errors --%>         
                <netui:attribute name="id" value="whatever"/>
                <netui:attribute name="name" value="whatever"/>
            </netui:select>       
            <netui:button value="submitIt" type="submit"/>
        </netui:form>
    </body>
</netui:html>
