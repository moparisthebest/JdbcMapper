<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base/>
        <netui:scriptHeader />
    <netui:body>
        <p style="color:#339900;">This is a test of the &lt;divPanel>.  There are three links which change the currently
        displayed page.  Each 'page' contains a link.  When the link is pressed, we hit the postback
        link and redisplay the page.  What should happen is that the current page inside the &lt;divPanel>
        should still be the same one that was posted back.
        <hr>
        <netui:anchor clientAction='divpanel.showPage("page1");'>page One</netui:anchor>&nbsp;&nbsp;&nbsp;
        <netui:anchor clientAction='divpanel.showPage("page2");'>page Two</netui:anchor>&nbsp;&nbsp;&nbsp;
        <netui:anchor clientAction='divpanel.showPage("page3");'>page Three</netui:anchor>
        <hr>
        <netui:divPanel dataSource="pageFlow.divState" tagId="divpanel">
            <netui-template:section name="page1">
                Page One<br>
                <netui:anchor action="postback">postback</netui:anchor>
            </netui-template:section>
            
            <netui-template:section name="page2">            
                Page Two<br>
                <netui:anchor action="postback">postback</netui:anchor>
            </netui-template:section>
            
            <netui-template:section name="page3">            
                Page Three<br>
                <netui:anchor action="postback">postback</netui:anchor>
            </netui-template:section>
        </netui:divPanel>
    </netui:body>
</netui:html>

  
