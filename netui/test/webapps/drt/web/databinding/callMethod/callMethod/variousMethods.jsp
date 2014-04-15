<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Various callMethod Test</title>
  </head>
  <body>
    <b>Various callMethod Test</b>
<%
    databinding.callMethod.CallMethodType cm = new databinding.callMethod.CallMethodType();
request.setAttribute("callMethodType", cm);
%>
<b>Calls on an arbitrary Java object with callMethod</b>
<br/>
<!-- test 1 -->
<netui-data:callMethod object="${requestScope.callMethodType}" resultId="publicMethod" method="publicMethod"/>
<netui:span value="${pageScope.publicMethod}"/>
<br/>
<br/>
<!-- test 2 -->
<netui-data:callMethod object="${requestScope.callMethodType}" resultId="protectedMethod" method="protectedMethod"/>
<netui:span value="${pageScope.protectedMethod}"/>
<br/>
<br/>
<!-- test 3 -->
<netui-data:callMethod object="${requestScope.callMethodType}" resultId="privateMethod" method="privateMethod"/>
<netui:span value="${pageScope.privateMethod}"/>
<br/>
<br/>
<!-- test 4 -->
<netui-data:callMethod object="${requestScope.callMethodType}" resultId="publicStaticMethod" method="publicStaticMethod"/>
<netui:span value="${pageScope.publicStaticMethod}"/>
<br/>
<br/>
<!-- test 5 -->
<netui-data:callMethod object="${requestScope.callMethodType}" resultId="publicMethodZeroArg" method="publicMethodZeroArg"/>
<netui:span value="${pageScope.publicMethodZeroArg}"/>
<br/>
<br/>
<!-- test 6 -->
<netui-data:callMethod object="${requestScope.callMethodType}" resultId="publicMethodOneArg" method="publicMethodOneArg">
    <netui-data:methodParameter value="123"/>
</netui-data:callMethod>
<netui:span value="${pageScope.publicMethodOneArg}"/>
<br/>
<br/>
<!-- test 7 -->
<netui-data:callMethod object="${requestScope.callMethodType}" resultId="publicMethodTwoArg" method="publicMethodTwoArg">
    <netui-data:methodParameter value="123"/>
    <netui-data:methodParameter value="456"/>
</netui-data:callMethod>
<netui:span value="${pageScope.publicMethodTwoArg}"/>
<br/>
<br/>
<br/>
    <hr>
    <address><a href="mailto:ekoneil@bea.com"></a></address>
<!-- Created: Wed Sep 10 12:14:17 Mountain Daylight Time 2003 -->
<!-- hhmts start -->
Last modified: Wed Sep 10 12:43:31 Mountain Daylight Time 2003
<!-- hhmts end -->
  </body>
</html>
