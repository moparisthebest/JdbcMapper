<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <body>
        <netui:anchor action="resetTrees">Reset Trees</netui:anchor>
        <ul>
        <li><netui:anchor action="goAttribute1">Attribute1</netui:anchor> -- Basic test of setting multiple attributes</li>
        <li><netui:anchor action="goAttribute2">Attribute2</netui:anchor> -- Single descendent, overridden by multi</li>
        <li><netui:anchor action="goAttribute3">Attribute3</netui:anchor> -- Multi descendent</li>
        <li><netui:anchor action="goAttribute4">Attribute4</netui:anchor> -- Multi descendent, overridden by multi</li>
        <li><netui:anchor action="goAttribute5">Attribute5</netui:anchor> -- Multi descendent, with overlapping attributes being set</li>
        </ul>
    </body>
</netui:html>

  
