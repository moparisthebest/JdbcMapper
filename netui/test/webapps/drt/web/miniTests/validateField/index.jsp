<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>Declarative Validation Test</h3>

        <netui:anchor href="saveLocale.do">Save current locale</netui:anchor>
        <br/>
        <netui:anchor href="changeLocale.do">Change to French locale &quot;fr&quot;</netui:anchor>
        <br/>
        <netui:anchor href="resetLocale.do">Reset locale</netui:anchor>

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
            </table>
            <br/>
            &nbsp;
            <netui:button action="validate"></netui:button>
        </netui:form>
        <hr>
        <netui:errors/>
    </netui:body>
</netui:html>

