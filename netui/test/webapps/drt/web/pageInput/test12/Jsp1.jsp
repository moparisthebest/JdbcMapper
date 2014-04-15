<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>
<html>
   <head>
      <title>PageInput Test12</title>
   </head>
   <body>
      <h3 align="center">PageInput Test12 - Jsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <netui-data:declarePageInput name="pgInput1" type="java.lang.String" />
         <netui-data:declarePageInput name="pgInput2" type="java.lang.String" />

         {pageInput.pgInput1}:
         <font color="blue">
            "<netui:span value="${pageInput.pgInput1}"/>"
         </font>
         <br/>
         {pageInput.pgInput2}:
         <font color="blue">
            "<netui:span value="${pageInput.pgInput2}"/>"
         </font>

         <br/><br/>
         <hr width="95%"/>
         <br/>
         <netui:form action="action1">
            {actionForm.fld1}:
               <netui:textBox dataSource="actionForm.fld1"/>
               <netui:error key="field1"/>
             <br/>
             {actionForm.fld2}:
                <netui:textBox dataSource="actionForm.fld2"/>
                <netui:error key="field2"/>
             <br/><br/>
             <netui:button action="action1" value="Continue..." type="submit"/>
         </netui:form>

         <netui:errors/>
         <br/>
         <font color="green">
            <b>
               You will visit this page twice.  Just press "continue" each time.
            </b>
            <br/>
            <p align="left">
            - The first time the form fields and the pageInput fields will
              display.  Note their values.<br/>
            - The second time the same form and pageInput fields will display
             along with some error messages.  Make sure the pageInput fields
             data is present and has the same value as the first time. The form
             data will change.  That's expected.
            </p>
         </font>
      </center>
   </body>
</html>
