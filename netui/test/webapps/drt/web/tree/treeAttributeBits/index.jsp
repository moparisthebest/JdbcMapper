<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <body>
    <h4>TreeHtmlAttributeInfo 1 - All True</h4>
        <table>
            <tr valign="top">
                <td>TreeHtmlAttributeInfo 1:</td>
                <td>
                <netui:span value="${pageFlow.attributeInfo1.onIcon}" defaultValue="&nbsp;"/><br>
                <netui:span value="${pageFlow.attributeInfo1.onSelectionLink}" defaultValue="&nbsp;"/><br>
                </td>
            </tr>
        </table>
    <h4>TreeHtmlAttributeInfo 2 - All False</h4>
        <table>
            <tr valign="top">
                <td>TreeHtmlAttributeInfo 2:</td>
                <td>
                <netui:span value="${pageFlow.attributeInfo2.onIcon}" defaultValue="&nbsp;"/><br>
                <netui:span value="${pageFlow.attributeInfo2.onSelectionLink}" defaultValue="&nbsp;"/><br>
                </td>
            </tr>
        </table>
    <hr>
        This is a basic test of the TreeHtmlAttributeInfo.  The test will flip the state of 
        the boolean values and then report the state in the page.  The two anchors below
        flip the state in different ways.
        <br>
        Flip the current state of each to the opposite: <netui:anchor action="flip">Flip Bits</netui:anchor><br>
        Flip every other bit to the opposite (1,3,5): <netui:anchor action="alternate">Alternate</netui:anchor>    
    </body>
</netui:html>

 
 