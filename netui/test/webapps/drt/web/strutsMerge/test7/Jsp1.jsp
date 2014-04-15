<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
    <head>
        <title>Merge Test7</title>
    </head>
    <body>
        <h3 align="center">Merge Test7 - Jsp1.jsp</h3>
        <hr width="95%"/>
        <br/>
        <h3 align="center">
            Press submit and you should be taken to the "done.jsp" page.
        </h3>
        <center>
            <netui:form action="jpfAction1">
                <table>
                    <tr>
                        <td><netui:textBox dataSource="actionForm.field1"/></td>
                    </tr>
                </table>
                <netui:button action="jpfAction1" value="Submit" type="submit"/>
            </netui:form>
        </center>
    </body>
</html>
