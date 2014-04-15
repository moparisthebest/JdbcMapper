<%@ tag body-content="empty" %>
<%@ attribute name="dataGrid" required="true" type="java.lang.Object"%>

<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${dataGrid.dataSet.size > 0}">
  <netui:form action="doPage" tagId="pageForm">
    Jump to Page:
<%-- This is the simple pager that uses its own <select> tag --%>
<script type="text/javascript">
  function doPagerSubmit(comp)
  {
    var form = document.forms[getNetuiTagName("pageForm",comp)];
    form.method="GET";
    form.submit();
  }
  </script>
  <select name="${dataGrid.urlBuilder.pagerRowQueryParamKey}" onchange="doPagerSubmit(this); return true;">
    <netui-data:repeater dataSource="dataGrid.urlBuilder.pagerParamValues">
    <c:choose>
      <c:when test="${container.index == dataGrid.state.pagerModel.page}">
        <option value="${container.item}" selected="true">${container.index+1}</option>
      </c:when>
      <c:otherwise>
        <option value="${container.item}">${container.index+1}</option>
      </c:otherwise>
    </c:choose>
    </netui-data:repeater>
  </select>
    </netui:form>
</c:if>

