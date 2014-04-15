<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Web Application Page
        </title>
    </head>
    <body>
        <p>
        Change the Session nameBean.name
        <netui:form action="Action2" beanType="tags.sessionBean.Controller$NameBean"
		beanScope="session" beanName="nameBean">
            Name: <netui:textBox dataSource="actionForm.name" />
            <netui:button value="Action3"></netui:button>
        </netui:form>
        <hr/> 
        Session nameBean.name = '<netui:span value="${sessionScope.nameBean.name}"/>'
        </p>
    </body>
</netui:html>
