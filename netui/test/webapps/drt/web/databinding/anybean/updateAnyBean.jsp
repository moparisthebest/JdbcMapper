<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <body>
        <netui:form action="updateAnyBean">
            <table>
                <tr valign="top">
                    <td>Name:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.name"/>
                    </td>
                </tr>
            </table>
            <br>
            &nbsp;
            <netui:button type="submit" value="updateAnyBean"/>
        </netui:form>
        <br/>
        <br/>
        POSTed value: <netui:span value="${pageInput.name}"/>
    </body>
</netui:html>

  
