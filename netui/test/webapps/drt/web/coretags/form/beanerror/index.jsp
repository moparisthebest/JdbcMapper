<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    <h4>Explicit Form Bean Errors</h4>
    <p style="color:green">This verifies errors that are produced when the beanName, beanType, and beanScope attributes
    are not used correctly.  In the first, no beanType is specified.  In the second and third a bad beanType
    is specified.
    <br>
    This is a single page test.  
    </p>

    <hr>
    <p>No Type Specified:
    <netui:form action="begin" beanName="ctFormBeanError2"
	beanScope="session" style="display:inline">
            Type: <netui:textBox dataSource="actionForm.type" />&nbsp;<netui:button type="submit" value="Submit"></netui:button>
    </netui:form>
    </p>
    <p>Bad Type:
    <netui:form action="begin" beanName="ctFormBeanError1" beanType="coretags.form.bean.Controller$BadType" beanScope="session" style="display:inline">
            Type: <netui:textBox dataSource="actionForm.type" />&nbsp;<netui:button type="submit" value="Submit"></netui:button>
    </netui:form>
    </p>
    <p>Non-form Type Specified:
    <netui:form action="begin" beanName="ctFormBeanError3" beanType="java.lang.String" beanScope="session" style="display:inline">
            Type: <netui:textBox dataSource="actionForm.type" />&nbsp;<netui:button type="submit" value="Submit"></netui:button>
    </netui:form>
    <p>No Name Specified:
    <netui:form action="begin" beanType="coretags.form.beanerror.Controller$FormBean" beanScope="session" style="display:inline">
            Type: <netui:textBox dataSource="actionForm.type" />&nbsp;<netui:button type="submit" value="Submit"></netui:button>
    </netui:form>
    </p>
    </p>
    </netui:body>
</netui:html>

  
