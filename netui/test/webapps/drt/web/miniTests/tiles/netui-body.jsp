<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<h1> Main Body Tile - NetUI HTML tags</h1>

<netui:span value="${pageFlow.results}"/>

<ul>
    <li>
        <netui:anchor action="home">
        Default layout - using NetUI HTML tags
        </netui:anchor>
    </li>
    <li>
        <netui:anchor action="struts">
        Default layout - using Struts HTML tags
        </netui:anchor>
    </li>
    <li>
        <netui:anchor action="blank">
        Blank body
        </netui:anchor>
    </li>
    <li>
        <netui:anchor action="edit">
        Change to edit menu
        </netui:anchor>
    </li>
    <li>
        <netui:anchor action="reversePanels">
        Reverse side panels
        </netui:anchor>
    </li>
    <li>
        <netui:anchor action="changeBody">
        Change the body layout
        </netui:anchor>
    </li>
    <li>
        <netui:anchor action="alternate">
        Alternative Layout
        </netui:anchor>
    </li>
</ul>

