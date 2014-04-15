<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<html>
<head>
    <title>Page Flow Forward Inheritance - Overriding Global Forwards</title>
</head>
<body>
    <h3>Page Flow Forward Inheritance - Overriding Global Forwards</h3>

    <p>
        The Jpf.Forward can be set as a global forward on the
        Controller. The following tests that the overridden and
        inherited forwards behave as expected.
    </p>
    <ul>
        <li><a href="parent/begin.do">Parent Controller</a></li>
        <li><a href="override/begin.do">Extended Controller with overriden global forward</a></li>
    </ul>
</body>
</html>
