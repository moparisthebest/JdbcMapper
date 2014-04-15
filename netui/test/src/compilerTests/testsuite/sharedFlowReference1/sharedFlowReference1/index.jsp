<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
  <head>
    <title>Web Application Page</title>
    <netui:base/>
  </head>
  <netui:body>
  	Use a shared action to get the value of <netui:anchor action="getShared"  >sharedMessage: </netui:anchor>
  	<netui:label value="${pageInput.message}"/>
  </netui:body>
</netui:html>
