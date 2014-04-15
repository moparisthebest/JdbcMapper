<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    <h4>Anchor Null Binding Errors</h4>
    <p style="color:green">
        This test will test null binding against various attributes of the anchor that should raise errors.  
        The first set of test bind to
        the core anchor properties <b>action, href, clientAction, linkName</b>.  Of these, only the href accepts
        the value "".  All the rest will raise a runtime error.<br>
        This is a single page test.
    </p>
    <p style="color:green">
        The second test binds to <b>tagId</b> which will raise an error.
    </p>
    <hr>
    <ul>
    <li>action -- <netui:anchor action="${pageFlow.nullValue}">Null action</netui:anchor></li>
    <li>href -- <netui:anchor href="${pageFlow.nullValue}">Null href</netui:anchor></li>
    <li>clientAction -- <netui:anchor clientAction="${pageFlow.nullValue}">Null clientAction</netui:anchor></li>
    <li>linkName -- <netui:anchor linkName="${pageFlow.nullValue}">Null linkName</netui:anchor></li>
    </ul>
    <hr>
    <ul>
    <li>tagId -- <netui:anchor href="" tagId="${pageFlow.nullValue}">Null tagId</netui:anchor></li>
    </u>    
    </netui:body>
</netui:html>

  
