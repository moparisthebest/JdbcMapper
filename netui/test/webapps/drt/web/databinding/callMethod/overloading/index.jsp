<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui-html"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>CallMethod Overloading Test</title>
  </head>

  <body>
    <b>CallMethod Overloading Test</b>
<br/>
<br/>
<b>foo()</b>
<netui-data:callPageFlow method="foo" resultId="test1"/>
<netui-html:span value="${pageScope.test1}"/>
<br/>
<b>foo(Integer)</b>
<netui-data:callPageFlow method="foo" resultId="test2">
    <netui-data:methodParameter value="42" type="java.lang.Integer"/>
</netui-data:callPageFlow>
<netui-html:span value="${pageScope.test2}"/>
<br/>
<b>foo(int)</b>
<netui-data:callPageFlow method="foo" resultId="test3">
    <netui-data:methodParameter value="42" type="int"/>
</netui-data:callPageFlow>
<netui-html:span value="${pageScope.test3}"/>
<br/>
<br/>
<b>foo(String, int)</b>
<netui-data:callPageFlow method="foo" resultId="test4">
    <netui-data:methodParameter value="null" type="java.lang.String"/>
    <netui-data:methodParameter value="42" type="int"/>
</netui-data:callPageFlow>
<netui-html:span value="${pageScope.test4}"/>
<br/>
<br/>
<b>foo(String, Integer)</b>
<netui-data:callPageFlow method="foo" resultId="test5">
    <netui-data:methodParameter value="null" type="java.lang.String"/>
    <netui-data:methodParameter value="42" type="java.lang.Integer"/>
</netui-data:callPageFlow>
<netui-html:span value="${pageScope.test5}"/>
<br/>
<br/>
<b>foo(String)</b>
<netui-data:callPageFlow method="foo" resultId="test6">
    <netui-data:methodParameter value="42" type="java.lang.String"/>
</netui-data:callPageFlow>
<netui-html:span value="${pageScope.test6}"/>
<br/>
<br/>
<b>Error tests</b>
<br/>
<b>foo(int)</b>
<netui-data:callPageFlow method="foo" resultId="test100">
    <netui-data:methodParameter type="java.lang.STring" value="42"/>
</netui-data:callPageFlow>
<netui-html:span value="${pageScope.test100}"/>
<br/>
<br/>
<b>foo(String, int)</b>
<netui-data:callPageFlow method="foo" resultId="test101">
    <netui-data:methodParameter value="42" type="int"/>
    <netui-data:methodParameter value="asdf" type="java.lang.String"/>
</netui-data:callPageFlow>
<netui-html:span value="${pageScope.test101}"/>
<br/>
<br/>
<b>foo(Integer)</b>
<netui-data:callPageFlow method="foo" resultId="test102">
    <netui-data:methodParameter value="42" type="FooBar"/>
</netui-data:callPageFlow>
<netui-html:span value="${pageScope.test102}"/>
<br/>
<br/>
<b>foo(?)</b>
<netui-data:callPageFlow method="foo" resultId="test103">
    <netui-data:methodParameter value="42" type=""/>
</netui-data:callPageFlow>
<netui-html:span value="${pageScope.test103}"/>
<br/>
<br/>
    <hr>
  </body>
</html>
