<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Multi Select
        </title>
    </head>
    <body>
        <p>
        MultiSelect <br/>
        <br/>
        <netui:form action="postback">
            <table>
                <tr valign="top">
                    <td>SelectedItems:</td>
                    <td>
                    <netui:select optionsDataSource="${pageFlow.jpfMap}" dataSource="actionForm.selectedItems"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Radio Button Group Items:</td>
                    <td>
                    <netui:radioButtonGroup optionsDataSource="${pageFlow.jpfRadio}" dataSource="actionForm.radioItem"/>
                    </td>
                </tr>
                
                <tr valign="top">
                    <td>CheckBox Group Items:</td>
                    <td>
                    <netui:checkBoxGroup optionsDataSource="${pageFlow.jpfCheck}" dataSource="actionForm.checkboxItems"/>
                    </td>
                </tr>
            </table>
            <br/>&nbsp;
            <netui:button value="postback" type="submit"/>
        </netui:form>
        </p>
    </body>
</netui:html>
