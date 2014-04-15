<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>

<netui-template:template templatePage="../site/template.jsp">
    <netui-template:setAttribute name="title" value="Empty Columns Tag"/>
    <netui-template:section name="body">
    <p>
<datagrid:portfolioXmlBean/>
<br/>
<netui-data:dataGrid dataSource="pageScope.stocks" name="portfolio">
    <netui-data:header>
        <netui-data:headerCell headerText="Symbol"/>
        <netui-data:headerCell headerText="Price"/>
        <netui-data:headerCell headerText="Web"/>
    </netui-data:header>
    <netui-data:rows>
    </netui-data:rows>
    <netui-data:footer>
        <td colspan="4"><center>This is the grid's footer</center></td>
    </netui-data:footer>
</netui-data:dataGrid>
<br/>
<netui:anchor href="index.jsp">Reset</netui:anchor>
<br/>
    </p>
    </netui-template:section>
</netui-template:template>
