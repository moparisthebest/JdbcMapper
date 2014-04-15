<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Web Application Page
        </title>
    </head>
    <body>
        <netui:checkBoxGroup dataSource="pageFlow.value" optionsDataSource="${pageFlow.checkOptions}"></netui:checkBoxGroup><br/>
        <netui:radioButtonGroup dataSource="pageFlow.radioValue" optionsDataSource="${pageFlow.checkOptions}"></netui:radioButtonGroup><br/>
        <netui:select dataSource="pageFlow.value" optionsDataSource="${pageFlow.checkOptions}"></netui:select><br/>
    </body>
</netui:html>
