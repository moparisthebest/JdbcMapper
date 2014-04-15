<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>${pageFlow.URI}</h3>

        <p>
            This test ensures that page flow nesting is friendly to the browser back button.  To run
            it, execute the following sequence:
        </p>
        <ul>
            <li>Click the 'goB' link.</li>
            <li>Click 'done' on the next page.</li>
            <li>Click the 'goC' link.</li>
            <li>Use the back button to get back to the previous page (the one with 'goB' and 'goC').</li>
            <li>Refresh that page in the browser.</li>
        </ul>
        <p>
            Without the fix to this bug, you would get an action-not-found error for 'bDone' in page
            flow C.  With the fix, you are properly returned to A.
        </p>

        <hr/>
        <netui:anchor action="goB">goB</netui:anchor>
        <br/>
        <netui:anchor action="goC">goC</netui:anchor>
    </netui:body>
</netui:html>

  

