<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="mockportal.tld" prefix="mp" %>

<html>
<head>
<title>Mock Portal with Trees</title>
</head>

<body>

    <h3>Mock Portal with Trees</h3>
    <mp:mockPortal>
        <mp:mockPortlet portletID="treeTestA" pageFlowURI="/mockportal/treetest/tree1/Controller.jpf" verbose="false" />
        <mp:mockPortlet portletID="treeTestB" pageFlowURI="/mockportal/treetest/tree2/Controller.jpf" verbose="false" />
    </mp:mockPortal>

</body>
</html>
