<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>Action Interceptors</h3>

        <netui:anchor href="interceptedBeforeAction/Controller.jpf">
            test simple-action-interceptor running before all actions in a page flow
        </netui:anchor>
        <br/>
        <netui:anchor href="interceptedAfterAction/Controller.jpf">
            test simple-action-interceptor running after all actions in a page flow
        </netui:anchor>
        <br/>
        <netui:anchor href="someFlow/Controller.jpf">
            test simple-action-interceptor running after a specific action
        </netui:anchor>
        <br/>
        <netui:anchor href="globalInterceptors/Controller.jpf">
            test global (all page flow) interceptors, as well as custom interceptor properties
        </netui:anchor>
        <br/>
        <netui:anchor href="interruptChain/Controller.jpf">
            test interrupting the interceptor chain
        </netui:anchor>
        <br/>
        <netui:anchor href="overrideForwards/Controller.jpf">
            test various ways of overriding the destination of an intercepted action
        </netui:anchor>
        <br/>
    </netui:body>
</netui:html>

  

