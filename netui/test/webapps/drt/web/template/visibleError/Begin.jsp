<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="temp" %>
<temp:template templatePage="Template.jsp">
  <temp:setAttribute name="title" value="${pageFlow.title}"/>
  <temp:section name="left">
    <span style="color:blue;">
    Blue text in the 'left' side.
    </span>
  </temp:section>
  <temp:section name="right">
    <span style="color:red;">
    Red text in the 'right' side.
    </span>
  </temp:section>
</temp:template>
