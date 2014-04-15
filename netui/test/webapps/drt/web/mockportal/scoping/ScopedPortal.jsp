<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="mockportal.tld" prefix="mp" %>

<html>
<head>
<title>Scoped Portal (/mockportal/scoping)</title>
</head>

<body>

    <h3>Scoped Portal (/mockportal/scoping)</h3>
    <mp:mockPortal>
        <mp:mockPortlet portletID="portletA" pageFlowURI="/mockportal/scoping/ScopingtController.jpf" verbose="false" />
        <mp:mockPortlet portletID="portletB" pageFlowURI="/mockportal/scoping/ScopingController.jpf" verbose="false" />
    </mp:mockPortal>

</body>
</html>
