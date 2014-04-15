<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui-data:declarePageInput name="form" type="accesskey.Controller.MyBean"/>
<netui:html>
    <head>
        <title>
            Web Application Page
        </title>
    </head>
    <body>
        <netui:anchor action="begin" accessKey="H">Home</netui:anchor><br/>
        <b>Attributes:</b><br/>
        <netui-data:repeater dataSource="pageInput.form.attributes">
            <netui-data:repeaterHeader><ol></netui-data:repeaterHeader>
            <netui-data:repeaterItem>
                <li>
                    <netui:span value="${container.item}" defaultValue="&nbsp;"></netui:span>
                </li>
            </netui-data:repeaterItem>
            <netui-data:repeaterFooter></ol></netui-data:repeaterFooter>
        </netui-data:repeater><br>
        <b>Type:</b> <netui:span value="${pageInput.form.type}"></netui:span><br/>
    </body>
</netui:html>
