<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Null binding in the Beehavior tag</h4>
    <p style="color:green">This test verifies that values bound to the
    Behavior tag are handled correctly.  In the first two, both <b>name</b>
    and <b>facet</b> are required to provide a value.  These will report
    an error.  The third  case generates an error because the parent
    doesn't support IBehaviorConsumer.
    <br>
    This is a single page test.
    </p>
    <ul>
    <li><netui:behavior name="${pageFlow.nullValue}" value="nullName"/></li>
    <li><netui:behavior name="nullFacet" facet="${pageFlow.nullValue}" value="nullFacet"/></li>
    <li><netui:behavior name="nullValue" value="${pageFlow.nullValue}"/></li>
    </ul>
    </netui:body>
</netui:html>

  
