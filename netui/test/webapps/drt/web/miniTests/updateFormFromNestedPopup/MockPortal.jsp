<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="mockportal.tld" prefix="mp" %>

<html>
<head>
<title>Mock Portal (/mockportal)</title>
</head>

<body>

    <h3>Mock Portal (/mockportal)</h3>
    <mp:mockPortal>
        <mp:mockPortlet portletID="portletA" pageFlowURI="/miniTests/updateFormFromNestedPopup/Controller.jpf" verbose="false" />
        <mp:mockPortlet portletID="portletB" pageFlowURI="/miniTests/updateFormFromNestedPopup/Controller.jpf" verbose="false" />
    </mp:mockPortal>

</body>
</html>
