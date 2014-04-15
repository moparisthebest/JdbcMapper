<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui:html>
    <head>
        <title>
            Expansion/Contraction of Tree Nodes Containing Script
        </title>
    </head>
    <netui:body>
        <netui:scriptHeader></netui:scriptHeader>
        <h3>Expansion/Contraction of Tree Nodes Containing Script</h3>
        <p>A couple of the nodes in this tree have labels that contain
        JavaScript. This caused a node expand/collapse problem in Mozilla.
        </p>
        <netui:anchor action="resetTrees">Reset Trees</netui:anchor><BR />
        <hr>
        <div style="height: 600px">
        <table width="100%">
        <tr><td valign="top" width="50%">
        <netui:scriptContainer>
        <netui:tree runAtClient="true"
                    dataSource="pageFlow.testTree"
                    selectionAction="postback"
                    tagId="testTree" />
        </netui:scriptContainer>
        </td></tr>
        </table>
        </div>
    </netui:body>
</netui:html>
