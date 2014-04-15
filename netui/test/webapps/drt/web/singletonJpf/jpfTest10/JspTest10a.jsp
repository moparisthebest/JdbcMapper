<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui:html>
    <head>
        <title>SingletonJpf JpfTest10</title>
    </head>
    <body>
        <center>
            <span style="font-size: 20px; color: green; font-weight: bold">
                SingletonJpf JpfTest10 - JspTest10a.jsp
            </span>
            <br/><br/>
           <span style="font-size: 20px; color: blue; font-weight: bold">
                <netui:anchor action="gblJpfTest10">Goto Jpf1 via Global.app</netui:anchor>
                &nbsp;&nbsp;
                <netui:anchor action="finish">Finish</netui:anchor>
            </span>
        </center>
    </body>
</netui:html>
