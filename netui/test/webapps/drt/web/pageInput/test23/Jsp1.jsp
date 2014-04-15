<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>PageInput Test23</title>
   </head>
   <body>
      <h3 align="center">PageInput Test23 - Jsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <netui-data:declarePageInput  name="theForm"
                                       type="pageInput.test23.Jpf1$MyForm" />
         <h3>
            All fields being displayed below are from the same formBean just
            using different data-binding contexts.
         </h3>
         <b>These values are from the {actionForm} context.</b>
         <br/>
         <netui:form action="action1">

            {actionForm.String1}:
            <font color="blue">
               <netui:textBox dataSource="actionForm.string1"/>
            </font>
            <br/>

            {actionForm.String2}:
            <font color="blue">
               <netui:textBox dataSource="actionForm.string2"/>
            </font>
            <br/>

            {actionForm.Int1}:
            <font color="blue">
               <netui:textBox dataSource="actionForm.int1"/>
            </font>
            <br/>

            {actionForm.Int2}:
            <font color="blue">
               <netui:textBox dataSource="actionForm.int2"/>
            </font>
            <br/><br/>
            <netui:button  action="action1" value="Submit changes"
                           type="submit"/>
         </netui:form>
         <netui:errors/>
         <hr width="95%"/>

         <br/>
         <b>These values are from the {pageInput} context.</b>
         <br/><br/>

         {pageInput.theForm.String1}:
         <font color="blue">
            <netui:span value="${pageInput.theForm.string1}" />
         </font>
         <br/>

         {pageInput.theForm.String2}:
         <font color="blue">
            <netui:span value="${pageInput.theForm.string2}" />
         </font>
         <br/>

         {pageInput.theForm.Int1}:
         <font color="blue">
            <netui:span value="${pageInput.theForm.int1}" />
         </font>
         <br/>

         {pageInput.theForm.Int2}:
         <font color="blue">
            <netui:span value="${pageInput.theForm.int2}" />
         </font>
         <br/><br/>
         <hr width="95%"/>

         <br/>
         <font color="green">
            <b>
               You will see this page twice.
               <br/>
               - The first time please change the values in all the textBoxes then
               press the submit button.
               <br/>
               - The second time check the values of the checkBoxes and the
               PageInputs.  The textBoxs should have your modified values and
               the pageInput fields should have their original values.  If this
               in not the case the test have failed.  Then press Finish.
            </b>
         </font>
         <br/><br/>
         <netui:anchor action="finish">Finish...</netui:anchor>
      </center>
   </body>
</html>
