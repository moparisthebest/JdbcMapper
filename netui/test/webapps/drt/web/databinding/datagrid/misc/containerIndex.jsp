<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui-template:template templatePage="../site/template.jsp">
    <netui-template:setAttribute name="title" value="Data Grid container.index Test"/>
    <netui-template:section name="body">
    <p>
    <br/>
<%
    pageContext.setAttribute("fiveStrings", new String[] {"zero", "one", "two", "three", "four"});
%>
    <netui-data:dataGrid dataSource="pageScope.fiveStrings" name="portfolio">
        <netui-data:rows>
            <netui-data:spanCell value="${container.index}"/>
            <netui-data:spanCell value="${container.item}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
    <br/>
    <br/>
    <netui-data:dataGrid dataSource="pageScope.fiveStrings" name="portfolio1">
        <netui-data:configurePager pageSize="1"/>
        <netui-data:rows>
            <netui-data:spanCell value="${container.index}"/>
            <netui-data:spanCell value="${container.item}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
    <br/>
    <br/>
    <netui-data:dataGrid dataSource="pageScope.fiveStrings" name="portfolio2">
        <netui-data:configurePager pageSize="2"/>
        <netui-data:rows>
            <netui-data:spanCell value="${container.index}"/>
            <netui-data:spanCell value="${container.item}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
    <br/>
    <br/>
    <netui-data:dataGrid dataSource="pageScope.fiveStrings" name="portfolio3">
        <netui-data:configurePager pageSize="3"/>
        <netui-data:rows>
            <netui-data:spanCell value="${container.index}"/>
            <netui-data:spanCell value="${container.item}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
    <br/>
    <br/>
    <netui-data:dataGrid dataSource="pageScope.fiveStrings" name="portfolio4">
        <netui-data:configurePager pageSize="4"/>
        <netui-data:rows>
            <netui-data:spanCell value="${container.index}"/>
            <netui-data:spanCell value="${container.item}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
    <br/>
    <br/>
    <netui-data:dataGrid dataSource="pageScope.fiveStrings" name="portfolio5">
        <netui-data:configurePager pageSize="5"/>
        <netui-data:rows>
            <netui-data:spanCell value="${container.index}"/>
            <netui-data:spanCell value="${container.item}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
    <br/>
    <br/>
    <netui-data:dataGrid dataSource="pageScope.fiveStrings" name="portfolio5">
        <netui-data:configurePager pageSize="6"/>
        <netui-data:rows>
            <netui-data:spanCell value="${container.index}"/>
            <netui-data:spanCell value="${container.item}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
    </p>
    <p>
    <table>
    <netui-data:repeater dataSource="pageScope.fiveStrings">
        <tr>
          <td><netui:span value="${container.index}"/></td>
          <td><netui:span value="${container.item}"/></td>
        </tr>
    </netui-data:repeater>
    <table>
    </p>
    </netui-template:section>
</netui-template:template>
