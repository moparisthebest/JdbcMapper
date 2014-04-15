<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        name: <b>${pageInput.form.name}</b>
        <br/>
        address: <b>${pageInput.form.address}</b>
        <br/>
        city: <b>${pageInput.form.city}</b>
        <br/>
        state: <b>${pageInput.form.state}</b>
        <br/>
        zip: <b>${pageInput.form.zip}</b>
        <br/>
        <br/>
        <netui:anchor action="begin">start over</netui:anchor>
    </netui:body>
</netui:html>

  
