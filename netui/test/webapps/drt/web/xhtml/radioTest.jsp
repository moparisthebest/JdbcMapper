<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html documentType="xhtml1-transitional">
    <head>
        <title>RadioGroup Tests</title>
        <style type="text/css">
        .normal {color: #000099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal2 {color: #cc0099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal3 {color: #00cc99;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalBold {color: #000099;font-family:Verdana; font-size:8pt; font-weight: bold;margin:0,0,0,0;}
        li {color: #000099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalError {color: #ff0033;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalHead {color: #000099;font-family:Verdana; font-size:8pt;font-weight: strong;margin:0,0,0,0;}
        .title {color: #000099;font-family:Verdana; font-size:12pt;margin:2,0,5,0;}
        .resultDiv {border: thin solid;margin:5,5,5,5;}
        </style>
    </head>        
  <netui:body>
    <h1 class="normalHead">RadioGroup Tests</h1>
    <netui:anchor action="begin" rel="contents" styleClass="normal" rev="chapter">Home</netui:anchor>
    <div class="resultDiv">
        <p class="normal">RadioGroup Tests</p>
            <netui:form action="postRadioForm">
                <table>
                    <tr valign="top">
                        <td class="normalBold" align="right">RadioButton Group</td>
                        <td>
                        <netui:radioButtonGroup dataSource="actionForm.rbg"  orientation="vertical">
                            <netui:radioButtonOption value="Radio Opt 1" labelStyleClass="normal" disabled="true">Radio Option One</netui:radioButtonOption>
                            <netui:radioButtonOption value="Radio Opt 2" labelStyleClass="normal">Radio Option Two</netui:radioButtonOption>
                        </netui:radioButtonGroup>
                        </td>
                    </tr>
                    <tr valign="top">
                        <td class="normalBold" align="right">Repeating Options RadioButton Group</td>
                        <td>
                        <netui:radioButtonGroup dataSource="actionForm.rbgOpts" 
                                optionsDataSource="${pageFlow.radioButtonOptions}" 
                                orientation="horizontal" 
                                repeater="true">
                            <netui:radioButtonOption value="${container.item}" styleClass="normal" />
                            <netui:span value="${container.item}" styleClass="normal" />
                        </netui:radioButtonGroup>
                        </td>
                    </tr>
                    <tr valign="top">
                        <td class="normalBold" align="right">Map Options RadioButton Group</td>
                        <td>
                        <netui:radioButtonGroup dataSource="actionForm.rbgOptsMap"
                            optionsDataSource="${pageFlow.radioButtonMapOptions}"
                            labelStyleClass="normal2" 
                        >
                        </netui:radioButtonGroup>
                        </td>
                    </tr>
                    <tr valign="top">
                        <td colspan="2" align="center">
                            <table cellspacing="0" border="1">
                            <tr><th class="normalBold" valign="top" align="center">Complex Repeater</th><td>
                            <netui:radioButtonGroup dataSource="actionForm.rbgComplex" optionsDataSource="${pageFlow.opts}" 
                                repeater="true">
                                <netui:radioButtonOption value="${container.item.optionValue}" />
                                    <netui:span styleClass="${container.item.style}" value="${container.item.name}" /><br />
                            
                            </netui:radioButtonGroup>
                            </td></tr></table>
                        </td>
                    </tr>
                </table>
                <br />
                <netui:button type="submit" value="Post RadioForm"/>
            </netui:form>
    </div>
    </netui:body>
</netui:html>

  
