<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<netui-data:declarePageInput name="string" type="java.lang.String"/>

<html>
    <head>
    </head>
    <body>
        <h3>String entry point</h3>

        value: <b><netui:span value="${pageInput.string}"/></b>
        <br/>
        <netui:anchor action="doneString">done</netui:anchor>
    </body>
</html>
