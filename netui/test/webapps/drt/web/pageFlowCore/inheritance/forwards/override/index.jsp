<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<html>
<head>
    <title>Page Flow Forward Inheritance - Overriding Global Forwards</title>
</head>
<body>
    <h1>Page Flow Forward Inheritance - Overriding Global Forwards</h1>
    <h3>Global Forward - Inherited Page Flow</h3>
    <br/>
    <netui:anchor action="toGlobalForward1">inherited action to overridden global forward 1</netui:anchor>
    <br/>
    <netui:anchor action="toGlobalForward2">inherited action to inherited global forward 2</netui:anchor>
    <br/>
    <netui:anchor action="myForward1">action to overridden global forward 1</netui:anchor>
    <br/>
    <netui:anchor action="myForward2">action to inherited global forward 2</netui:anchor>
    <br/>
    <netui:anchor action="toGlobal1HasActionLevelForward">to overridden global forward 1 from inherited action with an action level forward</netui:anchor>
    <br/>
    <netui:anchor action="toGlobal2HasActionLevelForward">to inherited global forward 2 from inherited action with an action level forward</netui:anchor>
    <br/>
    <netui:anchor action="localToGlobal1HasActionLevelForward">to overridden global forward 1 from local action with an action level forward</netui:anchor>
    <br/>
    <netui:anchor action="localToGlobal2HasActionLevelForward">to inherited global forward 2 from local action with an action level forward</netui:anchor>
    <br/>
    <a href="../begin.do">back to start</a>
</body>
</html>
