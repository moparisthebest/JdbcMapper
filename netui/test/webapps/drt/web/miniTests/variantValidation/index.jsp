<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>Declarative Validation Test</h3>

        <netui:anchor href="saveLocale.do">Save current locale</netui:anchor>
        <br/>
        <netui:anchor href="changeLocale1.do">Change to French locale &quot;fr_CA&quot;</netui:anchor>
        <br/>
        <netui:anchor href="changeLocale2.do">Change locale to &quot;fr_CA_MAC&quot;</netui:anchor>
        <br/>
        <netui:anchor href="resetLocale.do">Reset locale</netui:anchor>

        <netui:form action="validate">
            <table>
                <tr valign="top">
                    <td>item:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.item"></netui:textBox>
                    </td>
                    <td>
                    <netui:error key="item"/>
                    </td>
                </tr>
            </table>
            <br>
            &nbsp;
            <netui:button action="validate"></netui:button>
        </netui:form>
        <hr>
        <netui:errors/>
    </netui:body>
</netui:html>

  
