<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<html>
    <head>
        <title>
            Bundle Test
        </title>
    </head>
    <body>
       <netui-data:declareBundle name="foo"
                bundlePath="error/Errors"/>
       <netui-data:declareBundle name="bar"
                bundlePath="error/Text"/>
        Bundle Messages:
        <ul>
        <li><b>[</b><netui:span value='${bundle["foo"].BundleString}'/><b>]</b>
        <li><b>[</b><netui:span value='${bundle["bar"].BundleString}'/><b>]</b>
        <li><b>[</b><netui:span value='${bundle["foo"].BundleString2}'/><b>]</b>
        <li><b>[</b><netui:span value='${bundle["bar"].BundleString2}'/><b>]</b>
        </ul>
    </body>
</html>
