<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui-data:declarePageInput name="formInput" type="tags.sessionBean.Controller.NewFormBean"/>
<netui:html>
    <head>
        <title>
            Web Application Page
        </title>
    </head>
    <body>
        Page Input Form: <netui:span value="${pageInput.formInput.name}"/><br />
        Session My Bean: <netui:span value="${sessionScope.nameBean.name}"/>
        <hr />
        <netui:anchor action="Action3">Return to Form</netui:anchor>
    </body>
</netui:html>
