<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>


<html>
<head>
<title>NetUI Validation Test</title>
</head>
<body bgcolor="white">

<h3>NetUI Validation Test</h3>

<br>

<netui:form action="submitForm">
    <table>
        <tr>
            <td>
                email address:
            </td>
            <td>
            <netui:textBox dataSource="actionForm.email"/>
            </td>
            <td>
            <netui:error key="email"/>
            </td>
        </tr>
        <tr>
            <td>
                age:
            </td>
            <td>
            <netui:textBox dataSource="actionForm.age"/>
            </td>
            <td>
            <netui:error key="age"/>
            </td>
        </tr>
    </table>

    <netui:button>submit</netui:button>
    <netui:button action="exit">exit</netui:button>
</netui:form>

<netui:errors/>

</body>
</html>
