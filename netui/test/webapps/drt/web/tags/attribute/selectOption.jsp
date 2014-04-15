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
        selectOption
        </p>
        <netui:form action="submitIt">
            <netui:select dataSource="actionForm.selectedItems" 
                          defaultValue="${pageFlow.defaultSelectedItems}" 
                          style="theStyle" 
                          styleClass="theClass" 
                          multiple="true">
                <netui:selectOption value="${pageFlow.optionOne}" style="theStyle" styleClass="theClass">
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
                </netui:selectOption>
                <netui:selectOption value="${pageFlow.optionTwo}"/>
                <netui:selectOption value="${pageFlow.optionThree}"/>
            </netui:select>       
            <netui:button value="submitIt" type="submit"/>
        </netui:form>
    </body>
</netui:html>
