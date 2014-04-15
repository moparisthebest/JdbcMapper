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
        checkBox Tag Errors
        </p>
<% 
    pageContext.setAttribute("checkBoxDefaultValue", new Boolean("true"));
%> 
        <netui:form action="submitIt">
            <table>
                <tr valign="top">
                    <td>My Checkbox:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.boxChecked" 
                                    disabled="false" 
                                    defaultValue="${pageScope.checkBoxDefaultValue}" 
                                    style="theStyle" 
                                    styleClass="theClass">                       
                        <%-- Test override --%>
                        <netui:attribute name="class" value="MyAttributeClass"/>
                        <netui:attribute name="style" value="MyAttributeStyle"/>
                        <netui:attribute name="disabled" value="false"/>
                        <%-- Test netui expression --%>
                        <%
                            pageContext.setAttribute("color", new String("red"));
                        %><a href="/TuresAppWeb/testPages/hidden/index.jsp">/TuresAppWeb/testPages/hidden/index.jsp</a>
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
                    </netui:checkBox>
                    </td>
                </tr>
            </table>
            <br/>&nbsp;
            <netui:button value="submitIt" type="submit"/>
        </netui:form>
    </body>
</netui:html>
