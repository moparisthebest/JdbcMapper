<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<html>
   <head>
      <title>MiscJpf Bug 30448 test</title>
   </head>
   <body>
      <h3 align="center">MiscJpf Bug 30448 test - SubJsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <br/>
         <ol>
            <li>
               <netui:anchor action="test1">Raise parent, handled action, no form</netui:anchor>
            </li><br/><br/>
            <li>
               <netui:anchor action="test2">Raise parent, unhandled action, with form</netui:anchor>
            </li><br/><br/>
            <li>
               <netui:anchor action="test3">Raise parent, handled, overloaded action, no form</netui:anchor>
            </li><br/><br/>
            <li>
               <netui:anchor action="test4">Raise parent, handled, overloaded action, with form</netui:anchor>
            </li><br/><br/>
            <li>
               <netui:anchor action="test5">Raise parent, unhandled action, no form</netui:anchor>
            </li><br/><br/>
            <li>
               <netui:anchor action="test6">Raise parent, handled action, with form</netui:anchor>
            </li><br/><br/>
            <li>
               <netui:anchor action="test7">Raise parent, handled action, with form, no annotations</netui:anchor>
            </li><br/><br/>
            <li>
               <netui:anchor action="test8">Raise parent, handled action, no form, with return-form-type annotation</netui:anchor>
            </li><br/><br/>
            <li>
               <netui:anchor action="test9">Raise parent, handled action, no form, with return-form annotation</netui:anchor>
            </li><br/><br/>
         </ul>
       </center>
   </body>
</html>
