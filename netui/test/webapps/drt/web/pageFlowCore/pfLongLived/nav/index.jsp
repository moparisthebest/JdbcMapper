<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>

    <body>
        <h3>Navigation</h3>
        
        <netui:anchor action="doNothing">no-op action</netui:anchor>
        <br>
        <a href="../nonLongLived/NonLongLivedController.jpf" target="contentFrame">non-longLived 1</a>
        <br>
        <a href="../anotherNonLongLived/AnotherNonLongLivedController.jpf" target="contentFrame">non-longLived 2</a>
        <br>
        <a href="../longLived/LongLivedController.jpf" target="contentFrame">longLived</a>
    </body>
</netui:html>
