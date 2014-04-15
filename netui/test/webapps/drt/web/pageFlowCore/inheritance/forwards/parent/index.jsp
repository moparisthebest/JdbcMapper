<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<html>
<head>
    <title>Page Flow Forward Inheritance - Overriding Global Forwards</title>
</head>
<body>
    <h1>Page Flow Forward Inheritance - Overriding Global Forwards</h1>
    <h3>Global Forward - Parent Page Flow</h3>
    <br/>
    <netui:anchor action="toGlobalForward1">action to global forward 1</netui:anchor>
    <br/>
    <netui:anchor action="toGlobalForward2">action to global forward 2</netui:anchor>
    <br/>
    <netui:anchor action="toGlobal1HasActionLevelForward">to global forward 1 from action with an action level forward</netui:anchor>
    <br/>
    <netui:anchor action="toGlobal2HasActionLevelForward">to global forward 2 from action with an action level forward</netui:anchor>
    <br/>
    <a href="../begin.do">back to start</a>
</body>
</html>
