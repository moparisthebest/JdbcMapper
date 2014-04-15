<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    <h4>Form Null Binding</h4>
    <p style="color:green">Test bind null value to the attributes of a form.  Below are two set of tests against form.
    In the first, we are binding to the attributes who's values are ignored and no attribute is output.  In the second
    a value is required so an error is generated.
    <br>
    This is a single page test.  
    </p>
    <hr>
    <ul>
    <li><netui:form action="begin" style="display:inline" focus="${pageFlow.nullValue}"></netui:form><span> -- focus</span></li>
    <li><netui:form action="begin" style="display:inline" target="${pageFlow.nullValue}"></netui:form><span> -- target</span></li>
    <li><netui:form action="begin" style="display:inline" enctype="${pageFlow.nullValue}" ></netui:form><span> -- enctype</span></li>
    <li><netui:form action="begin" style="display:inline" location="${pageFlow.nullValue}" ></netui:form><span> -- location</span></li>
    <li><netui:form action="begin" style="display:inline" title="${pageFlow.nullValue}" ></netui:form><span> -- title</span></li>
    </ul>
    <hr>
    <ul>
    <li><netui:form action="begin" style="display:inline" method="${pageFlow.nullValue}" ></netui:form><span> -- method</span></li>
    <li><netui:form action="begin" style="display:inline" beanScope="${pageFlow.nullValue}" ></netui:form><span> -- scope</span></li>
    <li><netui:form action="${pageFlow.nullValue}" style="display:inline"></netui:form><span> -- action</span></li>
    <li><netui:form action="begin" style="display:inline" tagId="${pageFlow.nullValue}"></netui:form><span> -- tagId</span></li>
    <li><netui:form action="begin" style="display:inline" beanType="${pageFlow.nullValue}" ></netui:form><span> -- type</span></li>
    <li><netui:form action="begin" style="display:inline" beanName="${pageFlow.nullValue}" beanType="coretags.form.nullbinding.Controller.$FormBean"></netui:form><span> -- name</span></li>
    </ul>
    
    </netui:body>
</netui:html>

  
