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
package org.apache.beehive.netui.test.controls.pflifecycle;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.ResourceContext;
import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.beehive.controls.api.context.ControlThreadContext;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.controls.runtime.servlet.ServletBeanContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 */
@ControlImplementation(isTransient=true)
public class PfControlLifecycleImpl
    implements PfControlLifecycle {

    private static final Log LOG = LogFactory.getLog(PfControlLifecycleImpl.class);

    private boolean _onReleaseCalled = true;

    @Context
    private ControlBeanContext _context;

    @Context
    private ResourceContext _resourceContext;

    public String echo(String value) {
	//System.err.println("Echo:" + value);
        return "Echo: '" + value + "'";
    }

    @EventHandler(field = "_context", eventSet = ControlBeanContext.LifeCycle.class, eventName = "onCreate")
    public void onCreate() {
	//System.err.println("PfControlLifecycleImpl Created");
        LOG.info("bean context event -- onCreate");
        checkContainerContext();
    }

    @EventHandler(field = "_resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onAcquire")
    public void onAquire() {
	//System.err.println("OnAcquire");
        LOG.info("bean context event -- onAcquire");
        checkContainerContext();

        if(!_onReleaseCalled) 
            throw new IllegalStateException("onAcquire called without having called on Release on the previous request!");

        _onReleaseCalled = false;
    }

    @EventHandler(field = "_resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onRelease")
    public void onRelease() {
	//System.err.println("OnRelease");
        LOG.info("bean context event -- onRelease");
        checkContainerContext();
        _onReleaseCalled = true;
    }

    private void checkContainerContext() {
        ControlContainerContext ccc = ControlThreadContext.getContext();
        LOG.info("control container context: " + (ccc != null ? ccc.hashCode() : "is null"));

        if(ccc == null)
            throw new IllegalStateException("Control could not find a valid ControlContainerContext!");

        if(!(ccc instanceof ServletBeanContext))
            throw new IllegalStateException("Control container context is not a ServletBeanContext");

        ServletBeanContext servletBeanContext = (ServletBeanContext)ccc;
        if(servletBeanContext.getServletRequest() == null)
            throw new IllegalStateException("ServletBeanContext could not provide a valid ServletRequest!");

        if(servletBeanContext.getServletResponse() == null)
            throw new IllegalStateException("ServletBeanContext could not provide a valid ServletResponse!");

        if(servletBeanContext.getServletContext() == null)
            throw new IllegalStateException("ServletBeanContext could not provide a valid ServletContext!");
    }
}
