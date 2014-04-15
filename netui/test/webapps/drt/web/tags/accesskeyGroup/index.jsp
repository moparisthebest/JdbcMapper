<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Web Application Page
        </title>
    </head>
    <netui:body>
        <h4>RadioButtonGroup and CheckBoxGroup AccessKey</h4>
        <p style="color:green">
        This test binds a <b>RadioButtonGroup</b> and <b>CheckBoxGroup</b> to an optionsDataSource constructed
        in the page flow.  The page flow creates arrays of <b>GroupOption</b> which are used as the
        optionsDataSource.  Each option has an accessKey defined.  Post the form to see what was selected.
        </p>
        <netui:form action="post">
            <table>
                <tr valign="top">
                    <td>Attributes:</td>
                    <td>
                    <netui:checkBoxGroup dataSource="actionForm.attributes" optionsDataSource="${pageFlow.attributes}"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Type:</td>
                    <td>
                    <netui:radioButtonGroup dataSource="actionForm.type" optionsDataSource="${pageFlow.colors}"/>
                    </td>
                </tr>
            </table>
            <br/>&nbsp;
            <netui:button value="post" type="submit"/>
        </netui:form>
        <hr>
    </netui:body>
</netui:html>
