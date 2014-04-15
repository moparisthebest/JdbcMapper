<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <netui:body>
    <p>
      <b>JIRA 493 BVT</b>
      <br/>
      <br/>
<%
java.util.Map map = new java.util.HashMap();
map.put("arrayParamKey", new String[] {"paramValue1", "paramValue2", "paramValue3"});
pageContext.setAttribute("map", map);
%>
    <netui:anchor href="j502.jsp" value="Submit">
        <netui:parameterMap map="${pageScope.map}"/>
    </netui:anchor>
    </p>
    </netui:body>
</netui:html>
