<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>PageInput Test17</title>
   </head>
   <body>
      <h3 align="center">PageInput Test17 - Jsp3.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <br/>
         <h2><font color="blue">Testing netui:anchor & netui:content</font></h2>
         <br/><br/>
         <netui-data:declarePageInput name="ObjectA" type="shared.ClassA" />

         Class A string value. <font color="blue"><netui:anchor href="${pageInput.ObjectA.stringValue}" ><netui:content value="${pageInput.ObjectA.stringValue}" /></netui:anchor></font>
         <br/>
         Class B string value. <font color="blue"><netui:anchor href="${pageInput.ObjectA.classB.stringValue}" ><netui:content value="${pageInput.ObjectA.classB.stringValue}" /></netui:anchor></font>
         <br/>
         Class C string value. <font color="blue"><netui:anchor href="${pageInput.ObjectA.classB.classC.stringValue}" ><netui:content value="${pageInput.ObjectA.classB.classC.stringValue}" /></netui:anchor></font>
         <br/>
         Class D string value. <font color="blue"><netui:anchor href="${pageInput.ObjectA.classB.classC.classD.stringValue}" ><netui:content value="${pageInput.ObjectA.classB.classC.classD.stringValue}" /></netui:anchor></font>
         <br/>
         Class E string value. <font color="blue"><netui:anchor href="${pageInput.ObjectA.classB.classC.classD.classE.stringValue}" ><netui:content value="${pageInput.ObjectA.classB.classC.classD.classE.stringValue}" /></netui:anchor></font>
         <br/>
         Class F string value. <font color="blue"><netui:anchor href="${pageInput.ObjectA.classB.classC.classD.classE.classF.stringValue}" ><netui:content value="${pageInput.ObjectA.classB.classC.classD.classE.classF.stringValue}" /></netui:anchor></font>
         <br/>
         Class G string value. <font color="blue"><netui:anchor href="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.stringValue}" ><netui:content value="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.stringValue}" /></netui:anchor></font>
         <br/>
         Class H string value. <font color="blue"><netui:anchor href="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.classH.stringValue}" ><netui:content value="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.classH.stringValue}" /></netui:anchor></font>
         <br/>
         Class I string value. <font color="blue"><netui:anchor href="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.classH.classI.stringValue}" ><netui:content value="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.classH.classI.stringValue}" /></netui:anchor></font>
         <br/>
         Class J string value. <font color="blue"><netui:anchor href="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.classH.classI.classJ.stringValue}" ><netui:content value="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.classH.classI.classJ.stringValue}" /></netui:anchor></font>
         <br/>
         Class K string value. <font color="blue"><netui:anchor href="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.classH.classI.classJ.classK.stringValue}" ><netui:content value="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.classH.classI.classJ.classK.stringValue}" /></netui:anchor></font>
         <br/>
         shared.ClassK@nnnnnnn" <font color="blue"><netui:anchor href="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.classH.classI.classJ.classK}" ><netui:content value="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.classH.classI.classJ.classK}" /></netui:anchor></font>
         <br/><br/>
         <netui:anchor action="action3">Continue...</netui:anchor>
         <br/><br/>
         <hr width="95%"/>
         <font color="green">
            The values of the links must match the values on the left.  If they
            don't the test failed.
            <br/><br/>
            Also hover the mouse over the links and look at the URLs in the
            status bar below.  The last part of the URL should be the same text
            as the link you've hovered over.  If it's not the test failed.
         </font>
      </center>
   </body>
</html>
