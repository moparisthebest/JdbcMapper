<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>

<netui:html>
   <head>
      <title>MiscJpf Bug 30303 test</title>
   </head>
   <body>
      <h3 align="center">MiscJpf Bug 30303 test - Jsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
      <font color="red">
         <strong>
            Do Not change any of the values just press the "Submit" button!
         </strong>
      </font>

      <!-- ---------------------------------------------------------------------
      The most important part of this test is to have the form's enctype to be
      "multipart/form-data".
      ---------------------------------------------------------------------- -->
      <netui:form action="jpfAction1" enctype="multipart/form-data">
         <table align="center" width="100%" border="0">
            <tr>
               <td align="right" width="50%">
                  Struts text:
               </td>
               <td align="left" width="50%">
                  <netui:textBox dataSource="strutsText"
                                 defaultValue="Struts Value" />
               </td>
            </tr>
            <tr>
               <td align="right" width="50%">
                  PageFlow text:
               </td>
               <td align="left" width="50%">
                  <netui:textBox dataSource="actionForm.pageflowText"
                                 defaultValue="PageFlow Value" />
               </td>
            </tr>
         </table>
         <br/>
         <netui:button>Submit</netui:button>
      </netui:form>
   </body>
</netui:html>
