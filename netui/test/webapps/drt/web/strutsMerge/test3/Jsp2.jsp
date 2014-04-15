<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<html>
    <head>
        <title>Merge Test3</title>
    </head>
    <body>
        <h3 align="center">Merge Test3 - Jsp2.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <netui:form action="jpfAction2">
                <table>
                    <tr class="tablebody">
                        <td>Field1</td>
                        <td>
                            <netui:textBox dataSource="actionForm.field1"/>
                        </td>
                    </tr>
                </table>
                <netui:button action="jpfAction2" value="continue" type="submit"/>
            </netui:form>
        </center>
    </body>
</html>
