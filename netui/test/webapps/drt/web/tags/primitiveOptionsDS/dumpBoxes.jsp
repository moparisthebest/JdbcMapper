<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Web Application Page
        </title>
    </head>
    <body>
        <p>
        <netui-data:repeater dataSource="pageFlow.boxes">
            <netui-data:repeaterHeader><table class="tablebody" border="1"></netui-data:repeaterHeader>
            <netui-data:repeaterItem>
                <tr>
                    <td><netui:span value="${container.item}" defaultValue="&nbsp;"></netui:span></td>
                </tr>
            </netui-data:repeaterItem>
            <netui-data:repeaterFooter></table></netui-data:repeaterFooter>
        </netui-data:repeater>
        </p>
    </body>
</netui:html>
