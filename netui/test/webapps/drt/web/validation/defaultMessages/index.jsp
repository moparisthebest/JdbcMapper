<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    
        <h3>Validation - Default Messages</h3>

        <netui:form action="validate">

            <table>
                <tr valign="top">
                    <td>required:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.required"/>
                        <netui:error key="required"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>minlength:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.minlength"/>
                        <netui:error key="minlength"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>maxlength:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.maxlength"/>
                        <netui:error key="maxlength"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>byte:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.byte"/>
                        <netui:error key="byte"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>short:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.short"/>
                        <netui:error key="short"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>int:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.int"/>
                        <netui:error key="int"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>float:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.float"/>
                        <netui:error key="float"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>long:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.long"/>
                        <netui:error key="long"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>double:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.double"/>
                        <netui:error key="double"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>date:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.date"/>
                        <netui:error key="date"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>range:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.range"/>
                        <netui:error key="range"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>creditcard:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.creditcard"/>
                        <netui:error key="creditcard"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>email:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.email"/>
                        <netui:error key="email"/>
                    </td>
                </tr>
            </table>
            
            <netui:button value="submit"/>
        </netui:form>
    </netui:body>
</netui:html>

  
