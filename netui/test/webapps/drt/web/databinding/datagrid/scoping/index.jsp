<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="mockportal.tld" prefix="mp" %>

<html>
<head>
<title>Mock Portal (/mockportal)</title>
</head>
<body>

    <h3>Mock Portal (/mockportal)</h3>
    <mp:mockPortal>
        <mp:mockPortlet portletID="grid1"
                        pageFlowURI="/databinding/datagrid/scoping/grid1/begin.do"
                        verbose="false"/>
    </mp:mockPortal>

</body>
</html>
