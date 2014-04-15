<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    <h4>CheckBox Null Binding</h4>
    <p style="color:green">This test binds the primary attributes of the checkbox tag to a null value.
    In the first set of tests, the value is ignored.  In the second set of tests, the default value is
    output.  In the third set of tests, <b>tagId</b> will result in an error and the <b>defaultValue</b> will
    result in a false.
    <br>
    This is a single page test.  
    </p>
    <hr>
    <ul>
    <li><netui:checkBox dataSource="pageFlow.values[0]" alt="${pageFlow.nullValue}" /> -- alt</li>
    <li><netui:checkBox dataSource="pageFlow.values[1]" title="${pageFlow.nullValue}" /> -- title</li>
    </ul>
    <hr>
    <ul>
    <li><netui:checkBox dataSource="pageFlow.values[2]" tabindex="${pageFlow.nullValue}" /> -- tabindex</li>
    <li><netui:checkBox dataSource="pageFlow.values[3]" accessKey="${pageFlow.nullValue}"/> -- accessKey</li>
    </ul>
    <hr>
    <ul>
    <li><netui:checkBox dataSource="pageFlow.values[4]" tagId="${pageFlow.nullValue}" /> -- tagId</li>
    <li><netui:checkBox dataSource="pageFlow.nullBoolean" defaultValue="${pageFlow.nullValue}"/> -- defaultValue</li>
    </ul>
    <hr>
    <ul>
    
    </netui:body>
</netui:html>

  
