<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<head>
    <title>ScopedJpf JpfTest2</title>
</head>
<body>
    <center>
        <netui:form action="refresh">
            <span style="font-size: 20px; color: green; font-weight: bold">
                ScopedJpf JpfTest2 - Jpf2.Jsp2a.jsp
                <br/>
                PageFlow scope:&nbsp; <netui:span value="${pageFlow.scope}"/>
                <br/>
                Form field1:&nbsp; <netui:span value="${actionForm.string1}"/>
            </span>
            <br/><br/>
            <netui:button action="refresh" type="submit">Refresh page</netui:button>
            &nbsp;&nbsp;
            <netui:button action="goNested" type="submit">Goto nested</netui:button>
        </netui:form>
    </center>
</body>
