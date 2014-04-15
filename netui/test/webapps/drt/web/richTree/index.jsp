<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>Tree Tests</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <netui:base/>
    </head>
    <netui:body>
    <netui:anchor action="initTrees">Reinitialize Trees</netui:anchor>
    <table width="100%" border='1' cellpadding='1' cellspacing='0'>
    <caption><b>Base Tree Tests</b></caption>
    <tr><th>Test</th><th>Description</th><th>Action</th><th>JSP</th><th>Tree</th></tr>
    <tr><td><netui:anchor action="goBaseTree">RichTreeDynamicBaseTree</netui:anchor></td>
	<td>Simplest Tree Defined Dynamically</td><td>goBaseTree</td><td>baseTree.jsp</td>
	<td>tree1</td></tr>
    <tr><td><netui:anchor action="goStaticBaseTree">RichTreeStaticBaseTree</netui:anchor></td>
	<td>Simplest Tree Defined Statically</td><td>goStaticBaseTree</td><td>baseTreeTwo.jsp</td>
	<td>tree2</td></tr>
    <tr><td><netui:anchor action="goSelectStyles">RichTreeSelectionStyle</netui:anchor></td>
	<td>Simplest Tree with selection styles</td><td>goSelectStyles</td><td>selectStyles.jsp</td>
	<td>tree3</td></tr>
    <tr><td><netui:anchor action="goTreeStyle">RichTreeTreeStyle</netui:anchor></td>
	<td>Simplest Tree the tree style set</td><td>goTreeStyle</td><td>treeStyle.jsp</td>
	<td>tree4</td></tr>
    <tr><td><netui:anchor action="goTreeHtml">RichTreeTreeHtml</netui:anchor></td>
	<td>Tree Items specify their own label through HTML content</td><td>goTreeHtml</td><td>treeHtml.jsp</td>
	<td>tree10</td></tr>
    <tr><td><netui:anchor action="goTreeHtmlTwo">RichTreeTreeHtmlTwo</netui:anchor></td>
	<td> Tree Items specify their own label through HTML content</td><td>goTreeHtmlTwo</td><td>treeHtmlTwo.jsp</td>
	<td>tree11</td></tr>
    <tr><td><netui:anchor action="goContent">RichTreeContent</netui:anchor></td>
	<td>Demonstrate the use of tree content.</td><td>goContent</td><td>content.jsp</td>
	<td>tree12</td></tr>
    <tr><td><netui:anchor action="goDisabled">RichTreeDisabled</netui:anchor></td>
	<td>Disabled Nodes within the tree</td><td>goDisabled</td><td>disabled.jsp</td>
	<td>tree16</td></tr>
    <tr><td><netui:anchor action="goOverride">RichTreeOverride</netui:anchor></td>
	<td>Override tree attributes</td><td>goOverride</td><td>override.jsp</td>
	<td>Tree17</td></tr>
    <tr><td><netui:anchor action="goOverrideTwo">RichTreeOverrideTwo</netui:anchor></td>
	<td>Override the whole tree actions from the root</td><td>goOverrideTwo</td><td>overrideTwo.jsp</td>
	<td>tree18</td></tr>
    <tr><td><netui:anchor action="goHref">RichTreeHref</netui:anchor></td>
	<td>Verify that HRefs and target work</td><td>goHref</td><td>href.jsp</td>
	<td>tree19</td></tr>
    <tr><td><netui:anchor action="goSelectAction">RichTreeSelectAction</netui:anchor></td>
	<td>Verify that selection action target works</td><td>goSelectAction</td><td>selectAction.jsp</td>
	<td>tree37</td></tr>
    <tr><td><netui:anchor action="goEncodeContent">RichTreeEncodeContent</netui:anchor></td>
	<td>Verify escape for HTML works on labels and content</td><td>*goEncodeContent*</td><td>encodeContent.js</td>
	<td>tree20</td></tr>
    <tr><td><netui:anchor action="goNoRoot">RichTreeNoRoot</netui:anchor></td>
	<td>Verify expand on client on a rootless tree</td><td>goNoRoot</td><td>noRoot.jsp</td>
	<td>tree21</td></tr>
    </table>
    <br>
    <table width="100%" border='1' cellpadding='1' cellspacing='0'>
    <caption><b>Client Side Tests</b></caption>
    <tr><th>Test</th><th>Description</th><th>Action</th><th>JSP</th><th>Tree</th></tr>
    <tr><td><netui:anchor action="goBaseClient">RichTreeRunAtClient</netui:anchor></td>
	<td>Simplest Tree with runAtClient Support.</td><td>goBaseClient</td><td>baseClient.jsp</td>
	<td>tree8</td></tr>
    <tr><td><netui:anchor action="goClientContent">RichTreeClientContent</netui:anchor></td>
	<td>Client expansion with content and labels</td><td>goClientContent</td><td>clientContent.jsp</td>
	<td>tree13</td></tr>
    <tr><td><netui:anchor action="goContentAnchor">RichTreeContentAnchor</netui:anchor></td>
	<td>Using an Anchor inside of the Content with runAtClient</td><td>goContentAnchor</td><td>contentAnchor</td>
	<td>tree14</td></tr>
    <tr><td><netui:anchor action="goClientContentAnchor">RichTreeClientContentAnchor</netui:anchor></td>
	<td>Using an Anchor inside of the Content</td><td>goClientContentAnchor</td><td>clientContentAnchor.jsp</td>
	<td>tree15</td></tr>
    <tr><td><netui:anchor action="goClientContentForm">RichTreeClientContentForm</netui:anchor></td>
	<td>Using an Form inside of the Content</td><td>goClientContentForm</td><td>clientContentForm.jsp</td>
	<td>tree22</td></tr>
    <tr><td><netui:anchor action="goTreeSC">RichTreeSC</netui:anchor></td>
	<td>Running a runAtClient tree in a script container</td><td>goTreeSC</td><td>treeSC.jsp</td>
	<td>tree24</td></tr>
    <tr><td><netui:anchor action="goTreeSC2">RichTreeSC2</netui:anchor></td>
	<td>Running a runAtClient tree in a script container</td><td>goTreeSC2</td><td>treeSC2.jsp</td>
	<td>tree28 tree29</td></tr>
    <tr><td><netui:anchor action="goTreeSC3">RichTreeSC3</netui:anchor></td>
	<td>Running a runAtClient tree in a script container</td><td>goTreeSC3</td><td>treeSC3.jsp</td>
	<td>tree30 tree31</td></tr>
    <tr><td><netui:anchor action="goTreeSC4">RichTreeSC4</netui:anchor></td>
	<td>Verification of tagId's on a tree</td><td>goTreeSC4</td><td>treeSC4.jsp</td>
	<td>tree34 tree35</td></tr>
    <tr><td><netui:anchor action="goRunAtClient2">RichTreeRunAtClient2</netui:anchor></td>
	<td>Multiple tree controls doing runAtClient</td><td>goRunAtClient2</td><td>runAtClient2.jsp</td>
	<td>tree25 tree26</td></tr>
    <tr><td><netui:anchor action="goExpandOnServer">RichTreeExpandOnServer</netui:anchor></td>
	<td> Base test of expand on server.</td><td>goExpandOnServer</td><td>expandOnServer.jsp</td>
	<td>tree27</td></tr>
    <tr><td><netui:anchor action="goDynamicClient">RichTreeDynamicClient</netui:anchor></td>
	<td>Dynamically created runAtClient</td><td>goDynamicClient</td><td>dynamicClient.jsp</td>
	<td>tree36</td></tr>
    </table>
    <br>
    <table width="100%" border='1' cellpadding='1' cellspacing='0'>
    <caption><b>Layout Bugs</b></caption>
    <tr><th>Test</th><th>Description</th><th>Action</th><th>JSP</th><th>Tree</th></tr>
    <tr><td><netui:anchor action="goCr180331">RichTreeInvalidLayout</netui:anchor></td>
	<td>Layout of a tree four layers deep.</td><td>goCr180331</td><td>cr180331.jsp</td>
	<td>tree5</td></tr>
    <tr><td><netui:anchor action="goCr182056">RichTreeLongLabels</netui:anchor></td>
	<td>Layout of a tree with long label names.</td><td>goCr182056</td><td>cr182056.jsp</td>
	<td>tree6</td></tr>
    </table>    
    <br>
    <table width="100%" border='1' cellpadding='1' cellspacing='0'>
    <caption><b>Error Conditions</b></caption>
    <tr><th>Test</th><th>Description</th><th>Action</th><th>JSP</th><th>Tree</th></tr>
    <tr><td><netui:anchor action="goEmptyTree">RichTreeEmptyTree</netui:anchor></td>
	<td>Just a Tree tag without backing TreeNode</td><td>goEmptyTree</td><td>emptyTree.jsp</td>
	<td>tree9</td></tr>
    <tr><td><netui:anchor action="goTreeBinding">RichTreeBindingError</netui:anchor></td>
	<td>Invalid Binding a tree.</td><td>goTreeBinding</td><td>treeBinding.jsp</td>
	<td>no tree</td></tr>
    <tr><td><netui:anchor action="goWriteTreeError">RichTreeWriteTreeError</netui:anchor></td>
	<td>No property defined to set the tree.</td><td>goWriteTreeError</td><td>writeTreeError.jsp</td>
	<td>tree7</td></tr>
    <tr><td><netui:anchor action="goRunAtClientError">RichTreeRunAtClientError</netui:anchor></td>
	<td>runAtClient set on the tree, but not on the HTML tag.</td><td>goRunAtClientError</td><td>runAtClientError.jsp</td>
	<td>tree23</td></tr>
    <tr><td><netui:anchor action="goTreeSCError">RichTreeSCError</netui:anchor></td>
	<td>Running a runAtClient tree outside a scriptContainer</td><td>goTreeSCError</td><td>treeSCError.jsp</td>
	<td>tree32 tree33</td></tr>
    </table>    
    <ul>
    <li>Need to verify a node with both an href and action defined</li>
    </ul>    
    </netui:body>
</netui:html>

  
