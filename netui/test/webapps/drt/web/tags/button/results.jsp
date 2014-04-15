<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        Shaper: <b>${pageInput.form.shaper}</b>
        <br/>
        Style: <b>${pageInput.form.style}</b>
        <br/>
        <br/>
        <netui:anchor action="begin">start over</netui:anchor>
    </netui:body>
</netui:html>

  
