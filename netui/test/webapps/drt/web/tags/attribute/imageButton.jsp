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
        imageButton
        </p>
        <netui:form action="submitIt">
            <table>
                <tr valign="top">
                    <td>TheProperty:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.theProperty"/>
                    </td>
                </tr>
            </table>
            <br/>
            &nbsp;
            <netui:imageButton src="./folder.gif" style="theStyle" styleClass="theClass">
                <%-- Test override --%>
                <netui:attribute  name="class" value="MyAttributeClass"/>
                <netui:attribute name="style" value="MyAttributeStyle"/>

                <%-- Test netui expression --%>
                <%
                    pageContext.setAttribute("color", new String("red"));
                %>
                <netui:attribute name="customAttr" value="${pageScope.color}"/>

                <%-- Test a regular old attribute --%>
                <netui:attribute name="anotherCustomAttr" value="anotherCustomValue"/>
            </netui:imageButton>
        </netui:form>
    </body>
</netui:html>
