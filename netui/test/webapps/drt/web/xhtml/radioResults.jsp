<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui-data:declarePageInput name="formData" type="xhtml.Controller.RadioTests"/>
<netui-data:declarePageInput name="action" type="java.lang.String"/>
<netui:html documentType="xhtml1-transitional">
    <head>
        <title>Radio Post Results</title>
        <style type="text/css">
        .normal {color: #000099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        p {color: #000099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalError {color: #ff0033;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalHead {color: #000099;font-family:Verdana; font-size:8pt;font-weight: strong;margin:0,0,0,0;}
        .title {color: #000099;font-family:Verdana; font-size:12pt;margin:2,0,5,0;}
        .resultDiv {border: thin solid;height: 400px;}
        </style>
    </head>        
  <netui:body>
    <table width="400"><tr><td align="left"><h1 class="normalHead">Radio Post Results</h1></td>
        <td align="right"><netui:anchor styleClass="normal" action="goRadioTest">Radio Tests</netui:anchor></td>
    </tr></table>
    <p>
        Action: <netui:span value="${pageInput.action}"></netui:span> 
    </p>
        <table class="tablebody" border="1" cellspacing="0">
        <tr class="tablehead" valign="top">
            <th class="normal">RadioButton Group</th>
            <th class="normal">Options RadioButton Group</th>
            <th class="normal">Map Options RadioButton Group</th>
            <th class="normal">Complex RadioButton Group</th>
        </tr>
        <tr valign="top">
            <td>
                <netui:span value="${pageInput.formData.rbg}" defaultValue="&nbsp;" styleClass="normal"></netui:span>

            </td>
            <td>
                <netui:span value="${pageInput.formData.rbgOpts}" defaultValue="&nbsp;" styleClass="normal"></netui:span>
            </td>
            <td>
                <netui:span value="${pageInput.formData.rbgOptsMap}" defaultValue="&nbsp;" styleClass="normal" />
            </td>
            <td>
                <netui:span value="${pageInput.formData.rbgComplex}" defaultValue="&nbsp;" styleClass="normal" />
            </td>
        </tr>
        </table>        
  </netui:body>
</netui:html>


  
  
