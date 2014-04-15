<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    <h4>HtmlBase Attribute Binding</h4>
    <p style="color:green">
        This test will null bind to a bunch of attributes on which are defined on the HtmlBaseTag class.  These
        attributes are exposed on a bunch of the HTML tags.  In all cases below, when a value is resolved
        to null, the value is ignored.  All of the anchors in this test should result in the same markup.<br>
        This is a single page test. 
    </p>
    <hr>
    <ul>
    <li><netui:anchor action="begin" style="${pageFlow.nullValue}">Link</netui:anchor> -- style</li>
    <li><netui:anchor action="begin" styleClass="${pageFlow.nullValue}">Link</netui:anchor> -- styleClass</li>
    <li><netui:anchor action="begin" title="${pageFlow.nullValue}">Link</netui:anchor> -- title</li>
    <li><netui:anchor action="begin" onClick="${pageFlow.nullValue}">Link</netui:anchor> -- onClick</li>
    <li><netui:anchor action="begin" onDblClick="${pageFlow.nullValue}">Link</netui:anchor> -- onDblClick</li>
    <li><netui:anchor action="begin" onKeyDown="${pageFlow.nullValue}">Link</netui:anchor> -- onKeyDown</li>
    <li><netui:anchor action="begin" onKeyPress="${pageFlow.nullValue}">Link</netui:anchor> -- onKeyPress</li>
    <li><netui:anchor action="begin" onKeyUp="${pageFlow.nullValue}">Link</netui:anchor> -- onKeyUp</li>
    <li><netui:anchor action="begin" onMouseDown="${pageFlow.nullValue}">Link</netui:anchor> -- onMouseDown</li>
    <li><netui:anchor action="begin" onMouseMove="${pageFlow.nullValue}">Link</netui:anchor> -- onMouseMove</li>
    <li><netui:anchor action="begin" onMouseOut="${pageFlow.nullValue}">Link</netui:anchor> -- onMouseOut</li>
    <li><netui:anchor action="begin" onMouseOver="${pageFlow.nullValue}">Link</netui:anchor> -- onMouseOver</li>
    <li><netui:anchor action="begin" onMouseUp="${pageFlow.nullValue}">Link</netui:anchor> -- onMouseUp</li>
    <li><netui:anchor action="begin" dir="${pageFlow.nullValue}">Link</netui:anchor> -- dir</li>
    <li><netui:anchor action="begin" lang="${pageFlow.nullValue}">Link</netui:anchor> -- lang</li>
    </ul>
    <hr>
    
    </netui:body>
</netui:html>


  
  
