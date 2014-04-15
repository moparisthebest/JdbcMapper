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
        <h4>Access Key Test</h4>
        <p style="color:green">
        This test defines a set of access keys that allow access to the various page components.  The access keys are marked
        next to the component.  These don't always work for every type of component.
        </p>
        <netui:anchor action="begin" accessKey="H"><b>(H)</b>ome</netui:anchor>
        <br>
        <netui:anchor action="post" accessKey="P"><b>(P)</b>ost</netui:anchor>
        <br>
        <netui:imageAnchor action="begin" src="./folder.gif" accessKey="W"></netui:imageAnchor><b>(W)</b>
        <netui:form action="post">
            <table>
                <tr valign="top">
                    <td>Color:</td>
                    <td>
                    <netui:radioButtonGroup dataSource="actionForm.color" defaultValue="red">
                        <netui:radioButtonOption value="${pageFlow.types[0]}" alt="${pageFlow.altText}" accessKey="8"></netui:radioButtonOption> <b>(8)</b><br/>
                        <netui:radioButtonOption value="${pageFlow.types[1]}" accessKey="9" alt="${pageFlow.altText}"></netui:radioButtonOption> <b>(9)</b><br/>
                        <netui:radioButtonOption value="${pageFlow.types[2]}" accessKey="0" alt="${pageFlow.altText}"></netui:radioButtonOption> <b>(0)</b><br/>
                    </netui:radioButtonGroup>
                   </td>
                </tr>
                <tr valign="top">
                    <td>Des<b>(C)</b>ription:</td>
                    <td>
                    <netui:textArea dataSource="actionForm.description" accessKey='C'/>
                    </td>
                </tr>
                <tr valign="top">
                    <td><b>(D)</b>isabled:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.disabled" accessKey="D" alt="${pageFlow.altText}"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>na<b>(M)</b>e:</td>
                    <td>
                    <netui:textBox dataSource="actionForm.name" accessKey="M" alt="${pageFlow.altText}"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td><b>(K)</b>ype:</td>
                    <td>
                    <netui:select dataSource="actionForm.type" optionsDataSource="${pageFlow.types}" accessKey="K"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Attributes:</td>
                    <td>
                    <netui:checkBoxGroup dataSource="actionForm.attributes" defaultValue="windows">
                        <netui:checkBoxOption value="${pageFlow.attributes[0]}" alt="${pageFlow.altText}" accessKey="1"></netui:checkBoxOption> <b>(1)</b><br/>
                        <netui:checkBoxOption value="${pageFlow.attributes[1]}" accessKey="2" alt="${pageFlow.altText}"></netui:checkBoxOption> <b>(2)</b><br/>
                        <netui:checkBoxOption value="${pageFlow.attributes[2]}" accessKey="3" alt="${pageFlow.altText}"></netui:checkBoxOption> <b>(3)</b><br/>
                    </netui:checkBoxGroup>
                    </td>
                </tr>
            </table>
            <br/>&nbsp;
            <netui:button value="Form Submit" type="submit" accessKey="F" alt="${pageFlow.altText}"/><b>(F)</b>
            <netui:button type="reset" value="Reset" accessKey="R" alt="${pageFlow.altText}"/><b>(R)</b>
            <netui:imageButton src="./folder.gif" accessKey="X"></netui:imageButton><b>(X)</b>
        </netui:form>
    </body>
</netui:html>
