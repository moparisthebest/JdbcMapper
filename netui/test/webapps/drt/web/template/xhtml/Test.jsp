<%@ page language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="temp" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<temp:template templatePage="Template.jsp" documentType="xhtml1-transitional">
  <temp:setAttribute name="title" value="Simple Test"/>
  <temp:section name="left">
     <netui:form action="post">
        <netui:select dataSource="pageFlow.selectedValue"
		optionsDataSource="${pageFlow.selectOptions}" /><br />
	<netui:button value="submit"/>
     </netui:form>
  </temp:section>
  <temp:section name="right">
      Enter some value in the form fool....
  </temp:section>
</temp:template>
