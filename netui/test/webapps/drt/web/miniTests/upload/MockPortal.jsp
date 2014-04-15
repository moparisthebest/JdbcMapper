<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="mockportal.tld" prefix="mp" %>

<html>
<head>
<title>Mock Portal (/miniTests/upload)</title>
</head>

<body>

    <h3>Mock Portal (/miniTests/upload)</h3>
    <mp:mockPortal>
        <mp:mockPortlet portletID="upload1" pageFlowURI="/miniTests/upload/uploadController.jpf" />
        <mp:mockPortlet portletID="upload2" pageFlowURI="/miniTests/upload/uploadController.jpf" />
    </mp:mockPortal>

</body>
</html>
