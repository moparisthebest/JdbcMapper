<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<html>
<head>
<title>Page Flow Inheritance - Overriding Overloaded Actions</title>
</head>
<body>
    <h3>Page Flow Inheritance - Overriding Overloaded Actions</h3>

    <p>Overloading Actions is really only useful for when you return from a nested flow. (See miniTests/overloadedActions/) Otherwise, from within a page flow, implicitly, you can only execute one of the overloaded actions.</p>
    <p>Overloaded actions require work to disambiguate the action paths in the module config. The "natural" mapping is determined as follows.</p>
    <p>As the NetUI compiler goes through the action methods of a page flow, the action mapping that takes no form has the highest precedence. If found, it replaces an existing overloaded action as the "natural" mapping for the given path.  Otherwise, if all the overloaded actions of a given name take forms, use the one with a form bean type that comes alphabetically before an existing one's.</p>
    <b>Test overloaded action, one without a form bean:</b>
    <br>The action executed should be the one without the form bean.
    <ul>
        <li><a href="super1/index.jsp">Super1</a></li>
        <li><a href="derived1a/index.jsp">Derived1a</a> - override action without form bean</li>
        <li><a href="derived1b/index.jsp">Derived1b</a> - override action with form bean</li>
    </ul>
    <b>Test overloaded action, both with a form bean (FormOne and FormTwo):</b>
    <br>The action executed should be the one with the form bean (FormOne).
    <ul>
        <li><a href="super2/index.jsp">Super2</a></li>
        <li><a href="derived2a/index.jsp">Derived2a</a> - override action with form bean, FormOne</li>
        <li><a href="derived2b/index.jsp">Derived2b</a> - override action with form bean, FormTwo</li>
    </ul>
</body>
</html>

	


			   
