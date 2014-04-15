<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            lots of action outputs:
        </title>
    </head>
    <body>
        pi1: <netui:span value="${pageInput.pi1}"/>
        <br>
        pi2: <netui-data:repeater dataSource="pageInput.pi2">
                <netui-data:repeaterHeader>
                    <ul>
                </netui-data:repeaterHeader>
                <netui-data:repeaterItem>
                    <li><netui:span value="${container.item}"/></li>
                </netui-data:repeaterItem>
                <netui-data:repeaterFooter>
                    </ul>
                </netui-data:repeaterFooter>
             </netui-data:repeater>
        <br>
        pi3: <netui-data:repeater dataSource="pageInput.pi3">
                <netui-data:repeaterHeader>
                    <ul>
                </netui-data:repeaterHeader>
                <netui-data:repeaterItem>
                    <netui-data:repeater dataSource="container.item">
                        <netui-data:repeaterHeader>
                            subarray: <ul>
                        </netui-data:repeaterHeader>
                        <netui-data:repeaterItem>
                            <li><netui:span value="${container.item}"/></li>
                        </netui-data:repeaterItem>
                        <netui-data:repeaterFooter>
                            </ul>
                        </netui-data:repeaterFooter>
                     </netui-data:repeater>
                </netui-data:repeaterItem>
                <netui-data:repeaterFooter>
                    </ul>
                </netui-data:repeaterFooter>
             </netui-data:repeater>

        <br>
        <br>
        <netui:anchor action="begin">back to start</netui:anchor>
    </body>
</netui:html>
