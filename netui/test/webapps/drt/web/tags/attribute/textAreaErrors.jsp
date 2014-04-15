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
        textArea Tag Errors
        </p>
        <netui:form action="submitIt">
            <table>
                <tr valign="top">
                    <td>TheProperty:</td>
                    <td>
                    <netui:textArea rows="99"
                                    cols="99" 
                                    dataSource="actionForm.theProperty"  
                                    disabled="true" 
                                    readonly="true" 
                                    style="theStyle" 
                                    styleClass="theClass">
                        <%-- Test override --%>
                        <netui:attribute  name="class" value="MyAttributeClass"/>
                        <netui:attribute name="style" value="MyAttributeStyle"/>
                        <netui:attribute name="rows" value="5"/>
                        <netui:attribute name="cols" value="5"/>

                        <% 
                            pageContext.setAttribute("disabled", new String("false"));
                            pageContext.setAttribute("readonly", new String("false"));
                        %>
                        <netui:attribute name="disabled" value="${pageScope.disabled}"/>
                        <netui:attribute name="readonly" value="${pageScope.readonly}"/>

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
                    </netui:textArea>                    
                    </td>
                </tr>
            </table>
            <br/>&nbsp;
            <netui:button value="submitIt" type="submit"/>
        </netui:form>
    </body>
</netui:html>
