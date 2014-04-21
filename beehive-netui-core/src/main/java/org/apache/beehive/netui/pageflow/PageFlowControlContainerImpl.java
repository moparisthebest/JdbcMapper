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

import org.apache.beehive.controls.runtime.servlet.ServletBeanContext;
import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.beehive.netui.pageflow.internal.AdapterManager;
import org.apache.beehive.netui.util.logging.Logger;

import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class provide a set of method that deal with the ControlContainerContext that is scoped to
 * the FlowController.
 */
public class PageFlowControlContainerImpl
    implements PageFlowControlContainer, Serializable
{
    private static final Logger _log = Logger.getInstance( PageFlowControlContainerImpl.class );

    private ControlContainerContext _sharedContext;
    private ReentrantLock _sharedLock;

    public ControlContainerContext getControlContainerContext(PageFlowManagedObject pfmo)
    {
        // Based upon the type of controller find and return the appropriate ControlContainerContext (CCC)
        // For PageFlowController and FacesBackingBean the CCC is obtained from the object directly
        // For SharedFlowController there is a shared object maintained here
        if (pfmo instanceof SharedFlowController)
            return _sharedContext;
        if (pfmo instanceof PageFlowController)
            return ((PageFlowController) pfmo)._beanContext;
        if (pfmo instanceof FacesBackingBean)
            return ((FacesBackingBean) pfmo)._beanContext;

        // Found an unknown type of PageFlowManagedObject.  Assert this, log it, and then return null
        _log.error("Unknown FlowController ControlBeanContenxt:" + pfmo.getClass().getName());
        assert(false) : "Unknown FlowController ControlBeanContenxt:" + pfmo.getClass().getName();
        return null;
    }

    public void beginContextOnPageFlow(PageFlowManagedObject pfmo, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
    {
        // get the lock on the shared flow if the shared flow context exists.  This is the
        // reason that you must always call endContextOnPageFlow if you call this method.
        if (_sharedContext != null) {
            assert(_sharedLock != null) : "Forgot to create the shared lock";
            _sharedLock.lock();

            if (_sharedContext instanceof ServletBeanContext) {
                ((ServletBeanContext) _sharedContext).beginContext(servletContext,request,response);
            }
            else {
                _sharedContext.beginContext();
            }
        }

        // if the page flow isn't a shared flow then we must begin context on that also
        if (pfmo instanceof PageFlowController || pfmo instanceof FacesBackingBean) {

            ControlContainerContext cbc = getControlContainerContext(pfmo);
            if (cbc != null) {
                if (cbc instanceof ServletBeanContext) {
                    ((ServletBeanContext) cbc).beginContext(servletContext,request,response);
                }
                else {
                    cbc.beginContext();
                }
            }
        }
    }

    public void createAndBeginControlBeanContext(PageFlowManagedObject pfmo, HttpServletRequest request, HttpServletResponse response,
                                                 ServletContext servletContext)
    {
        if (pfmo instanceof SharedFlowController) {
            if (_sharedContext == null) {
                _sharedContext = (ControlContainerContext)
                    AdapterManager.getServletContainerAdapter(servletContext).createControlBeanContext( request, response );
                _sharedLock = new ReentrantLock();
            }
        }
        else if (pfmo instanceof PageFlowController) {
            if (((PageFlowController) pfmo)._beanContext == null) {
                ((PageFlowController) pfmo)._beanContext = (ControlContainerContext)
                    AdapterManager.getServletContainerAdapter(servletContext).createControlBeanContext( request, response );
            }
        }
        else if (pfmo instanceof FacesBackingBean) {
            if (((FacesBackingBean) pfmo)._beanContext == null) {
                ((FacesBackingBean) pfmo)._beanContext = (ControlContainerContext)
                    AdapterManager.getServletContainerAdapter(servletContext).createControlBeanContext( request, response );
            }
        }
        else {
            _log.error("Unknown FlowController ControlBeanContenxt:" + pfmo.getClass().getName());
            assert(false) : "Unknown FlowController ControlBeanContenxt:" + pfmo.getClass().getName();
            return;
        }
        beginContextOnPageFlow(pfmo,request,response,servletContext);
    }

    public void endContextOnPageFlow(PageFlowManagedObject flowController)
    {
        // You must reverse the order of the begin because the low level stuff uses a stack for
        // this.  We also do this with a try/finally so that we make sure to free up the lock.
        try {
            if (flowController instanceof PageFlowController || flowController instanceof FacesBackingBean) {

                ControlContainerContext cbc = getControlContainerContext(flowController);
                if (cbc != null) {
                    cbc.endContext();
                }
            }
        }
        finally {
            if (_sharedContext != null) {
                assert(_sharedLock != null) : "The sharedLock was not allocated";
                try {
                    _sharedContext.endContext();
                }
                finally {
                    _sharedLock.unlock();
                }
            }
        }
    }
}
