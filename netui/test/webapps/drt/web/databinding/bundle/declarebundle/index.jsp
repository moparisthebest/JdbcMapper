<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui-html"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<netui-data:declareBundle name="bundle1" bundlePath="properties/bundle1"/>
<netui-data:declareBundle language="de" name="germanBundle1" bundlePath="properties/bundle1"/>
<netui-data:declareBundle name="bundle2" bundlePath="properties.resources.bundle2"/>
<netui-data:declareBundle name="!@$%%^&" bundlePath="properties/resources/strangeName"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Bundle Binding Tests</title>
  </head>
  <body>
    <b>Bundle Binding Tests</b>
    <br/>
    bundle.bundle1.message1: <netui-html:span value="${bundle.bundle1.message1}"/><br/>
    bundle.germanBundle1.message1: <netui-html:span value="${bundle.germanBundle1.message1}"/><br/>
    bundle["!@$%%^&"].message1: <netui-html:span value='${bundle["!@$%%^&"].message1}'/><br/>
    bundle["bundle2"].message1: <netui-html:span value='${bundle["bundle2"].message1}'/><br/>
    bundle.bundle2.message2: <netui-html:span value='${bundle.bundle2.message2}'/><br/>
  </body>
</html>
