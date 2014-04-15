/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Header:$
 */
package org.apache.beehive.netui.pageflow;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.config.ModuleConfig;

import javax.servlet.ServletContext;


/**
 * An event reporter, which will be notified of events like "page flow created", "action raised", etc.
 */
public abstract class PageFlowEventReporter
{
    private ServletContext _servletContext;
    
    protected PageFlowEventReporter( ServletContext servletContext )
    {
        _servletContext = servletContext;
    }
    
    /**
     * Event fired when an action is raised on a FlowController (a page flow or a shared flow).
     * 
     * @param requestContext the current request context.
     * @param flowController the FlowController on which the action is being raised (a
     *            {@link PageFlowController} or a {@link SharedFlowController}).
     * @param actionMapping the <code>org.apache.struts.action.ActionMapping</code> that represents the action being
     *            raised.  The name of the action, prefixed by a '/', can be found by calling <code>getPath</code>
     *            on this object.
     * @param form the form bean that was passed to the action, or <code>null</code> if there was none.
     */ 
    public abstract void actionRaised( RequestContext requestContext, FlowController flowController,
                                       ActionMapping actionMapping, ActionForm form );
    
    /**
     * Event fired when an action successfully completes on a FlowController (a page flow or a shared flow).
     * 
     * @param requestContext the current request context.
     * @param flowController the FlowController on which the action was raised (a
     *            {@link PageFlowController} or a {@link SharedFlowController}).
     * @param actionMapping the <code>org.apache.struts.action.ActionMapping</code> that represents the action that was
     *            raised.  The name of the action, prefixed by a '/', can be found by calling <code>getPath</code>
     *            on this object.
     * @param form the form bean that was passed to the action, or <code>null</code> if there was none.
     * @param result the ActionForward result returned from the action.
     * @param timeTakenMillis the length of time in milliseconds for the action to be run.
     */ 
    public abstract void actionSuccess( RequestContext requestContext, FlowController flowController,
                                        ActionMapping actionMapping, ActionForm form, ActionForward result,
                                        long timeTakenMillis );
    
    /**
     * Event fired when an exception is raised during processing of an action request.
     * 
     * @param requestContext the current request context.
     * @param ex the Throwable that was raised.
     * @param actionMapping the <code>org.apache.struts.action.ActionMapping</code> that represents the action that was
     *            raised.  The name of the action, prefixed by a '/', can be found by calling <code>getPath</code>
     *            on this object.  This parameter will be <code>null</code> if the request did not get to the point
     *            where an action was actuallly raised.
     * @param form the form bean that was passed to the action, or <code>null</code> if there was none.
     * @param flowController the FlowController associated with the action request.  This parameter will be
     *            <code>null</code> if the request did not get to the point where a FlowController could be created or
     *            looked up.
     * 
     * @see #beginActionRequest
     */ 
    public abstract void exceptionRaised( RequestContext requestContext, Throwable ex, ActionMapping actionMapping,
                                          ActionForm form, FlowController flowController );
    
    /**
     * Event fired when an exception is handled successfully during processing of an action request.
     * 
     * @param requestContext the current request context.
     * @param ex the Throwable that was raised.
     * @param actionMapping the <code>org.apache.struts.action.ActionMapping</code> that represents the action that was
     *            raised.  The name of the action, prefixed by a '/', can be found by calling <code>getPath</code>
     *            on this object.  This parameter will be <code>null</code> if the request did not get to the point
     *            where an action was actuallly raised.
     * @param form the form bean that was passed to the action, or <code>null</code> if there was none.
     * @param flowController the FlowController associated with the action request.  This parameter will be
     *            <code>null</code> if the request did not get to the point where a FlowController could be created or
     *            looked up.
     * @param result the ActionForward result returned from the exception handler.
     * @param timeTakenMillis the length of time in milliseconds for the exception to be handled.
     * 
     * @see #beginActionRequest
     */ 
    public abstract void exceptionHandled( RequestContext requestContext, Throwable ex, ActionMapping actionMapping,
                                           ActionForm form, FlowController flowController, ActionForward result,
                                           long timeTakenMillis );
    
    /**
     * 
     * Event fired when a FlowController (a page flow or a shared flow) is created.
     * 
     * @param requestContext the current request context.
     * @param flowController the FlowController (a {@link PageFlowController} or a {@link SharedFlowController})
     *            that was created.
     */ 
    public abstract void flowControllerCreated( RequestContext requestContext, FlowController flowController );
    
    /**
     * Event fired when a FlowController (a page flow or a shared flow) is "destroyed", i.e., removed from wherever it
     * is being stored.
     * 
     * @param flowController the FlowController (a {@link PageFlowController} or a {@link SharedFlowController})
     *            that is being destroyed.
     * @param storageLocation The storage location.  For session-scoped FlowControllers, this is a
     *            <code>javax.servlet.http.HttpSession</code>.
     */ 
    public abstract void flowControllerDestroyed( FlowController flowController, Object storageLocation );
    
    /**
     * Event fired at the beginning of an action request.  Note that this is called on all action requests, even those
     * that do not successfully run actions.
     * 
     * @param requestContext the current request context.
     */ 
    public abstract void beginActionRequest( RequestContext requestContext );
    
    /**
     * Event fired at the end of an action request.  Note that this is called on all action requests, even those
     * that do not successfully run actions.
     * 
     * @param requestContext the current request context.
     * @param timeTakenMillis the length of time in milliseconds for the action request to be processed.
     * 
     */ 
    public abstract void endActionRequest( RequestContext requestContext, long timeTakenMillis );
    
    /**
     * Event fired at the end of an action request.  Note that this is called on all action requests, even those
     * that do not successfully run actions.
     * 
     * @param requestContext the current request context.
     */ 
    public abstract void beginPageRequest( RequestContext requestContext );
    
    /**
     * Event fired at the end of a page request.
     * 
     * @param requestContext the current request context.
     * @param timeTakenMillis the length of time in milliseconds for the page request to be processed.
     */ 
    public abstract void endPageRequest( RequestContext requestContext, long timeTakenMillis );
    
    /**
     * Event fired when a page flow or shared flow is registered lazily (once per webapp deployment).
     * 
     * @param modulePath the module path, which is the "parent directory" for actions on the controller.
     * @param controllerClassName the name of the controller class.
     * @param moduleConfig the Struts ModuleConfig that corresponds to the controller.
     */ 
    public abstract void flowControllerRegistered( String modulePath, String controllerClassName,
                                                   ModuleConfig moduleConfig );
    
    protected ServletContext getServletContext()
    {
        return _servletContext;
    }
}
