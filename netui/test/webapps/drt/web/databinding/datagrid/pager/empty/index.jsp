<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.Collections"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui-template:template templatePage="../../site/template.jsp">
    <netui-template:setAttribute name="title" value="Empty Paged Data Grid"/>
    <netui-template:section name="body">
        <p>
        <% pageContext.setAttribute("emptyList", Collections.EMPTY_LIST); %>
        <br/>
        <netui-data:dataGrid dataSource="pageScope.emptyList" name="portfolio">
            <netui-data:configurePager pageSize="2" pagerFormat="firstPrevNextLast" pageAction="begin.do"/>
            <netui-data:header>
                <netui-data:headerCell headerText="Symbol"/>
                <netui-data:headerCell headerText="Price"/>
                <netui-data:headerCell headerText="Web"/>
            </netui-data:header>
            <netui-data:rows>
                <netui-data:spanCell value="${container.item.symbol}"/>
                <netui-data:spanCell value="${container.item.price}"/>
                <netui-data:anchorCell href="${container.item.web}" value="${container.item.name}">
                    <netui:parameter name="rowid" value="${container.index}"/>
                    <netui:parameter name="symbol" value="${container.item.symbol}"/>
                </netui-data:anchorCell>
            </netui-data:rows>
            <netui-data:footer>
                <td colspan="4">
                    <netui-data:renderPager/>
                </td>
            </netui-data:footer>
        </netui-data:dataGrid>
        <br/>
        <netui:anchor href="index.jsp">Reset</netui:anchor>
        <br/>
        </p>
    </netui-template:section>
</netui-template:template>
