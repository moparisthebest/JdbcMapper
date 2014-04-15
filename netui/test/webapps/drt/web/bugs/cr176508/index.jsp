<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <body>
        <p style="color:#339900;">This test verifies that within a select box the proper options are selected based upon
        the values of the dataSource.  The test will compare an Integer to a String and then the
        other way around. In both cases (1) and (3) should be selected.  Postback on the
        second select doesn't work correctly because the array type is Integer.
        <hr>
        <netui:form action="postback">
            <netui:select dataSource="pageFlow.selectVals" optionsDataSource="${pageFlow.options}" multiple="true" size="3"></netui:select><br><br>
            <netui:select dataSource="pageFlow.selectValsTwo" optionsDataSource="${pageFlow.optionsTwo}" multiple="true" size="3"></netui:select><br>
            <netui:button type="submit" value="Submit"></netui:button>
        </netui:form>
        
    </body>
</netui:html>

  
