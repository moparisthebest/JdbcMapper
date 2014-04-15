<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html idScope="html">
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <netui:anchor action="begin">xhtml</netui:anchor><br>
        Action: <netui:span value="${pageFlow.action}" /><br>
        Text: <netui:span value="${pageFlow.text}" />
	<p style="color:green">This is an HTML generated document that contains
	six different types of form submits.  Each type differs in either how
	the form is submitted, or how the form binds to the bean.
	</p>
	<table width="100%">
	<tr><td width="50%">
	<h4>Case One -- Form Submit to an Action</h4>
	   <p style="color:green">Normal button submit, and bean on action</p>
           <netui:form tagId="f1" action="post">
	       Text: <netui:textBox dataSource="actionForm.text" />
	       <netui:button value="submit"/>
	   </netui:form>
         </td><td width="50%">
	<h4>Case Two -- Form Submit To PageFlow</h4>
	   <p style="color:green">Normal button submit, binding to page flow</p>
           <netui:form tagId="f2" action="postNoForm">
	       Text: <netui:textBox dataSource="pageFlow.textPage" />
	       <netui:button value="submit"/>
	   </netui:form>
	</td></tr>
	<tr><td width="50%">
	<h4>Case Three -- Form Submit Override Action</h4>
	   <p style="color:green">Override the action submitted on the button</p>
           <netui:form tagId="f3" action="post">
	       Text: <netui:textBox dataSource="actionForm.text" />
	       <netui:button action="postOverride" value="submit"/>
	   </netui:form>
         </td><td width="50%">
	<h4>Case Four -- Form Submit Override Action/Different Bean</h4>
	   <p style="color:green">Override the action and go to a different bean type</p>
           <netui:form tagId="f4" action="post">
	       Text: <netui:textBox dataSource="actionForm.text" />
	       <netui:button action="postOverrideNewForm" value="submit"/>
	   </netui:form>
	</td></tr>
	<tr><td width="50%">
	<h4>Case Five -- Form Submit/Action UseBean</h4>
	   <p style="color:green">Normal Submit, the action has a useFormBean binding to a bean</p>
           <netui:form tagId="f5" action="postUseFormBean">
	       Text: <netui:textBox dataSource="actionForm.text" />
	       <netui:button value="submit"/>
	   </netui:form>
         </td><td width="50%">
	<h4>Case Six -- Form Submit Direct Bean Binding</h4>
	   <p style="color:green">Normal Submit, the form tag specifies a bean in the Session</p>
           <netui:form tagId="f6" action="postDirect" beanName="FormCases"
		beanType="coretags.form.cases.Controller$Bean" beanScope="session">
	       Text: <netui:textBox dataSource="actionForm.text" />
	       <netui:button value="submit"/>
	   </netui:form>
	</td></tr>
        </table>
    </netui:body>
</netui:html>
