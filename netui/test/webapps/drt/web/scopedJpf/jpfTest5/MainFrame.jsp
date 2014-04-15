<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<frameset rows="20%,40%,40%">

    <frame src="JspTest5a.jsp" name="topFrame">

    <frameset cols="50%,50%">
        <frame src="jpf1/jpf1Begin.do?jpfScopeID=scopeA" name="frameA">
        <frame src="jpf2/jpf2Begin.do?jpfScopeID=scopeB" name="frameB">
    </frameset>

    <frameset cols="50%,50%">
        <frame src="jpf1/jpf1Begin.do?jpfScopeID=scopeC" name="frameC">
        <frame src="jpf2/jpf2Begin.do?jpfScopeID=scopeD" name="frameD">
    </frameset>

</frameset>
