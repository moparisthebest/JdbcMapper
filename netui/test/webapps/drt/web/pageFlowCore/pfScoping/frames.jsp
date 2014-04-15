<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<frameset rows="40%,60%">

    <frameset cols="50%,50%">
        <frame src="a/FlowA.jpf?jpfScopeID=aa" name="frameA">
        <frame src="b/FlowB.jpf?jpfScopeID=bb" name="frameB">
    </frameset>

    <frame src="windows.jsp" name="windowsFrame">
    
</frameset>
