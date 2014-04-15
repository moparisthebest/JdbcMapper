<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Null binding HtmlGroupBase Tag</h4>
    <p style="color:green">This test will bind the null value to the attributes which are defined on
    the HtmlGroupBase tag.  It does this through the checkbox and radio button group which are the two
    subclasses of that tag.  Error are reported if the <b>defaultValue</b> or the <b>optionsDataSource<b> are
    set to a null value.
    <br>
    This is a single page test.
    </p>
    <ul>
    <li><netui:checkBoxGroup dataSource="pageFlow.checked" defaultValue="${pageFlow.nullValue}">
    <netui:checkBoxOption>Option</netui:checkBoxOption>
    </netui:checkBoxGroup> -- defaultValue</li>
    <li><netui:checkBoxGroup dataSource="pageFlow.checked" optionsDataSource="${pageFlow.nullValue}">
    <netui:checkBoxOption>Option</netui:checkBoxOption>
    </netui:checkBoxGroup> -- optionsDataSource</li>
    <li><netui:checkBoxGroup dataSource="pageFlow.checked" labelStyle="${pageFlow.nullValue}">
    <netui:checkBoxOption>Option</netui:checkBoxOption>
    </netui:checkBoxGroup> -- labelStyle</li>
    <li><netui:checkBoxGroup dataSource="pageFlow.checked" labelStyleClass="${pageFlow.nullValue}">
    <netui:checkBoxOption>Option</netui:checkBoxOption>
    </netui:checkBoxGroup> -- labelStyleClass</li>
    <li><netui:checkBoxGroup dataSource="pageFlow.checked" style="${pageFlow.nullValue}">
    <netui:checkBoxOption>Option</netui:checkBoxOption>
    </netui:checkBoxGroup> -- style</li>
    <li><netui:checkBoxGroup dataSource="pageFlow.checked" styleClass="${pageFlow.nullValue}">
    <netui:checkBoxOption>Option</netui:checkBoxOption>
    </netui:checkBoxGroup> -- styleClass</li>
    <li><netui:checkBoxGroup dataSource="pageFlow.checked" orientation="${pageFlow.nullValue}">
    <netui:checkBoxOption>Option</netui:checkBoxOption>
    </netui:checkBoxGroup> -- orientation</li>
    </ul>
    </netui:body>
</netui:html>

  
