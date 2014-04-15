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
        <h4>BindingUpdateErrors test</h4>
        <p style="color:green">
        This test will post a form to an action that doesn't accept a form.  This will generate a set
        of binding update errors because the values don't update anything.  These errors will be
        reported below by the<b>bindingUpdateErrors</b> tag.  You should enter a value into the form and
        hit the submit button.  Then this page will redisplay with the binding errors.
        </p>
        <hr>
        <netui:form action="postName">
            <table>
                <tr valign="top">
                    <td>FirstName:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.firstName"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>LastName:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.lastName"/>
                    </td>
                </tr>
            </table>
            <br/>&nbsp;
            <netui:button value="postName" type="submit"/>&nbsp;
        <hr>
        <netui:bindingUpdateErrors alwaysReport="true"/><br>
        Errors for actionForm.firstName: <netui:bindingUpdateErrors alwaysReport="true"  expression="actionForm.firstName"/><br>
        Errors for actionForm.lastame: <netui:bindingUpdateErrors alwaysReport="true" expression="actionForm.lastName"/><br>
         </netui:form>
    </netui:body>
</netui:html>
