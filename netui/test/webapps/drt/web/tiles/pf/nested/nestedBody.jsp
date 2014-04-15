<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<netui:span value="${pageFlow.results}"/>

<ul>
    <li>
        <netui:anchor action="backOne" >
        Nested Return to Home
        </netui:anchor>
    </li>
    <li>
        <netui:anchor action="backTwo" >
        Nested Return to Another
        </netui:anchor>
    </li>
    <li>
        <netui:anchor action="nestOne" >
        Local layout Home
        </netui:anchor>
    </li>
</ul>
