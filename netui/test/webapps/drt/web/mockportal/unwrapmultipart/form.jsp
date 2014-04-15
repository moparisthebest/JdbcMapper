<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0" %>

<br/>
testAttr value: <%= (String) request.getAttribute( "testAttr" ) %>
<br/>
<br/>
<br/>
Multipart Request:
<br/>
<netui:form action="getFile" enctype="multipart/form-data" >
    <table>

        <tr>
            <td>File:</td>
            <td><netui:fileUpload dataSource="actionForm.file" size="25" /></td>
        </tr>
        <tr align="center">
            <td colspan="2">
                <button type="submit">Submit</button>
            </td>
        </tr>
    </table>
</netui:form>


"Regular" request:
<br/>
<netui:form action="getFile" >
    <table>
        <tr align="center">
            <td colspan="2">
                <button type="submit">Submit</button>
            </td>
        </tr>
    </table>
</netui:form>


