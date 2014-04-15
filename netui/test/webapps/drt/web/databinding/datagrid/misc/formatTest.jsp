<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>
<netui-template:template templatePage="../site/template.jsp">
    <netui-template:setAttribute name="title" value="Format Test"/>
    <netui-template:section name="body">
    <p>
    <datagrid:portfolioXmlBean/>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="stocks">
        <netui-data:configurePager pageHref="disabledPagerNPE.jsp" disableDefaultPager="true"/>
        <netui-data:caption>
            Stocks
        </netui-data:caption>
    <netui-data:header>
        <netui-data:headerCell headerText="Symbol"/>
        <netui-data:headerCell headerText="Price"/>
    </netui-data:header>
        <netui-data:rows>
            <netui-data:spanCell value="${container.item.symbol}">
                 <netui:formatString pattern="<<< #* >>>"/>
                 <netui:formatString pattern="||| #* |||"/>
            </netui-data:spanCell>
            <netui-data:spanCell value="${container.item.price}">
                 <netui:formatNumber language="EN" country = "US" type="currency"/>
            </netui-data:spanCell>
        </netui-data:rows>
        <netui-data:footer>
            <netui-data:renderPager/>
        </netui-data:footer>
    </netui-data:dataGrid>
    <br/>
<%
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.set(2002,0,17,13,30,8);
    java.util.Date[] dates = new java.util.Date[] {c.getTime()};
    pageContext.setAttribute("dates", dates);
%>
    <netui-data:dataGrid dataSource="pageScope.dates" name="stocks">
        <netui-data:header>
            <netui-data:headerCell value="Date"/>
        </netui-data:header>
        <netui-data:rows>
            <netui-data:spanCell value="${container.item}">
                 <netui:formatDate pattern="yyyy.MM.dd"/>
            </netui-data:spanCell>
        </netui-data:rows>
    </netui-data:dataGrid>
    </p>
    </netui-template:section>
</netui-template:template>
