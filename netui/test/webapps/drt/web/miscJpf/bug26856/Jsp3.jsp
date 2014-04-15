<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>MiscJpf Bug 26856 test</title>
    </head>
    <body>
        <h3 align="center">MiscJpf Bug 26856 test - Jsp1.jsp</h3>
        <hr width="95%"/>
        <br/>
        <h3>Test 1</h3>
        <em>Message from default resource bundle.</em>
        <br/>
        <netui:span value="${bundle.default.message1}"/>

        <hr width="95%"/>
        <br/>
        <h3>Test 2</h3>
        <em>Message from named resource bundle, using quotes.</em>
        <br/>
        <netui:span value="${bundle[\"myMessages/miscJpf/bug26856\"].message1}"/>

        <hr width="95%"/>
        <br/>
        <h3>Test 3</h3>
        <em>Message from named resource bundle, using single quotes.</em>
        <br/>
        <netui:span value="${bundle['myMessages/miscJpf/bug26856'].message1}"/>

        <hr width="95%"/>
        <br/>
        <h3>Test 4</h3>
        <em>Non-existant message from default resource bundle.  An error is expected.</em>
        <br/>
         <% try { %>
        <netui:span value="${bundle.default.nonExistentMessage}"/>
         <% } catch ( Exception e ) { pageContext.getOut().print( e.getMessage() ); } %>

        <hr width="95%"/>
        <br/>
        <h3>Test 5</h3>
        <em>Message from non-existant resource bundle.  An error is expected.</em>
        <br/>
         <% try { %>
        <netui:span value="${bundle.missingBundle.nonExistentMessage}"/><br/>
         <% } catch ( Exception e ) { pageContext.getOut().print( e.getMessage() ); } %>

        <hr width="95%"/>
        <br/>
        <h3>Test 6</h3>
        <em>"Overloaded" bundle name.</em>
        <br/>
        <netui-data:declareBundle name="myBundle1" bundlePath="miscJpf.bug26856.bundle2"/>
        <netui:span value="${bundle[\"myBundle1/miscJpf/bug26856\"].message2}"/>
        <br/>
        <netui:span value="${bundle.myBundle1.message2}"/>

        <hr width="95%"/>
        <br/>
        <h3>Test 7</h3>
        <em>Message with value substitution. SubTest 7 will give an error.</em>
        <br/>
        <ol>
            <li> <!-- Test 1 -->
                <netui-data:message value="${bundle.myBundle1.message3}" resultId="formattedMessage">
                    <netui-data:messageArg value="Arg0"/>
                    <netui-data:messageArg value="Arg1"/>
                    <netui-data:messageArg value="Arg2"/>
                </netui-data:message>
                <netui:span value="${pageScope.formattedMessage}"/>
            </li>
            <li> <!-- Test 2 -->
                <netui-data:message value="${bundle.myBundle1.message3}" resultId="formattedMessage">
                    <netui-data:messageArg value="Arg0"/>
                    <netui-data:messageArg value="Arg1"/>
                </netui-data:message>
                <netui:span value="${pageScope.formattedMessage}"/>
            </li>
            <li> <!-- Test 3 -->
                <netui-data:message value="${bundle.myBundle1.message3}" resultId="formattedMessage">
                    <netui-data:messageArg value="Arg0"/>
                    <netui-data:messageArg value="Arg1"/>
                    <netui-data:messageArg value="Arg2"/>
                    <netui-data:messageArg value="Arg3"/>
                </netui-data:message>
                <netui:span value="${pageScope.formattedMessage}"/>
            </li>
            <li> <!-- Test 4 -->
                <netui-data:message value="${bundle.myBundle1.message4}" resultId="formattedMessage">
                    <netui-data:messageArg value="Arg0"/>
                    <netui-data:messageArg value="Arg1"/>
                    <netui-data:messageArg value="Arg2"/>
                </netui-data:message>
                <netui:span value="${pageScope.formattedMessage}"/>
            </li>
            <li> <!-- Test 5 -->
                <netui-data:message value="${bundle.myBundle1.message5}" resultId="formattedMessage">
                    <netui-data:messageArg value="Arg0"/>
                    <netui-data:messageArg value="Arg1"/>
                    <netui-data:messageArg value="Arg2"/>
                </netui-data:message>
                <netui:span value="${pageScope.formattedMessage}"/>
            </li>
            <li> <!-- Test 6 -->
                <netui-data:message value="${bundle.myBundle1.message6}" resultId="formattedMessage">
                    <netui-data:messageArg value="Arg0"/>
                    <netui-data:messageArg value="Arg1"/>
                    <netui-data:messageArg value="Arg2"/>
                </netui-data:message>
                <netui:span value="${pageScope.formattedMessage}"/>
            </li>
            <li> <!-- Test 7 -->
                <netui-data:message value="${bundle.myBundle1.message7}" resultId="errorMessage">
                    <netui-data:messageArg value="Arg0"/>
                    <netui-data:messageArg value="Arg1"/>
                    <netui-data:messageArg value="Arg2"/>
                </netui-data:message>
                <netui:span value="${pageScope.errorMessage}"/>
            </li>
        </ol>
        <hr width="95%"/><br/><br/>
        <center>
            <netui:anchor action="done">Done</netui:anchor>
        </center>
    </body>
</html>
