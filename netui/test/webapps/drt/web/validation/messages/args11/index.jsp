<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>Declarative Validation Test</h3>

        <netui:form action="validate">
            <table>
                <tr valign="top">
                    <td>item1:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.item1"></netui:textBox>
                    </td>
                    <td>
                    <netui:error key="item1"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>item2:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.item2"></netui:textBox>
                    </td>
                    <td>
                    <netui:error key="item2"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>item3:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.item3"></netui:textBox>
                    </td>
                    <td>
                    <netui:error key="item3"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>item4:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.item4"></netui:textBox>
                    </td>
                    <td>
                    <netui:error key="item4"/>
                    </td>
                </tr>
            </table>
            <br/>
            &nbsp;
            <netui:button action="validate"></netui:button>
        </netui:form>
        <hr>
        <netui:errors/>
    </netui:body>
</netui:html>

