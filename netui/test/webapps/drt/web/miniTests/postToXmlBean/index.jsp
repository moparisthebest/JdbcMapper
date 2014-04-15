<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>${pageFlow.URI}</h3>

        This test confirms that we can post data directly into an XmlBean (and also that we can run
        validation rules on XmlBean properties).
        <br/>
        <br/>

        <netui:form action="submit">
            <table>
                <tr>
                    <td>symbol:</td>
                    <td><netui:textBox dataSource="actionForm.symbol"/><netui:error key="symbol"/></td>
                </tr>
                <tr>
                    <td>name:</td>
                    <td><netui:textBox dataSource="actionForm.name"/><netui:error key="name"/></td>
                </tr>
                <tr>
                    <td>price:</td>
                    <td><netui:textBox dataSource="actionForm.price"/><netui:error key="price"/></td>
                </tr>
            </table>
            <br/>
            <netui:button value="submit"/>
        </netui:form>
    </netui:body>
</netui:html>

  

