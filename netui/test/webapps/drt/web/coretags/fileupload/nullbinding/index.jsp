<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    <h4>FileUpload Null Binding</h4>
    <p style="color:green">Test of null binding to the attributes of the fileUpload.  All of these should be
    ignored except for the tagId which will report an error.
    <br>
    This is a single page test.  
    </p>
    <hr>
    <ul>
    <li><netui:fileUpload dataSource="pageFlow.file[0]" accept="${pageFlow.nullValue}"/> -- accept</li>
    <li><netui:fileUpload dataSource="pageFlow.file[1]" size="${pageFlow.nullValue}"/> -- size</li>
    <li><netui:fileUpload dataSource="pageFlow.file[2]" title="${pageFlow.nullValue}"/> -- title</li>
    <li><netui:fileUpload dataSource="pageFlow.file[3]" tabindex="${pageFlow.nullValue}"/> -- tabindex</li>
    </ul>
    <hr>
    <ul>
    <li><netui:fileUpload dataSource="pageFlow.file[4]" tagId="${pageFlow.nullValue}"/> -- tagId</li>
    </ul>
    
    </netui:body>
</netui:html>

  
