<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="netui-tags-html.tld" prefix="netui" %>
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data" %>

<html>
    <head>
        <title>QA Trace results</title>
    </head>
    <body>
        <h3 align="center" style="color: green;">QA Trace results - TraceResults.jsp</h3>
        <hr width="95%"/>
        <br/>
        <ul>
            <netui-data:repeater dataSource="{session.QaTrace.tracePoints}">
                <li>
                    <netui:label value="{container.item}" />
                </li>
            </netui-data:repeater>
        </ul>
        <center>
            <hr width="95%"/>
            <br/>
            <a href="done.jsp">Okay</a>
        </center>
    </body>
</html>
