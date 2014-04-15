<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>

      <h3>Select a Model</h3>
      <p>Board Style: ${pageFlow.style}</p>
      <netui:form action="done">
        <table>
          <tr valign="top">
            <td>Model:</td>
            <td>
              <netui:radioButtonGroup dataSource="actionForm.model" optionsDataSource="${pageFlow.models}" defaultValue="${pageFlow.defaultModel}"/>
            </td>
          </tr>
        </table>
        <br/>&nbsp;
        <netui:button type="submit" value="submit"/>
        <netui:button type="submit" action="cancel" value="cancel"/>
      </netui:form>

    </netui:body>
</netui:html>

  
