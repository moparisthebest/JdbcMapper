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
        <br>
        <netui:form action="post">
            <table>
                <tr valign="top">
                    <td>Attributes:</td>
                    <td>
                    <% try { %>
                    <netui:checkBoxGroup dataSource="actionForm.attributes[0]" optionsDataSource="${actionForm.colors}"/>
                    <% } catch ( Exception e ) { pageContext.getOut().print( e.getMessage() ); } %>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Type:</td>
                    <td>
                    <% try { %>
                    <netui:radioButtonGroup dataSource="actionForm.type" optionsDataSource="${actionForm.colors}"/>
                    <% } catch ( Exception e ) { pageContext.getOut().print( e.getMessage() ); } %>
                    </td>
                </tr>
            </table>
            <br/>&nbsp;
            <netui:button value="post" type="submit"/>
        </netui:form>
        <br>
    </netui:body>
</netui:html>
