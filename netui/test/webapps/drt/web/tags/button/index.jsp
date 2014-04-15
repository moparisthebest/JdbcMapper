<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
      <title>NetUI Button Tag Rendering</title>
      <netui:base/>
    </head>
    <netui:body>
      <h2>Test - NetUI Button tag rendering options</h2>

      <netui:form action="submitIt">
        <table>
          <tr valign="top">
            <td>Shaper:</td>
            <td>
              <netui:textBox dataSource="actionForm.shaper"/>
            </td>
          </tr>
          <tr valign="top">
            <td>Style:</td>
            <td>
              <netui:radioButtonGroup dataSource="actionForm.style" optionsDataSource="${pageFlow.styles}" defaultValue="${pageFlow.defaultStyle}"/>
            </td>
          </tr>
        </table>

        <br>
        <br>
        &lt;netui:button>... rendered with &lt;input> element
        <br>
        <netui:button action="cancel" value="Cancel Submit"/>
        <netui:button action="cancel">Cancel Submit...</netui:button>
        <br>
        <netui:button type="button" action="cancel" value="Cancel Button"/>
        <netui:button type="button" action="cancel">
          Cancel Button Text...
        </netui:button>
        <br>
        <netui:button type="reset" value="Reset Button"></netui:button>
        <netui:button type="reset" renderAsButton="false">
          Reset Text...
        </netui:button>
        <br>
        <netui:button type="submit" value="Input Submit"/>
        <netui:button type="submit">
            Input Submit Text...
        </netui:button>

        <br>
        <br>
        &lt;netui:button>... rendered with &lt;button> element
        <br>
        <netui:button action="cancel" value="Cancel Submit 2" renderAsButton="true"/>
        <netui:button action="cancel" renderAsButton="true">
          Cancel Submit...
        </netui:button>
        <br>
        <netui:button type="button" action="cancel" value="Cancel Button 2" renderAsButton="true"/>
        <br>
        <netui:button type="reset" value="Reset Button 2" renderAsButton="true">
          <netui:image src="folder.gif"/>Img Reset Text...
        </netui:button>
        <netui:button type="reset" renderAsButton="true">
          Reset Text...
        </netui:button>
        <br>
        <netui:button type="submit" value="submit" renderAsButton="true">
          Input Submit Text...
        </netui:button>
      </netui:form>

    </netui:body>
</netui:html>

