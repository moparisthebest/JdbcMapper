<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<h4>Dynamic Tree with RunAtClient and ExpandOnClient</h4>
<p style="color:green">This test creates a dynamic tree that is both runAtClient and expandOnServer.  The
children are created during the onExpand method.</p>
<netui:anchor action="reset">Reset</netui:anchor>
<!--Begin scriptContainer-->
<div style="border: thin solid;height: 400px;">
<netui:scriptContainer>
    <netui:scriptHeader/>
    <netui:tree dataSource="pageFlow.root" escapeForHtml="true"
        selectionAction="select" tagId="tree2" runAtClient="true"
        selectedStyle="background-color: #FFD185; font-color: #FFFFFF;"/>
</netui:scriptContainer>
</div>
<!--End scriptContainer-->
