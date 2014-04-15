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
    <body>
        <netui:form action="results">
            <table>
                <tr valign="top">
                    <td>Date:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.date" size="16" readonly="true">
                        <netui:formatDate pattern="M/d/yy HH:mm"/>
                    </netui:textBox>
                    
                    </td>
                </tr>
                <tr valign="top">
                    <td>Type:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.type"/>
                    </td>
                </tr>
            </table>
            <br/>&nbsp;
            <netui:button value="results" type="submit"/>
        </netui:form>
     </body>
</netui:html>
