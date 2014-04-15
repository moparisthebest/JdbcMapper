<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui-data:declarePageInput name="form" type="bug.BugController.FormBean"/>
<netui:html>
    <head>
        <title>
            Web Application Page
        </title>
    </head>
    <body>
        Date:<netui:span value="${pageInput.form.date}">
            <netui:formatDate pattern="M/d/yy  HH:mm"/>
        </netui:span><br/> 
        Type:<netui:span value="${pageInput.form.type}"></netui:span><br/>   
    </body>
</netui:html>
