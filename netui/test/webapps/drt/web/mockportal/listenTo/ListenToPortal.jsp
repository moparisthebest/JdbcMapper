<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="mockportal.tld" prefix="mp" %>

<html>
<head>
<title>Mock Portal - listenTo</title>
</head>

<body>

    <h3>Mock Portal - listenTo</h3>
    <mp:mockPortal>
        <mp:mockPortlet portletID="a1" pageFlowURI="/mockportal/listenTo/listenToA/aController.jpf" verbose="false" />
        <mp:mockPortlet portletID="b1" pageFlowURI="/mockportal/listenTo/listenToB/BController.jpf" verbose="false" listenTo="a1" />
    </mp:mockPortal>

</body>
</html>
