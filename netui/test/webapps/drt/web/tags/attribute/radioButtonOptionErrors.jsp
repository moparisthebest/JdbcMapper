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
        radioButtonOption Tag Errors
        </p>
        <%--  Here's a hint on the use of the radioButtonGroup tag:
              Explanation - You bind a String to the radioButtonGroup dataSource. 
              If this String matches the value you assigned to the first 
              redioButtonOption the first radiobutton is marked as true.  
              If the String matches the value you assigned to the second 
              redioButtonOption the second radiobutton is marked as true, etc. --%>

        <netui:form action="submitIt">
            <netui:radioButtonGroup dataSource="actionForm.theProperty"
                                 defaultValue="${pageFlow.defaultOption}">
                <netui:radioButtonOption value="${pageFlow.optionOne}" 
                                      labelStyle="theLabelStyle" 
                                      labelStyleClass="theLabelStyleClass" 
                                      style="theStyle" 
                                      styleClass="theClass" >
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
                    <netui:attribute name="type" value="whatever"/>
                    <netui:attribute name="value" value="whatever"/>
                    <netui:attribute name="checked" value="whatever"/>
                </netui:radioButtonOption>
                <netui:radioButtonOption value="${pageFlow.optionTwo}">
                    <%-- Test override on inner span --%>
                    <netui:attribute facet="label" name="class" value="aLabelStyleClass"/>
                    <netui:attribute facet="label" name="style" value="aLabelStyle"/>

                    <%-- Test netui expression on inner span --%>
                    <%
                        pageContext.setAttribute("color", new String("red"));
                    %>
                    <netui:attribute facet="label" name="customAttr" value="${pageScope.color}"/>
                    <%-- Test a regular old attribute on inner span --%>
                    <netui:attribute facet="label" name="anotherCustomAttr" value="anotherCustomValue"/>

                    <%-- Test disallowed attributes on inner span --%>
                    <%-- these cause errors --%>     
                    <netui:attribute name="id" value="whatever"/>
                    <netui:attribute name="name" value="whatever"/>
                </netui:radioButtonOption>
                <netui:radioButtonOption value="${pageFlow.optionThree}"></netui:radioButtonOption>
            </netui:radioButtonGroup>
            <netui:button value="submitIt" type="submit"/>
        </netui:form>
    </body>
</netui:html>
