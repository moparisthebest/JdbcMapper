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

        Success.  The data submitted was:
        <table>
            <tr>
                <td>symbol:</td>
                <td><b>${pageInput.stock.symbol}</b></td>
            </tr>
            <tr>
                <td>name:</td>
                <td><b>${pageInput.stock.name}</b></td>
            </tr>
            <tr>
                <td>price:</td>
                <td><b>${pageInput.stock.price}</b></td>
            </tr>
        </table>

        <br/>
        <netui:anchor action="begin">start over</netui:anchor>
    </netui:body>
</netui:html>



