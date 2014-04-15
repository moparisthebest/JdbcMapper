<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>${pageFlow.URI}</h3>

        This test verifies that the @Jpf.ValidateURL annotation works correctly.
        <br/>
        <br/>

        <netui:form action="validateURL">
            URL: <netui:textBox size="50" dataSource="actionForm.URL"/>
                <span style="color:red"><netui:error key="URL"/></span>
            <br/>
            <netui:button action="validateURL" value="validateURL"/>
            <br/>
            <netui:button action="validateURLAllowAllSchemes" value="validateURLAllowAllSchemes"/>
            <br/>
            <netui:button action="validateURLAllowTwoSlashes" value="validateURLAllowTwoSlashes"/>
            <br/>
            <netui:button action="validateURLDisallowFragments" value="validateURLDisallowFragments"/>
            <br/>
            <netui:button action="validateURLAllowOnlySpecialSauceScheme" value="validateURLAllowOnlySpecialSauceScheme"/>
            <br/>
        </netui:form>
    </netui:body>
</netui:html>

  

