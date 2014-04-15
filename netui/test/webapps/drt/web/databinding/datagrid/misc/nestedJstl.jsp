<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<netui-template:template templatePage="../site/template.jsp">
    <netui-template:setAttribute name="title" value="Nested JSTL Tags"/>
    <netui-template:section name="body">
    <p>
    <datagrid:portfolioXmlBean/>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="portfolio">
    <netui-data:header>
        <netui-data:headerCell headerText="Symbol"/>
        <netui-data:headerCell headerText="Symbol (with choose)"/>
    </netui-data:header>
        <netui-data:rows>
          <netui-data:spanCell value="${container.item.symbol}"/>
          <c:choose>
            <c:when test="${container.index == 0}">
              <netui-data:spanCell value="BEA Systems"/>
            </c:when>
            <c:when test="${container.index == 1}">
              <netui-data:spanCell value="Cisco"/>
            </c:when>
            <c:otherwise>
              <netui-data:spanCell value="None"/>
            </c:otherwise>
        </c:choose>
        </netui-data:rows>
    </netui-data:dataGrid>
    </netui-template:section>
</netui-template:template>
