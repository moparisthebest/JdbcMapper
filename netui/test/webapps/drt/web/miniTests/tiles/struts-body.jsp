<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>

<h1> Main Body Tile - Struts HTML tags</h1>

<ul>
    <li>
        <html:link page="/home.do">
        Default layout - using NetUI HTML tags
        </html:link>
    </li>
    <li>
        <html:link page="/struts.do">
        Default layout - using Struts HTML tags
        </html:link>
    </li>
    <li>
        <html:link page="/blank.do">
        Blank body
        </html:link>
    </li>
    <li>
        <html:link page="/edit.do">
        Change to edit menu
        </html:link>
    </li>
    <li>
        <html:link page="/reversePanels.do">
        Reverse side panels
        </html:link>
    </li>
    <li>
        <html:link page="/changeBody.do">
        Change the body layout
        </html:link>
    </li>
    <li>
        <html:link page="/alternate.do">
        Alternative Layout
        </html:link>
    </li>
</ul>

