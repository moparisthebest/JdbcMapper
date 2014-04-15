<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui:html>
    <head>
        <title>
            Binding a parameter map to a string
        </title>
    </head>
<netui:body>
   <netui:anchor action="begin">Post
      <netui:parameterMap map="${pageFlow.string}" />
   </netui:anchor>

</netui:body>
</netui:html>       