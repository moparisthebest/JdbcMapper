<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    <h4>Select Empty OptionDataSource</h4>
    <p style="color:green">This is a test of an empty array being used as an options
    data source for a repeating select.  The result should be that the select has no content.
    There should be an error either on the console or page.
    </p>
    <hr>
        <netui:select dataSource="pageFlow.select" optionsDataSource="${pageFlow.options}" repeater="true">
            <netui:selectOption repeatingType="Option" value="${container.item}">
            </netui:selectOption>
        </netui:select>   
    </netui:body>
</netui:html>

  
