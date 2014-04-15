<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        name is: ${pageInput.form.name}
        <br/>
        zip is: ${pageInput.form.zip}
        <br/>
        <br/>
        <netui:anchor action="begin">start over</netui:anchor>
    </netui:body>
</netui:html>

  
