<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui-html"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>declareBundle Binding Error Tests</title>
    <netui-data:declareBundle name="bundle1" bundlePath="properties/bundle1"/>
    <netui-data:declareBundle language="de" name="germanBundle1" bundlePath="properties/bundle1"/>
    <netui-data:declareBundle name="bundle2" bundlePath="properties.resources.bundle2"/>
    <netui-data:declareBundle name="!@$%%^&" bundlePath="properties/resources/strangeName"/>
  </head>
  <body>
    <b>Bundle Binding Tests</b>
<b>Empty declareBundle.name attribute</b><br/>
<netui-data:declareBundle name="" bundlePath="/properties/nonExistent"/>
<br/>
<br/>
<b>Empty declareBundle.bundlePath attribute</b><br/>
<netui-data:declareBundle name="foo" bundlePath=""/>
<br/>
<b>Empty declareBundle.bundlePath and declareBundle.name attributes</b><br/>
<netui-data:declareBundle name="foo" bundlePath=""/>
<br/>
<%
    pageContext.setAttribute("bundle", new java.util.ArrayList());
%>
<b>Whacking the BundleContext.KEY attribute with another type</b><br/>
<netui-data:declareBundle name="foo" bundlePath="/properties/foo"/>
<br/>
<br/>
  </body>
</html>
