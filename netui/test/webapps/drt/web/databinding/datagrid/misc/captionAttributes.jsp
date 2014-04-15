<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>
<netui:html>
  <head>
    <title>NetUI Data Grid Samples</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/databinding/datagrid/site/css/default.css" type="text/css"/>
	<style type="text/css">
	  caption.caption, caption.datagrid {
	  text-align: left;
	  color:#5f7797;
	  font-weight:bold;
	  font-size:36pt;
	  }
	</style>	
  </head>
  <netui:base/>
  <netui:body>
    <p>
    <table width="100%">
    <tr><td></td></tr>
    <tr><td>
    <datagrid:portfolioXmlBean/>
    <br/>
    <b>explicit style</b>
    <br/>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="stocks">
        <netui-data:configurePager pageHref="disabledPagerNPE.jsp" disableDefaultPager="true"/>
        <netui-data:caption style="text-align: left;color:#5f7797;font-weight:bold;font-size:36pt;" 
                            onClick="javascript:alert('working! click on the caption')">
            Stocks
        </netui-data:caption>
    <netui-data:header>
        <netui-data:headerCell headerText="Symbol"/>
        <netui-data:headerCell headerText="Price"/>
    </netui-data:header>
        <netui-data:rows>
            <netui-data:spanCell value="${container.item.symbol}"/>
            <netui-data:spanCell value="${container.item.price}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
    <br/>
    <br/>
    <br/>
    <b>styleClass="datagrid"</b>
    <br/>
    <br/>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="stocks">
        <netui-data:configurePager pageHref="disabledPagerNPE.jsp" disableDefaultPager="true"/>
        <netui-data:caption onClick="javascript:alert('working! click on the caption')">
            Stocks
        </netui-data:caption>
    <netui-data:header>
        <netui-data:headerCell headerText="Symbol"/>
        <netui-data:headerCell headerText="Price"/>
    </netui-data:header>
        <netui-data:rows>
            <netui-data:spanCell value="${container.item.symbol}"/>
            <netui-data:spanCell value="${container.item.price}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
    <b>styleClass="caption"</b>
    <br/>
    <br/>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="stocks">
        <netui-data:configurePager pageHref="disabledPagerNPE.jsp" disableDefaultPager="true"/>
        <netui-data:caption styleClass="caption"
                            onClick="javascript:alert('working! click on the caption')">
            Stocks
        </netui-data:caption>
    <netui-data:header>
        <netui-data:headerCell headerText="Symbol"/>
        <netui-data:headerCell headerText="Price"/>
    </netui-data:header>
        <netui-data:rows>
            <netui-data:spanCell value="${container.item.symbol}"/>
            <netui-data:spanCell value="${container.item.price}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
    </td></tr>
    </p>
    <table>
    <tr><td><netui:anchor href="/netuiDRT/databinding/datagrid/basic/index.jsp">Home</netui:anchor></td></tr>
    </table>
    </p>
  </netui:body>
</netui:html>
