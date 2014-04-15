<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Message Test</title>
  </head>
  <body>
    <b>Message Test</b>

<!-- TEST 1 -->
<%
    pageContext.setAttribute("message", new String("Start Message {0} Middle Message {1} End Message"));
%>
<netui-data:message value="${pageScope.message}" resultId="endMessage">
    <netui-data:messageArg value="Arg1"/>
    <netui-data:messageArg value="Arg2"/>
</netui-data:message>
<br/>
Test 1: <netui:span value="${pageScope.endMessage}"/>

<!-- TEST 2 -->
<%
    pageContext.setAttribute("message", new String("No Substitution"));
%>
<netui-data:message value="${pageScope.message}" resultId="endMessage">
    <netui-data:messageArg value="Arg1"/>
    <netui-data:messageArg value="Arg2"/>
</netui-data:message>
<br/>
Test 2: <netui:span value="${pageScope.endMessage}"/>

<!-- TEST 3 -->
<%
    pageContext.setAttribute("message", new String("{-1} {1} {2}"));
%>
<netui-data:message value="${pageScope.message}" resultId="endMessage">
    <netui-data:messageArg value="Arg1"/>
    <netui-data:messageArg value="Arg2"/>
</netui-data:message>
<br/>
Test 3: <netui:span value="${pageScope.endMessage}"/>

<!-- TEST 4 -->
<%
    pageContext.setAttribute("message", new String("foo"));
%>
<netui-data:message value="${pageScope.message}" resultId="endMessage">
    <netui-data:messageArg value="Arg1"/>
    <netui-data:messageArg value="Arg2"/>
</netui-data:message>
<br/>
Test 4: <netui:span value="${pageScope.endMessage}"/>

<!-- TEST 5 -->
<netui-data:declareBundle name="bundle1" bundlePath="properties.bundle1"/>
<netui-data:message value="${bundle.bundle1.messageWithSubs}" resultId="endMessage">
    <netui-data:messageArg value="Arg1"/>
    <netui-data:messageArg value="Arg2"/>
</netui-data:message>
<br/>
Test 5: <netui:span value="${pageScope.endMessage}"/>

<br/>
  </body>
</html>
