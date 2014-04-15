<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <body>
        action output string: <b><netui:span value="${pageInput.goodString}"/></b>
        <br>
        action output array: <netui-data:repeater dataSource="pageInput.goodArray">
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
        <br>
        <netui:anchor action="good1">good1</netui:anchor><br>
        <netui:anchor action="good2">good2</netui:anchor><br>
        <netui:anchor action="missingButNullable1">missingButNullable1</netui:anchor><br>
        <netui:anchor action="missingButNullable2">missingButNullable2</netui:anchor><br>
        <netui:anchor action="mismatched1">mismatched1</netui:anchor><br>
        <netui:anchor action="mismatched2">mismatched2</netui:anchor><br>
        <netui:anchor action="missingNotNullable1">missingNotNullable1</netui:anchor><br>
        <netui:anchor action="missingNotNullable2">missingNotNullable2</netui:anchor><br>
        <netui:anchor action="lots">lots</netui:anchor><br>
    </body>
</netui:html>

  
