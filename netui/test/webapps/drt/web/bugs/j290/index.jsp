<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html idScope="html">
    <head>
        <netui:base/>
    </head>
    <netui:body>
        Action: <netui:span value="${pageFlow.action}" /><br>
        Text: <netui:span value="${pageFlow.text}" />
	<p style="color:green">In this test, we have three forms.  The bug is
	that if one form preceeds another form, and both call actions with
	the same bean type, if the second form has a useFormBean attribute,
	it is hidden by the fact that the first form was processed first.
	The third form is also has a useFormBean, but a different bean.
	The second and third forms should have values when the page is
	first displayed.
	</p>
	<table width="100%">
	<tr><td width="33%" valign="top">
	<h4>Form Bound to Action</h4>
	   <p style="color:green">Form Bound to Action without useFormBean</p>
           <netui:form tagId="f1" action="post">
	       Text: <netui:textBox dataSource="actionForm.text" />
	       <netui:button value="submit"/>
	   </netui:form>
         </td><td width="33%" valign="top">
	<h4>Form Bound to Action with useFormBean</h4>
	   <p style="color:green">Form Bound to Action with same form bean type, but using useFormBean.  This should have a value when the page is first rendered.</p>
           <netui:form tagId="f2" action="post2">
	       Text: <netui:textBox dataSource="pageFlow.text" />
	       <netui:button value="submit"/>
	   </netui:form>
	</td><td width="33%" valign="top">
	<h4>useFormBean With Different Bean type</h4>
	   <p style="color:green">This form is also bound using useFormBean, but to a different Bean type. This should have a value when the page is first rendered.</p>
           <netui:form tagId="f3" action="post3">
	       Text: <netui:textBox dataSource="actionForm.text" />
	       <netui:button value="submit"/>
	   </netui:form>
	</td></tr>
        </table>
    </netui:body>
</netui:html>
