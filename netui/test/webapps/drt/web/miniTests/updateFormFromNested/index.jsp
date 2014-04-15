<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
          <netui:form action="submit">
        <table>
          <tr valign="top">
            <td>Name:</td>
            <td>
              <netui:textBox dataSource="actionForm.name"></netui:textBox>
              <span style="color:red"><netui:error key="name"/></span>
            </td>
          </tr>
          <tr valign="top">
            <td>Zip:</td>
            <td>
              <netui:textBox dataSource="actionForm.zip"></netui:textBox>
              <span style="color:red"><netui:error key="zip"/></span>
            </td>
          </tr>
        </table>
        <br/>&nbsp;
        <netui:button type="submit" value="get zip" action="getZip"/>
        <netui:button type="submit" value="submit"/>
      </netui:form>

    </netui:body>
</netui:html>

  
