<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<frameset rows="20%,40%,40%">

    <frame src="JspTest4a.jsp" name="topFrame">

    <frameset cols="50%,50%">
        <frame src="jpf1/Jpf1.jpf?jpfScopeID=scopeA" name="frameA">
        <frame src="jpf2/Jpf2.jpf?jpfScopeID=scopeA" name="frameB">
    </frameset>

    <frameset cols="50%,50%">
        <frame src="jpf1/Jpf1.jpf?jpfScopeID=scopeA" name="frameC">
        <frame src="jpf2/Jpf2.jpf?jpfScopeID=scopeA" name="frameD">
    </frameset>

</frameset>
