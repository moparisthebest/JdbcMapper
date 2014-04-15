<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.beehive.netui.pageflow.scoping.ScopedRequest"%>
<%@ page import="org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>

<%
    ScopedRequest scopedRequest = ScopedServletUtils.unwrapRequest( request );
%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
      <netui:form action="submit">
        <table>
          <tr valign="top">
            <td><b>Name:<b></td>
            <td>
              <netui:textBox dataSource="actionForm.name"/>
              <span style="color:red"><netui:error key="name"/></span>
            </td>
          </tr>
          <tr valign="top">
            <td>Address:</td>
            <td>
              <netui:textBox dataSource="actionForm.address"/>
            </td>
          </tr>
          <tr valign="top">
            <td>City:</td>
            <td>
              <netui:textBox dataSource="actionForm.city"/>
            </td>
          </tr>
          <tr valign="top">
            <td><b>State:</b></td>
            <td>
              <netui:textBox tagId="stateField" dataSource="actionForm.state"/>
              <span style="color:red"><netui:error key="state"/></span>
            </td>
          </tr>
          <tr valign="top">
            <td><b>Zip:<b></td>
            <td>
              <netui:textBox tagId="zipField" dataSource="actionForm.zip"/>
              <span style="color:red"><netui:error key="zip"/></span>

              <netui:anchor action="getZip" popup="true">
                look up
                <netui:configurePopup location="false" width="550" height="150">
                    <netui:retrievePopupOutput tagIdRef="stateField" dataSource="outputFormBean.state"/>
                    <netui:retrievePopupOutput tagIdRef="zipField" dataSource="outputFormBean.zip"/>
                </netui:configurePopup>
              </netui:anchor>

            </td>
          </tr>
        </table>
        <br/>
        <netui:button type="submit" value="submit"/>
      </netui:form>

    </netui:body>
</netui:html>
