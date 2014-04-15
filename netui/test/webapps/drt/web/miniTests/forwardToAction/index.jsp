<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Forward to Action
        </title>
    </head>
    <body>
        <h3>Forward to Action</h3>
        <netui:form action="someAction">
            <table>
                <tr valign="top">
                    <td>
                        Foo:
                    </td>
                    <td>
                        <netui:textBox dataSource="actionForm.foo"/>
                    </td>
                </tr>
            </table>
            <br/>
            &nbsp;
            <netui:button value="Submit" type="submit"/>
            <input type="checkbox" name="withForm"> Forward to action with form
        </netui:form>
    </body>
</netui:html>
