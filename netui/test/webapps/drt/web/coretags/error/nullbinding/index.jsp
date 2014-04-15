<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Null Binding to the Error and Errors tag</h4>
    <p style="color:green">This test will test both the Error and Errors tag for null binding to their attributes.
    The first set of tests are the error tags.
    <br>
    This is a single page test.
    </p>
    <ul>
    <li><netui:error key="val" bundleName="${pageFlow.nullValue}" /> -- error - bundle</li>
    <li><netui:error key="${pageFlow.nullValue}" /> -- error - value</li>
    <li><netui:error key="val" locale="${pageFlow.nullValue}" /> -- error - locale</li>
    <li><netui:errors bundleName="${pageFlow.nullValue}" /> -- errors - bundle</li>
    <li><netui:errors locale="${pageFlow.nullValue}" /> -- errors - locale</li>
     </ul>
    </netui:body>
</netui:html>

  
