<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netuidata"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>

<netui-template:template templatePage="../site/template.jsp">
    <netui-template:setAttribute name="title" value="DataGrid JSP Function Test"/>
    <netui-template:section name="body">
    <p>
    <datagrid:portfolioXmlBean/>
    <br/>
    <netuidata:dataGrid dataSource="pageScope.stocks" name="portfolio">
        <netuidata:header>
            <netuidata:headerCell headerText="Symbol" sortExpression="symbol" sortHref="jspFunction-sort.jsp"/>
            <netuidata:headerCell headerText="Price" sortExpression="price"/>
            <netuidata:headerCell headerText="Web" sortExpression="web"/>
        </netuidata:header>
        <netuidata:rows>
            <netuidata:templateCell sortExpression="symbol">
                Symbol is '${container.item.symbol}' and is sorted:
                <c:choose>
                    <c:when test="${netuidata:isSortedAscending(dataGrid.state.sortModel, 'symbol')}">
                        <netui:span value="ascending"/>
                    </c:when>
                    <c:when test="${netuidata:isSortedDescending(dataGrid.state.sortModel, 'symbol')}">
                        <netui:span value="descending"/>
                    </c:when>
                    <c:otherwise>none</c:otherwise>
                </c:choose>
            </netuidata:templateCell>
            <netuidata:spanCell value="${container.item.price}" sortExpression="price"/>
            <netuidata:anchorCell href="${container.item.web}" value="${container.item.name}" sortExpression="web">
                <netui:parameter name="rowid" value="${container.index}"/>
                <netui:parameter name="symbol" value="${container.item.symbol}"/>
            </netuidata:anchorCell>
        </netuidata:rows>
    </netuidata:dataGrid>
    <br/>
    <netui:anchor href="index.jsp">Reset</netui:anchor>
    <br/>
    </p>
    </netui-template:section>
</netui-template:template>
