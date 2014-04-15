<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <body>
    <p>
        <netui:anchor action="resetTrees">Reset Trees</netui:anchor>
    </p>
    <ul>
    <li><netui:anchor action="goAttribute1">Attribute 1</netui:anchor> -- Apply attributes to nodes</li>
    <li><netui:anchor action="goAttribute2">Attribute 2</netui:anchor> -- Apply attributes to descendents</li>
    <li><netui:anchor action="goAttribute3">Attribute 3</netui:anchor> -- Apply attributes to descendents with overrides</li>
    <li><netui:anchor action="goAttribute4">Attribute 4</netui:anchor> -- Apply attributes to descendents with element attributes</li>
    <li><netui:anchor action="goAttribute5">Attribute 5</netui:anchor> -- Apply attributes to descendents with 
        overrides and element attributes</li>
    <li><netui:anchor action="goAttribute6">Attribute 6</netui:anchor> -- Apply attributes to descendents with 
        overrides at multiple levels</li>
    <li><netui:anchor action="goAttribute7">Attribute 7</netui:anchor> -- Apply attributes to descendents with 
        overrides at multiple levels and local scoped</li>
    <li><netui:anchor action="goAttribute8">Attribute 8</netui:anchor> -- Apply to descendents with multiple local scopes in between</li>
    </ul>
    </body>
</netui:html>

  
