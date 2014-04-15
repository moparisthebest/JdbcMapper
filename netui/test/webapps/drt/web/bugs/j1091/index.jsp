<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template" %>
<netui:html>
    <head>
        <title>Web Application Page</title>
    </head>
    <body>
    <p>
        This test verifies that the <em>checkBoxGroup</em> and <em>select</em> tags can be nested
        inside of a <em>repeater</em> and <em>radioButtonGroup</em> respectively. This ensures that
        NetUI expressions are checked and rewritten correctly.
        <%
            pageContext.setAttribute("strings", new String[]{"red", "green", "blue"});
        %>
        <netui:form action="submit">
        <b>Embed a <em>select</em> box inside of a <em>radioButtonGroup</em>
            <br/>
            <netui:radioButtonGroup dataSource="actionForm.color">
                <netui:radioButtonOption value="red"/>
                <netui:radioButtonOption value="green"/>
                <netui:radioButtonOption value="blue"/>
                <netui:select dataSource="actionForm.shade">
                    <netui:selectOption value="light"/>
                    <netui:selectOption value="dark"/>
                </netui:select>
            </netui:radioButtonGroup>
            <br/>
            <b>Embed a <em>checkBoxGroup</em> inside of a <em>repeater</em></b>
            <br/>
            <netui-data:repeater dataSource="pageScope.strings">
                <netui:checkBoxGroup dataSource="actionForm.colors">
                    <netui:checkBoxOption value="red"/>
                    <netui:checkBoxOption value="green"/>
                    <netui:checkBoxOption value="blue"/>
                </netui:checkBoxGroup>
                <br/>
            </netui-data:repeater>
            <br/>
                <netui:button value="Submit"/>
            </netui:form>
    </p>
    </body>
</netui:html>
