<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    <h4>Explicit Form Bean</h4>
    <p style="color:green">This test will create a bean that is used to load the form
    values.  Three attributes are used to cause a bean to be created and then used to populate
    the form.  The <b>name</b> attribute names the bean in the attribute map.  The <b>type</b>
    specifies the type of the bean.  The <b>scope</b> defines the scope to look for the bean.  This
    is either session or request.
    <br>
    This is a single page test.  
    </p>
    <hr>
    <netui:form action="begin" beanName="ctFormBean"
	beanType="coretags.form.bean.Controller$FormBean" beanScope="session">
        <p>
            Type: <netui:textBox dataSource="actionForm.type" /><br>
            <netui:button type="submit" value="Submit"></netui:button>
        </p>
    </netui:form>
    </netui:body>
</netui:html>

  
