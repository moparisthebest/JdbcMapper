<%@ page    contentType="text/html;charset=UTF-8"  language="java" %>
<%@ taglib  uri="http://beehive.apache.org/netui/tags-html-1.0"              prefix="netui" %>
<%@ taglib  uri="http://beehive.apache.org/netui/tags-databinding-1.0"       prefix="netui-data" %>
<%@ taglib  uri="http://beehive.apache.org/netui/tags-template-1.0"          prefix="netui-template"%>

<netui-template:template templatePage="template.jsp">
   <netui-template:setAttribute  value="PageInput Test25" name="title"/>
   <netui-template:setAttribute  value="PageInput Test25 - Jsp1.jsp"
                                 name="heading"/>
   <netui-template:section       name="bodySection">
      <center>
         <br/>
         <h2><font color="blue">Testing PageInput tags</font></h2>
         <br/><br/>
         PageInput context value in the main page:
         <font color="blue">
            <netui:span value="${pageInput.string}" />
         </font>
         <br/><br/>
         <netui:anchor action="finish">Finish...</netui:anchor>
      </center>
   </netui-template:section>
</netui-template:template>
