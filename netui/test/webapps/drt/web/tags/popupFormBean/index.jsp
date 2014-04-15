<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
      <h2>Test - Passing a FormBean to a Popup</h2>

      <netui:form action="submit">
        <table>
          <tr valign="top">
            <td><b>Dealer:</b></td>
            <td>
              <netui:textBox dataSource="actionForm.dealer"/>
            </td>
          </tr>
          <tr valign="top">
            <td><b>Color:</b></td>
            <td>
              <netui:checkBoxGroup dataSource="actionForm.color" optionsDataSource="${pageFlow.colors}" defaultValue="${pageFlow.defaultColor}"/>
            </td>
          </tr>
          <tr valign="top">
            <td>Style:</td>
            <td>
              (passed to nested popup when looking up a model)
              <br>
              <netui:radioButtonGroup dataSource="actionForm.style" optionsDataSource="${pageFlow.styles}" defaultValue="${pageFlow.defaultStyle}"/>
            </td>
          </tr>
          <tr valign="top">
            <td><b>Model:</b></td>
            <td>
              <netui:textBox tagId="modelField" dataSource="actionForm.model"/>

              <netui:anchor action="getModel" popup="true">
                look up
                <netui:configurePopup updateFormFields="true" resizable="true" width="500" height="350">
                    <netui:retrievePopupOutput tagIdRef="modelField" dataSource="outputFormBean.model"/>
                </netui:configurePopup>
              </netui:anchor>

            </td>
          </tr>
        </table>
        <br/>
        <netui:button action="getModel" value="Get Model" popup="true">
          <netui:configurePopup updateFormFields="true" resizable="true" width="500" height="350">
              <netui:retrievePopupOutput tagIdRef="modelField" dataSource="outputFormBean.model"/>
          </netui:configurePopup>
        </netui:button>
        <br/>
        <netui:button type="submit" value="submit"/>
      </netui:form>

    </netui:body>
</netui:html>

