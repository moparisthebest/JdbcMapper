<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <netui:form action="post">
            <table>
                <tr valign="top">
                    <td>Text:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.text" tagId="textbox" />
                    </td>
                </tr>
            </table>
            <br>
            <netui:button tagId="button" value="Submit"/>&nbsp;
            <netui:imageButton tagId="imageButton" src="insert.gif"></netui:imageButton>
        </netui:form>
        <hr>
        Post Value: <netui:label value="${pageFlow.text}"/>
    
    </netui:body>
</netui:html>

  
