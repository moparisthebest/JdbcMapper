<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib prefix="netuitestutil" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/util" %>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>

<netui-template:template templatePage="../site/template.jsp">
    <netui-template:setAttribute name="title" value="Basic Data Grid"/>
    <netui-template:section name="body">
    <p>
    <datagrid:portfolioXmlBean/>
    <br/>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="portfolio">
        <netuitestutil:assert test="${dataGrid != null}" failureMessage="Data Grid Model was not found in the PageContext"/>
        <netui-data:header>
            <netui-data:headerCell headerText="Symbol">
              <netuitestutil:assert test="${cell != null}" failureMessage="Cell Model was not found in the PageContext"/>
            </netui-data:headerCell>
            <netui-data:headerCell headerText="Price">
              <netuitestutil:assert test="${cell != null}" failureMessage="Cell Model was not found in the PageContext"/>
            </netui-data:headerCell>
            <netui-data:headerCell headerText="Web">
              <netuitestutil:assert test="${cell != null}" failureMessage="Cell Model was not found in the PageContext"/>
            </netui-data:headerCell>
        </netui-data:header>
        <netui-data:rows>
            <netui-data:spanCell value="${container.item.symbol}">
              <netuitestutil:assert test="${cell != null}" failureMessage="Cell Model was not found in the PageContext"/>
            </netui-data:spanCell>
            <netui-data:spanCell value="${container.item.price}">
              <netuitestutil:assert test="${cell != null}" failureMessage="Cell Model was not found in the PageContext"/>
            </netui-data:spanCell>
            <netui-data:anchorCell href="${container.item.web}" value="${container.item.name}">
              <netuitestutil:assert test="${cell != null}" failureMessage="Cell Model was not found in the PageContext"/>
              <netui:parameter name="rowid" value="${container.index}"/>
              <netui:parameter name="symbol" value="${container.item.symbol}"/>
            </netui-data:anchorCell>
        </netui-data:rows>
        <netuitestutil:assert test="${cell == null}" failureMessage="Cell Model found after it should have been removed"/>
        <netuitestutil:assert test="${dataGrid != null}" failureMessage="Data Grid Model missing"/>
    </netui-data:dataGrid>
    <netuitestutil:assert test="${dataGrid == null}" failureMessage="Data Grid Model found after it should have been removed"/>
    <netuitestutil:assert test="${cell == null}" failureMessage="Cell Model found after it should have been removed"/>
    <br/>
    </p>
    </netui-template:section>
</netui-template:template>
