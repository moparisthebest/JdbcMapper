<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui:html>
    <head>
        <title>SingletonJpf JpfTest6</title>
    </head>
    <body>
        <center>
            <span style="font-size: 20px; color: green; font-weight: bold">
                SingletonJpf JpfTest6 - JspTest6a.jsp
            </span>
            <br/><br/>
           <span style="font-size: 20px; color: blue; font-weight: bold">
                <netui:anchor action="gotoJpf1">Singleton test</netui:anchor>
                &nbsp;&nbsp;
                <netui:anchor action="gotoJpf2">Non-singleton test</netui:anchor>
                <br/><br/>
                <netui:anchor action="finish">Done</netui:anchor>
            </span>
        </center>
    </body>
</netui:html>
