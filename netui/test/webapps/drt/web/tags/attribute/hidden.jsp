<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html documentType="html4-loose">
    <head>
        <title>
            Test Attribute Tag
        </title>
    </head>
    <body>
        <p> hidden </p>
        <netui:form action="submitIt">
            <netui:hidden dataSource="pageFlow.hiddenParam">
                <%-- Test netui expression --%>
                <%
                    pageContext.setAttribute("color", new String("red"));
                %>
                <netui:attribute name="customAttr" value="${pageScope.color}"/>
                <%-- Test a regular old attribute --%>
                <netui:attribute name="anotherCustomAttr" value="anotherCustomValue"/>
            </netui:hidden>
            
            <table>
                <tr valign="top">
                    <td>TheProperty:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.theProperty"/>
                    </td>
                </tr>
            </table>
            <br/>&nbsp;
            <netui:button value="submitIt" type="submit"/>
        </netui:form>
    </body>
</netui:html>
