<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Normal Error reporting by the BindingUpdateErrors tag</h4>
    <p style="color:green">This test will verify that BindingUpdateErrors report errors when binding errors
    occur.  The way this happens is we use a form tag that posts to an action that doesn't take a form bean.
    The errors are reported either based upon an expression or all the errors are reported.  Both cases are
    found below.
    <br>
    To run this test, you just hit the current page -- you will see the errors displayed.
    </p>
    <p>
    <ul>
    <li>actionForm.name -- <netui:bindingUpdateErrors expression="actionForm.name" alwaysReport="true" /></li>
    <li>actionForm.type -- <netui:bindingUpdateErrors expression="actionForm.type" alwaysReport="true" /></li>
    <li>all -- <netui:bindingUpdateErrors alwaysReport="true" /></li>
    </ul>
    </p>
    <hr>
    <netui:form action="begin">
    <p>
    Name <netui:textBox dataSource="actionForm.name" /><br>
    Type <netui:textBox dataSource="actionForm.type" /><br>
    <netui:button value="Submit"/>
    </p>
    </netui:form>
    </netui:body>
</netui:html>

  
