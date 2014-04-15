<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <body>
        <h4>Parameters on Buttons and Anchors</h4>
        <p style="color:green">This test will verify that setting a location
	on the form results in the proper navigation to a location within the
	document.  In this case, submitting the form results in going to the
	bottom of this page.
	</p>
        <netui:form action="post">
            <netui:button value="submit">
		<netui:parameter name="submit" value="submit" />
	    </netui:button><br>
            <netui:button value="submit with action" action="post">
		<netui:parameter name="submit" value="submit with action" />
	    </netui:button><br>
            <netui:anchor formSubmit="true">Submit the form
		<netui:parameter name="submit" value="link" />
	    </netui:anchor>
        </netui:form>
        <hr>
        <p>Value: ${param.submit}</p>
    </body>
</netui:html>

  
