<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>Simple Actions</h3>


        <netui:form action="begin">
            choice ("page1", "page2", or something unrecognized):
            <netui:textBox dataSource="pageFlow.choice" />
            <netui:button value="submit"/>
        </netui:form>
        
        <netui:anchor action="conditional">
            conditional action
        </netui:anchor>

        <br/>
        <br/>
        
        <table border="1">
            <tr>
                <td>
                    choice
                </td>
                <td>
                    result
                </td>
            </tr>
            <tr>
                <td>
                    page1
                </td>
                <td>
                    page1.jsp
                </td>
            </tr>
            <tr>
                <td>
                    page2
                </td>
                <td>
                    page2.jsp
                </td>
            </tr>
            <tr>
                <td>
                    <i>anything else</i>
                </td>
                <td>
                    page3.jsp
                </td>
            </tr>
        </table>


    </netui:body>
</netui:html>

  
