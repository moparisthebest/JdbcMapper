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
        <netui-data:headerCell headerText="Shares"/>
    </netui-data:header>
        <netui-data:rows>
            <netui-data:templateCell>
                <netui:span value="${container.item.symbol}"/>
            </netui-data:templateCell>
            <netui-data:templateCell>
                <netui:textBox dataSource="container.item.shares"/>
            </netui-data:templateCell>
        </netui-data:rows>
    </netui-data:dataGrid>
    <br/>
    </p>
    </netui-template:section>
</netui-template:template>
