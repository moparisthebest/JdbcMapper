<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
   <head>
      <title>MiscJpf Bug42846</title>
   </head>
   <body>
      <h3 align="center">MiscJpf Bug42846 - Jsp1.jsp</h3>
      <hr width="95%"/>
      <br/>
      <center>
         <br/>
         <h2><font color="blue">Testing PageFlow Scoping</font></h2>
         The two links below will pop new browser windows.  Pop them both then
         change the values in the text boxes and submit the changes.  Make sure
         that the changes made in window do not effect the other window.
         <br/><br/>
         <netui:anchor  href="/coreWeb/miscJpf/bug42846/jpfA/Jpf1.jpf"
                        target="_windowA" >JpfA</netui:anchor>
         <br/><br/>
         <netui:anchor  href="/coreWeb/miscJpf/bug42846/jpfB/Jpf1.jpf"
                        target="_windowB" >JpfB</netui:anchor>
         <br/><br/>
         <netui:anchor action="finish">Finish</netui:anchor>
      </center>
   </body>
</html>
