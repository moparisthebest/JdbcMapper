<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Verification of Tag Attribute Setting</h4>
    <p style="color:green">This test sets the exact same attributes for multiple
    tags.  Basically, it tests that if the JSP container is pooling tags, then
    the values are stored and reported correctly.  
    <br>
    This is a single page test.
    </p>
    <ul>
    <li><netui:anchor formSubmit="false" action="begin" accessKey="1"><netui:attribute name="lowsrc" value="low1.gif" />Link</netui:anchor></li>
    <li><netui:anchor formSubmit="false" action="begin" accessKey="2"><netui:attribute name="lowsrc" value="low2.gif" />Link</netui:anchor></li>
    <li><netui:anchor formSubmit="false" action="begin" accessKey="3"><netui:attribute name="lowsrc" value="low3.gif" />Link</netui:anchor></li>
    </ul>
    </netui:body>
</netui:html>

  
