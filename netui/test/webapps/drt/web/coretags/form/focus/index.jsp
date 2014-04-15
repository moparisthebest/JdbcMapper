<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <body>
        <h4>Post Form With an Anchor</h4>
        <p style="color:green">This tests the focus.  The focus for the
	    form should be on the <b>Text 2</b> textbox when the page is
	    loaded.
        </p>
        <netui:form action="post" focus="text2">    
	    Text 1: <netui:textBox tagId="text1" dataSource="actionForm.text1" /><br>
	    Text 2: <netui:textBox tagId="text2" dataSource="actionForm.text2" /><br>
	    Text 3: <netui:textBox tagId="text3" dataSource="actionForm.text3" /><br>
            <netui:button tagId="button" value="submit" />
        </netui:form>
        <hr>
        Results: ${pageFlow.results}
    </body>
</netui:html>

  
