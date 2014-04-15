<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>
<netui-template:template templatePage="../site/template.jsp">
    <netui-template:setAttribute name="title" value="Template Column Smoke Test"/>
    <netui-template:section name="body">
    <p>
    <datagrid:portfolioXmlBean/>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="stocks">
        <netui-data:configurePager disableDefaultPager="true"/>
    <netui-data:header>
        <netui-data:headerCell headerText="Symbol"/>
        <netui-data:headerCell headerText="Price"/>
    </netui-data:header>
        <netui-data:rows>
            <netui-data:templateCell>
                <netui:span value="${container.item.symbol}"/>
            </netui-data:templateCell>
            <netui-data:imageAnchorCell src="${pageContext.request.contextPath}/databinding/datagrid/images/${container.item.symbol}.gif"
                                          height="20" width="20" alt="${container.item.web}"
                                          href="${container.item.web}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
    <br/>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="stocks">
        <netui-data:configurePager disableDefaultPager="true"/>
    <netui-data:header>
        <netui-data:headerCell headerText="Symbol"/>
        <netui-data:headerCell headerText="Price"/>
    </netui-data:header>
        <netui-data:rows>
            <netui-data:templateCell>
                <netui:span value="${container.item.symbol}"/>
            </netui-data:templateCell>
            <netui-data:imageAnchorCell src="${pageContext.request.contextPath}/databinding/datagrid/images/${container.item.symbol}.gif"
                                          height="20" width="20" alt="${container.item.web}"
                                          href="${container.item.web}">
                <netui:parameter name="symbol" value="${container.item.symbol}"/>
            </netui-data:imageAnchorCell>
        </netui-data:rows>
    </netui-data:dataGrid>
    </p>
    </netui-template:section>
</netui-template:template>
