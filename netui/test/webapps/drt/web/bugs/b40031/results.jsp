<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui-data:declarePageInput name="string1" type="java.lang.String"/>
<netui-data:declarePageInput name="string2" type="java.lang.String"/>
<netui-data:declarePageInput name="" type="java.lang.String"/>
<netui:html>
    <head>
        <title>
            Web Application Page
        </title>
    </head>
    <body>
        <p>
        string 1:<netui:span value="${pageInput.string1}"/> <br/>
        string 2:<netui:span value="${pageInput.string2}"/> <br/>
        </p>
    </body>
</netui:html>
