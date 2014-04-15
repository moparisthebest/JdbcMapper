<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>PageInput Test17</title>
   </head>
   <body>
      <h3 align="center">PageInput Test17 - Jsp2.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <br/>
         <h2><font color="blue">Testing netui:textBox</font></h2>
         <br/><br/>
         <netui-data:declarePageInput name="ObjectA" type="shared.ClassA" />

         <netui:form action="action2">
            Class A string value.<font color="blue"><netui:textBox dataSource="pageFlow.string" defaultValue="${pageInput.ObjectA.stringValue}" /></font>
            <br/>
            Class B string value. <font color="blue"><netui:textBox dataSource="pageFlow.string" defaultValue="${pageInput.ObjectA.classB.stringValue}" /></font>
            <br/>
            Class C string value. <font color="blue"><netui:textBox dataSource="pageFlow.string" defaultValue="${pageInput.ObjectA.classB.classC.stringValue}" /></font>
            <br/>
            Class D string value. <font color="blue"><netui:textBox dataSource="pageFlow.string" defaultValue="${pageInput.ObjectA.classB.classC.classD.stringValue}" /></font>
            <br/>
            Class E string value. <font color="blue"><netui:textBox dataSource="pageFlow.string" defaultValue="${pageInput.ObjectA.classB.classC.classD.classE.stringValue}" /></font>
            <br/>
            Class F string value. <font color="blue"><netui:textBox dataSource="pageFlow.string" defaultValue="${pageInput.ObjectA.classB.classC.classD.classE.classF.stringValue}" /></font>
            <br/>
            Class G string value. <font color="blue"><netui:textBox dataSource="pageFlow.string" defaultValue="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.stringValue}" /></font>
            <br/>
            Class H string value. <font color="blue"><netui:textBox dataSource="pageFlow.string" defaultValue="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.classH.stringValue}" /></font>
            <br/>
            Class I string value. <font color="blue"><netui:textBox dataSource="pageFlow.string" defaultValue="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.classH.classI.stringValue}" /></font>
            <br/>
            Class J string value. <font color="blue"><netui:textBox dataSource="pageFlow.string" defaultValue="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.classH.classI.classJ.stringValue}" /></font>
            <br/>
            Class K string value. <font color="blue"><netui:textBox dataSource="pageFlow.string" defaultValue="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.classH.classI.classJ.classK.stringValue}" /></font>
            <br/>
            shared.ClassK@nnnnnnn" <font color="blue"><netui:textBox dataSource="pageFlow.string" defaultValue="${pageInput.ObjectA.classB.classC.classD.classE.classF.classG.classH.classI.classJ.classK}" /></font>
         </netui:form>
         <br/><br/>
         <netui:anchor action="action2">Continue...</netui:anchor>
         <br/><br/>
         <hr width="95%"/>
         <font color="green">
            The blue values on the right must match the values on the left.
            If they don't the test has failed.
         </font>
      </center>
   </body>
</html>
