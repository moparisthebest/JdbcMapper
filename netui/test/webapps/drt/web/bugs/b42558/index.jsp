<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>B42558 Repro</title>
  </head>

  <body>
    <h1>B42558 Repro</h1>

<netui-data:callPageFlow method="sayHello" resultId="helloText" failOnError="true">
    <netui-data:methodParameter value="Homer"/>
</netui-data:callPageFlow>

<br/>
Page Flow said: <netui:span value="${pageScope.helloText}"/>
<br/>
<br/>
  </body>
</html>
