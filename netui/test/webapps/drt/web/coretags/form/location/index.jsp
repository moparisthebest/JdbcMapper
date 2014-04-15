<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <body>
        <netui:anchor tagId="Top"/>
        <h4>Form Location</h4>
        <p style="color:green">This test will verify that setting a location
	on the form results in the proper navigation to a location within the
	document.  In this case, submitting the form results in going to the
	bottom of this page.
	</p>
        <netui:form action="post" location="label">    
            <input type="hidden" name="foo" value="value">
            <netui:button value="submit">

	    </netui:button><br>
            <netui:anchor formSubmit="true">Submit the form
		<netui:parameter name="submit" value="link" />
	    </netui:anchor>
        </netui:form>
        <hr>
	<div style="height: 500px;border: 1pt solid black">
        </div>
        <netui:anchor tagId="label"/>
        <netui:anchor linkName="#Top">Go To Top</netui:anchor>
	<p style="color:green">These are the results from the form post</p>
        <p>Value: ${param.foo}</p>
        <p>Value: ${param.submit}</p>
    </body>
</netui:html>

  
