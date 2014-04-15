<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<netui-data:declarePageInput name="message" type="java.lang.String"/>

<html>
    <head>
    </head>
    <body>
        <h3>DataIn entry point</h3>

        value: <b><netui:span value="${pageInput.message}"/></b>
        <br/>
        <netui:anchor action="doneDataIn">done</netui:anchor>
    </body>
</html>
