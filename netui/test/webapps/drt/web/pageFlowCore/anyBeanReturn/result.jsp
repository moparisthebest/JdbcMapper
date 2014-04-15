<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<netui-data:declarePageInput name="result" type="java.lang.String"/><html>
    <head>
    </head>
    <body>
        The form (String) was: <b><netui:span value="${pageInput.result}"/></b>
    </body>
</html>
