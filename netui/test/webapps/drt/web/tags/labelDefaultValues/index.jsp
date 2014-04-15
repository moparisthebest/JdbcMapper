<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Web Application Page
        </title>
    </head>
    <body>
    [nbsp]: '<netui:span escapeWhiteSpaceForHtml="false"  value="${pageFlow.value}" defaultValue="&nbsp;"/>'<br />
    amp: '<netui:span value="${pageFlow.value}" defaultValue="&"/>'<br />
    &amp;nbsp: '<netui:span value="${pageFlow.value}" defaultValue="&amp;nbsp;"/>'<br />
    &amp;amp: '<netui:span value="${pageFlow.value}" defaultValue="&amp"/>'<br />
    foo[copy]: '<netui:span value="${pageFlow.value}" defaultValue="foo &copy;"/>'<br />
    foo[nbsp nbsp]bar: '<netui:span value="${pageFlow.value}" defaultValue="foo&nbsp;&nbsp;bar"/>'<br />
    [nbsp]: '<netui:span value="${pageFlow.value}" defaultValue="&#160;"/>'<br />
    spacer: '<netui:span value="${pageFlow.spacer}"/>'<br />
    </body>
</netui:html>
