<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<html>
<head>
    <title>Page Flow Inheritance - Inherit useFormBean annotation attribute</title>
</head>
<body>
    <h3>Page Flow Inheritance - Inherit useFormBean annotation attribute</h3>
    <p>
        The Jpf.Action and Jpf.SimpleAction annotations include the
        useFormBean attribute to indicate that the form bean to use
        with the action is a named member variable, rather than a
        newly-created instance.  This is referred to as a "flow-scoped
        form bean". The following tests that the inherited actions
        with useFormBean for a form behave as expected.
    </p>
    <ul>
        <li><a href="simpleActions/begin.do">Parent Controller - Simple Action</a></li>
        <li><a href="derivedSimpleActions/begin.do">Derived Controller - Inherited Simple Action</a></li>
        <li><a href="actions/begin.do">Parent Controller - Action</a></li>
        <li><a href="derivedActions/begin.do">Derived Controller - Inherited Action</a></li>
    </ul>
</body>
</html>
