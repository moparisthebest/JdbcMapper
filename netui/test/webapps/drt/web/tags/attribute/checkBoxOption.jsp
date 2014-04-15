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
        checkBoxOption
        </p>
        <%--  Here's a hint on the use of the checkBoxGroup tag:
              Explanation - You bind an array of strings to the checkBoxGroup datasource. 
              If the first string in the array matches the value you assigned to the first 
              checkBoxOption the checkbox is marked as true.  If the second string in the 
              array matches the value you assigned to the second checkBoxOption the 
              checkbox is marked as true.--%>
        
        <netui:form action="submitIt">
            <netui:checkBoxGroup dataSource="actionForm.checkedBoxes"                                
                                 defaultValue="${pageFlow.defaultOptions}">
                <netui:checkBoxOption value="${pageFlow.optionOne}" 
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
                </netui:checkBoxOption>
                <netui:checkBoxOption value="${pageFlow.optionTwo}">
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
                </netui:checkBoxOption>
                <netui:checkBoxOption value="${pageFlow.optionThree}"></netui:checkBoxOption>
            </netui:checkBoxGroup>
            <netui:button value="submitIt" type="submit"/>
        </netui:form>
    </body>
</netui:html>
