<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui:html>
    <head>
        <title>ScopedJpf JpfTest2</title>
    </head>
    <body>
        <center>
            <span style="font-size: 20px; color: green; font-weight: bold">
                ScopedJpf JpfTest2 - SubJpf1.SubJsp1a.jsp
                <br/>
                Scope: <netui:span value="${pageFlow.scope}"/>
            </span>
        <netui:form action="finish">
            <netui:button action="finish" type="submit">Done</netui:button>
        </netui:form>
        </center>
    </body>
</netui:html>
