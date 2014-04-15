<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="mockportal.tld" prefix="mp" %>

<html>
<head>
<title>Scoped Portal (/mockportal/scoping2)</title>
</head>

<body>

    <h3>Scoped Portal (/mockportal/scoping2)</h3>
    <mp:mockPortal>
        <mp:mockPortlet portletID="portletA2" pageFlowURI="/mockportal/scoping2/ScopingtController.jpf" verbose="false" />
        <mp:mockPortlet portletID="portletB2" pageFlowURI="/mockportal/scoping2/ScopingController.jpf" verbose="false" />
    </mp:mockPortal>

</body>
</html>
