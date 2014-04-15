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
        <p style="color:green">This test will submit a form using an anchor 
        that is outside the form.  The form is told to output javascript which
        is then used by the anchor.
        <br>
        To run this test hit the anchor.  The value of a hidden field should then appear.
        <netui:form action="post" genJavaScriptFormSubmit="true">    
            <input type="hidden" name="foo" value="value">  
        </netui:form>

        <netui:anchor action="begin" onClick="anchor_submit_form('Netui_Form_0','post.do');return false;">Submit the form</netui:anchor>
        <hr>
        <p>Value: ${param.foo}</p>
    </body>
</netui:html>

  
