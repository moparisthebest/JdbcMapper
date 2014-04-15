<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
    <head>
        <title>Merge Test6</title>
    </head>
    <body>
        <h3 align="center">Merge Test6 - Jsp1.jsp</h3>
        <hr width="95%"/>
        <br/>
        <h3 align="center">
            If you check the box and "submit" you should be<br/>
            taken to the "done.jsp" page.  If you uncheck the<br/>
            box you should be taken to the "error.jsp" page.
        </h3>
        <center>
            <netui:form action="jpfAction1">
                <table>
                    <tr>
                        <td>Check to throw the exception</td>
                        <td><netui:checkBox dataSource="actionForm.field1"/></td>
                    </tr>
                </table>
                <netui:button action="jpfAction1" value="Submit" type="submit"/>
            </netui:form>
        </center>
    </body>
</html>
