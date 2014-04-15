<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="mockportal.tld" prefix="mp" %>

<html>
<head>
<title>Mock Portal - MultipartRequestWrapper Test</title>
</head>

<body>

    <h3>Mock Portal - MultipartRequestWrapper Test</h3>
    <mp:mockPortal>
        <mp:mockPortlet portletID="formA" pageFlowURI="/mockportal/unwrapmultipart/Controller.jpf" verbose="false" />
    </mp:mockPortal>

    <br/>
    Get attribute from the outer request:
    <br/>
    testAttr value: <%= (String) request.getAttribute( "testAttr" ) %>

</body>
</html>
