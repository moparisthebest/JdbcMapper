<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>
<netui-template:template templatePage="../site/template.jsp">
    <netui-template:setAttribute name="title" value="Basic Data Grid"/>
    <netui-template:section name="body">
    <p>
<datagrid:portfolioXmlBean/>
<br/>
<br/>
<netui:scriptContainer generateIdScope="true">
    <%@ include file="tr-tagid.jsp" %>
</netui:scriptContainer>
<br/>
<netui:anchor href="index.jsp">Reset</netui:anchor>
<br/>
    </p>
    </netui-template:section>
</netui-template:template>
