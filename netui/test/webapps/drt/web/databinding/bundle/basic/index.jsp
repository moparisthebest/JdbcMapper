<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Simple Bundle Tests</title>
  </head>
  <body>
    <b>Simple Bundle Tests</b>
    <netui-data:declareBundle name="myBundle" bundlePath="properties.bundle1"/>
    <p>
    <netui:span value="${bundle.myBundle.message1}"/><br/>
    <br/>
    Multi-word message key: <netui:span value="${bundle.myBundle['multi.word.key']}"/><br/>
    <br/>
    </p>
  </body>
</html>
