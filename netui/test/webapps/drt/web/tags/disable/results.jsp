<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.beehive.netui.script.common.DataAccessProviderBean"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <body>
    <h4>Disable Form Post Results</h4>
    <p style="color:green">
    This test dumps the contents ofthe request parameter names posted by the disable test. The only control not disabled
    is a select that has individual options disabled.  It also reports
    the contents of the form that was posted to the action.
    </p>
    <hr>
    <h4>Parameter Data</h4>
    <%
    java.util.Enumeration e = request.getParameterNames();
    java.util.List l = java.util.Collections.list(e);
    request.setAttribute("parameterNames",l);
    %>
    <netui-data:repeater dataSource="requestScope.parameterNames">
        <netui-data:repeaterHeader><ul></netui-data:repeaterHeader>
        <netui-data:repeaterItem>
                <li><netui:span value="${container.item}"/></li>
                <%
                DataAccessProviderBean bean = (DataAccessProviderBean) pageContext.getAttribute("container");
                String name = (String) bean.getItem();
                request.setAttribute("values",request.getParameterValues(name));
                %>
            </netui-data:repeaterItem>
        <netui-data:repeaterFooter></ul></netui-data:repeaterFooter>
    </netui-data:repeater>
    <hr>
    <h4>Form Data</h4>
    <table border="1" cellspacing="0">
    <tr><td>TextBox</td><td><netui:span value="${requestScope.form.textBox}"/>&nbsp;</td></tr>
    <tr><td>TextArea</td><td><netui:span value="${requestScope.form.textArea}"/>&nbsp;</td></tr>
    <tr><td>Select/Options</td><td>
        <ul>
        <netui-data:repeater dataSource="requestScope.form.select">
           <li><netui:span value="${container.item}"/></li>
        </netui-data:repeater>
        </ul>
    &nbsp;</td></tr>
    <tr><td>Select/Options Source:</td><td>
        <ul>
        <netui-data:repeater dataSource="requestScope.form.selectThree">
           <li><netui:span value="${container.item}"/></li>
        </netui-data:repeater>
        </ul>
    &nbsp;</td></tr>
    <tr><td>Select (option Disabled):</td><td>
        <ul>
        <netui-data:repeater dataSource="requestScope.form.selectTwo">
           <li><netui:span value="${container.item}"/></li>
        </netui-data:repeater>
        </ul>
    &nbsp;</td></tr>
    <tr><td>radioGroup</td><td><netui:span value="${requestScope.form.radioGroup}"/>&nbsp;</td></tr>
    <tr><td>radioGroup2</td><td><netui:span value="${requestScope.form.radioGroup2}"/>&nbsp;</td></tr>
    <tr><td>check1</td><td><netui:span value="${requestScope.form.check1}"/>&nbsp;</td></tr>
    <tr><td>check2</td><td><netui:span value="${requestScope.form.check2}"/>&nbsp;</td></tr>
    <tr><td>CheckBox Group/Options Source:</td><td>
        <ul>
        <netui-data:repeater dataSource="requestScope.form.checkOne">
           <li><netui:span value="${container.item}"/></li>
        </netui-data:repeater>
        </ul>
    &nbsp;</td></tr>
    <tr><td>CheckBox Group/Options</td><td>
        <ul>
        <netui-data:repeater dataSource="requestScope.form.checkTwo">
           <li><netui:span value="${container.item}"/></li>
        </netui-data:repeater>
        </ul>
    &nbsp;</td></tr>
    </table>
    </body>
</netui:html>
