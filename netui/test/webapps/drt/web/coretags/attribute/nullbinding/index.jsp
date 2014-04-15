<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Null binding in the Attribute tag</h4>
    <p style="color:green">This test verifies that values bound to the Attribute tag are 
    handled correctly.  In the first two, both <b>name</b> and <b>value</b> are required to 
    provide a value.  These will report an error.  The third <b>value</b> does not require a value
    an will be ignored.
    <br>
    This is a single page test.  
    </p>
    <ul>
    <li><netui:anchor action="begin">link
        <netui:attribute name="${pageFlow.nullValue}" value="nullName"/>
        </netui:anchor></li>
    <li><netui:anchor action="begin">link
        <netui:attribute name="nullFacet" facet="${pageFlow.nullValue}" value="nullFacet"/>
        </netui:anchor></li>
    <li><netui:anchor action="begin">link
        <netui:attribute name="nullValue" value="${pageFlow.nullValue}"/>
        </netui:anchor></li>
    </ul>
    </netui:body>
</netui:html>

  
