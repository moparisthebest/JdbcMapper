<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>${pageContext.request.requestURI}</h3>

        This test ensures that the framework will not try to instantiate an abstract page flow
        controller.  If you get here, then the test is successful; otherwise, you would see an
        InstantiationException for the abstract controller class.
    </netui:body>
</netui:html>

  

