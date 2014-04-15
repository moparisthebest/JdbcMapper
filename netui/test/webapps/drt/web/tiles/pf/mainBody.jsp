<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<h1> Main Body Tile </h1>

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
</ul>
<p>Other tests:
<ul>
    <li>
        <netui:anchor href="/coreWeb/tiles/pf/nested/Controller.jpf">
        Nested
        </netui:anchor>
    </li>
    <li>
        <netui:anchor href="/coreWeb/tiles/pf/inherit/Controller.jpf">
        Inherit
        </netui:anchor>
    </li>
    <li>
        <netui:anchor href="/coreWeb/tiles/pf/anotherInherit/Controller.jpf">
        Another Inherit
        </netui:anchor>
    </li>
</ul>

