<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<script>
    function testClick (element)
    {
        var value = lookupIdByTagId ("123", element);

        alert ("real value: " + document.getElementById (value));
    }
</script>

<netui:scriptContainer generateIdScope="true">
    <netui:hidden tagId="123" dataSource="pageFlow.foo"/>
    <button onClick="testClick (this);">
        click me!
    </button>
</netui:scriptContainer>



