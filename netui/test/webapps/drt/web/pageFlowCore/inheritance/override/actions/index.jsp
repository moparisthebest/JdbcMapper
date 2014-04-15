<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<html>
<head>
<title>Page Flow Inheritance - Overriding Actions and Attributes</title>
</head>
<body>
    <h3>Page Flow Inheritance - Overriding Actions and Attributes</h3>

    <p>
        There are some attributes such as loginRequired, readOnly, and rolesAllowed that
        can be set on both the Controller and the Action annotations. This set of tests
        covers different scenarios of overriding actions and their attributes with
        inheritance. The default behavior for loginRequired and readOnly is to not use a
        &lt;set-property&gt; in the &lt;action&gt; of the generated struts module config
        file if the attribute is false or not set. On the runtime, no property implies
        false. However, if the property is not set for the delegating action, then the
        runtime uses the property of the delegate. Explicitly setting the attribute to
        false, should set the property and have a false value in the runtime.
    </p>
    <b>Test 1:</b>
    <ul>
        <li><a href="super1/begin.do">Controller attributes set to true</a></li>
        <li><a href="derived1a/begin.do">extended Controller attributes changed to false</a></li>
        <li><a href="derived1b/begin.do">extended Controller, no attributes changed</a></li>
    </ul>
    <b>Test 2:</b>
    <ul>
        <li><a href="super2/begin.do">Controller attributes not set explicitly</a></li>
        <li><a href="derived2a/begin.do">extended Controller attributes set to true</a></li>
        <li><a href="derived2b/begin.do">extended Controller attributes set to false</a></li>
    </ul>
</body>
</html>

	


			   
