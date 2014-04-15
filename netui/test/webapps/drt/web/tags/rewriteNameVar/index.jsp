<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Web Application Page
        </title>
    </head>
    <netui:body>
    <script type="text/javascript"> 
    <!-- 
    function <netui:rewriteName name="someFunction" resultId="realName"/>(anchor) 
    { 
        anchor.style.color = "red"; 
    } 
    function <%= pageContext.getAttribute("realName")%>Out(anchor) 
    { 
        anchor.style.color = "blue"; 
    } 
    --> 
    </script> 
    <netui:anchor onMouseOver='<%= pageContext.getAttribute("realName") + "(this);"%>'
        action="begin" onMouseOut='<%= pageContext.getAttribute("realName") + "Out(this);"%>'>Some Link</netui:anchor> 
    </netui:body>
</netui:html>
