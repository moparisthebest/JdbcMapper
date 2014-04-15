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

        This test verifies that a <code>@Jpf.FormBean</code> annotation is inherited from a superclass
        if none is provided on the derived class, and that it is overridden if the derived class has one.
        
        <ul>
            <li>
                The first <code>netui:form</code> uses the base class, which defines message resources
                in its <code>@Jpf.FormBean</code> annotation.
            </li>
            <li>
                The second <code>netui:form</code> uses a derived class, which simply inherits the
                <code>@Jpf.FormBean</code> annotation.
            </li>
            <li>
                The second <code>netui:form</code> uses a derived class, which overrides the
                <code>@Jpf.FormBean</code> annotation.
            </li>
        </ul>

        In each case, leave the text box empty when you press Submit.
        <br/>
        <br/>
        <netui:form action="submitBaseFormBean">
            foo: <netui:textBox dataSource="actionForm.foo"/><netui:error key="foo"/>
            <br/>
            <netui:button value="Submit BaseFormBean"/>
        </netui:form>
        <netui:form action="submitDerivedFormBean">
            bar: <netui:textBox dataSource="actionForm.bar"/><netui:error key="bar"/>
            <netui:hidden dataSource="actionForm.foo" dataInput="something"/>
            <br/>
            <netui:button value="Submit DerivedFormBean"/>
        </netui:form>
        <netui:form action="submitOverridingFormBean">
            baz: <netui:textBox dataSource="actionForm.baz"/><netui:error key="baz"/>
            <netui:hidden dataSource="actionForm.foo" dataInput="something"/>
            <br/>
            <netui:button value="Submit OverridingFormBean"/>
        </netui:form>
    </netui:body>
</netui:html>

  

