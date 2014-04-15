<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="mockportal.tld" prefix="mp" %>

<html>
<head>
<title>Mock Portal (/mockportal)</title>
</head>

<body>

    <h3>Mock Portal (/mockportal)</h3>
    <mp:mockPortal>
        <mp:mockPortlet portletID="smokeTestA" pageFlowURI="/mockportal/smokeTest/SmokeTestController.jpf" verbose="false" />
        <mp:mockPortlet portletID="smokeTestB" pageFlowURI="/mockportal/smokeTest/SmokeTestController.jpf" verbose="false" />
    </mp:mockPortal>

</body>
</html>
