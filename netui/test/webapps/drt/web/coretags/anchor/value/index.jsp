<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    <h4>Anchcor Value</h4>
    <p style="color:green">Verify that an anchor value is set.  The first two
    should show the value attribute because it is set and overrides the body.
    The second two, will show the body because the value is not set or is
    the empty string.
    </p>
    <hr>
    <ul>
    <li><netui:anchor action="begin" value="No Body" /></li>
    <li><netui:anchor action="begin" value="Ignore Body">This is the Body</netui:anchor></li>
    <li><netui:anchor action="begin">This is the Body</netui:anchor></li>
    <li><netui:anchor action="begin" value="">Empty String in Value</netui:anchor></li>
    </ul>
    </netui:body>
</netui:html>


  
