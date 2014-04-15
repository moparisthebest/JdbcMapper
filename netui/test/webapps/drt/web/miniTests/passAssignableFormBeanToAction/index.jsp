<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>${pageFlow.URI}</h3>

        action2 accepts a form bean of type <code>BaseFormBean</code>.  In the first case, we pass it an instance of that.  In the second case, we pass it an instance of the derived <code>SubclassFormBean</code>.
        <br/>
        <br/>
        action3 accepts interface <code>SomeInterface</code>.  In the third case, we pass it an instance of <code>SubclassFormBean</code>, which implements the interface.
        <br/>
        <br/>
        value in form bean: <b>${pageInput.formValue}</b>
        <br/>
        <br/>
        <netui:anchor action="passBaseToAction2">pass base form bean to action2</netui:anchor>
        <br/>
        <netui:anchor action="passSubclassToAction2">pass subclass form bean to action2</netui:anchor>
        <br/>
        <netui:anchor action="passInterfaceImplementorToAction3">pass subclass form bean (which implements the right interface) to action3</netui:anchor>
    </netui:body>
</netui:html>

  

