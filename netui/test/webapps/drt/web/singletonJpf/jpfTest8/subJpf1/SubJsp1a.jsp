<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui:html>
    <head>
        <title>SingletonJpf JpfTest8</title>
    </head>
    <body>
        <center>
            <span style="font-size: 20px; color: green; font-weight: bold">
                SingletonJpf
                <br/>
                JpfTest8 - SubJsp1.SubJsp1a.jsp
            </span>
            <br/><br/>
            <netui:form action="finish">
               <span style="font-size: 20px; color: blue; font-weight: bold">
                    Form field: <netui:content value="${actionForm.string1}" />
                    <br/><br/>
                    <netui:button action="gotoJpf2" type="submit">Goto Jpf2</netui:button>
                    &nbsp;&nbsp;
                    <netui:button action="finish" type="submit">Return</netui:button>
                </span>
            </netui:form>
        </center>
    </body>
</netui:html>
