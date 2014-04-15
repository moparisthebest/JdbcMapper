<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <title>J841</title>
    </head>
    <body>
        <p>
            This is a test to ensure that the TextArea tag does not remove
            a leading empty line.
        </p>
        <netui:form action="nextAction" focus="">
            <table>
                <tr>
                    <td><netui:label value="Text Area: "></netui:label></td>
                    <td> </td>
                </tr>
                <tr>
                    <td>
                    <netui:textArea dataSource="actionForm.taform" rows="10" cols="30"></netui:textArea>
                    </td>
                </tr>
            </table>
            <netui:button value="nextAction"></netui:button>
        </netui:form>
    </body>
</netui:html>
