<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html generateIdScope="true">
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <p style="color: green">This test verifies the base behavior of the
	label.  The label uses the for attribute as an IDREF to map the
	label to an HTML control ID attribute.  This requires that the
	for attribute be encoded to in the same way the tagId is encoded.
        </p>
        <netui:form tagId="form" action="postForm">
            <table>
                <tr valign="top">
                    <td><netui:label for="textbox" value="TextBox:" /></td>
                    <td>
                    <netui:textBox tagId="textbox"
			dataSource="actionForm.text1" />
                    </td>
                </tr>
                <tr valign="top">
                    <td><netui:label for="select" value="Select:" /></td>
                    <td>
                    <netui:select tagId="select" dataSource="actionForm.text2"
			optionsDataSource="${pageFlow.selectOptions}" />
                    </td>
                </tr>
                <tr valign="top">
                    <td><netui:label for="textArea" value="TextArea:" /></td>
                    <td>
                    <netui:textArea tagId="textArea"
			dataSource="actionForm.text3" />
                    </td>
                </tr>
                <tr valign="top">
                    <td><netui:label for="check1" value="Check One:" /></td>
                    <td>
                    <netui:checkBox tagId="check1"
			dataSource="actionForm.check1" />
                    </td>
                </tr>
                <tr valign="top">
                    <td><netui:label for="check2" value="Check Two:" /></td>
                    <td>
                    <netui:checkBox tagId="check2" dataSource="actionForm.check2" />
                    </td>
            </table>
            <br>
            &nbsp;
            <netui:button type="submit" value="postForm"/>
        </netui:form>
    </netui:body>
</netui:html>

  
