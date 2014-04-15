<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    <h4>Anchor Null Binding</h4>
    <p style="color:green">
        This test will null bind to a bunch of attributes on the anchor.  These should all be ignored without
        warning.  In the first case, the anchors should all be exactly the same.  The attribute bound to a
        null expression is ignored in the output.  The second set of anchor have default values for there
        parameters.<br>
        This is a single page test. 
    </p>
    <hr>
    <ul>
    <li><netui:anchor action="begin" charSet="${pageFlow.nullValue}">Link</netui:anchor> -- charSet</li>
    <li><netui:anchor action="begin" coords="${pageFlow.nullValue}">Link</netui:anchor> -- coords</li>
    <li><netui:anchor action="begin" hrefLang="${pageFlow.nullValue}">Link</netui:anchor> -- hrefLang</li>
    <li><netui:anchor action="begin" location="${pageFlow.nullValue}">Link</netui:anchor> -- location</li>
    <li><netui:anchor action="begin" onBlur="${pageFlow.nullValue}">Link</netui:anchor> -- onBlur</li>
    <li><netui:anchor action="begin" onFocus="${pageFlow.nullValue}">Link</netui:anchor> -- onFocus</li>
    <li><netui:anchor action="begin" rel="${pageFlow.nullValue}">Link</netui:anchor> -- rel</li>
    <li><netui:anchor action="begin" rev="${pageFlow.nullValue}">Link</netui:anchor> -- rev</li>
    <li><netui:anchor action="begin" shape="${pageFlow.nullValue}">Link</netui:anchor> -- shape</li>
    <li><netui:anchor action="begin" target="${pageFlow.nullValue}">Link</netui:anchor> -- target</li>
    <li><netui:anchor action="begin" type="${pageFlow.nullValue}">Link</netui:anchor> -- type</li>
    </ul>
    <ul>
    <li><netui:anchor action="begin" accessKey="${pageFlow.nullValue}">Link</netui:anchor> -- accessKey</li>
    <li><netui:anchor action="begin" tabindex="${pageFlow.nullValue}">Link</netui:anchor> -- tabindex</li>
    </ul>
    <hr>
    
    </netui:body>
</netui:html>


  
