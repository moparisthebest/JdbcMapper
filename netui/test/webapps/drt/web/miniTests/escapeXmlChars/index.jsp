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

        <p>
            This test ensures that trouble-causing XML characters are properly escaped in XML
            attributes and XML text values, in generated Struts and Validator config files.
        </p>
        <p>
            The <code>condition</code> attribute on <code>@Jpf.ConditionalForward</code> and the
            <code>regex</code> attribute on <code>@Jpf.ValidateMask</code> contain characters that
            need to be escaped in generated XML attributes and XML text values, respectively.
        </p>

        <hr/>
        <netui:form action="submit">
            match regex pattern <code>^[' "=,\\&lt;&gt;#|&amp;~?(){}%*]*$</code>:
                <netui:textBox dataSource="actionForm.foo"/>
                <span style="color:red"><netui:error key="foo"/></span>
            <netui:button value="submit"/>
        </netui:form>

        Conditional forwards against condition=<code>param.x &lt; 5 || param.x &gt; 10 || param.y == "hi"</code>:
        <blockquote>
            <netui:anchor action="begin">
                <netui:parameter name="x" value="3"/>
                begin.do?x=3 -&gt; success.jsp
            </netui:anchor>
            <br/>
            <netui:anchor action="begin">
                <netui:parameter name="x" value="11"/>
                begin.do?x=11 -&gt; success.jsp
            </netui:anchor>
            <br/>
            <netui:anchor action="begin">
                <netui:parameter name="y" value="hi"/>
                begin.do?y=hi -&gt; success.jsp
            </netui:anchor>
            <br/>
            <netui:anchor action="begin">
                <netui:parameter name="x" value="7"/>
                begin.do?x=7 -&gt; index.jsp
            </netui:anchor>
        </blockquote>
    </netui:body>
</netui:html>

  

