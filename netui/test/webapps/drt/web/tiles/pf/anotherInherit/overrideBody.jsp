<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<h1> Another Main Body Tile </h1>

<netui:span value="${pageFlow.results}"/>

<ul>
    <li>
        <netui:anchor action="home">
        Default layout
        </netui:anchor>
    </li>
    <li>
        <netui:anchor action="blank">
        Blank body
        </netui:anchor>
    </li>
    <li>
        <netui:anchor action="another">
        Change to another page
        </netui:anchor>
    </li>
    <li>
        <netui:anchor action="local">
        Change to local page
        </netui:anchor>
    </li>
</ul>

