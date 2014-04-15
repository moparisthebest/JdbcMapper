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
package org.apache.beehive.netui.test.controls.container;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.ResourceContext;
import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.beehive.controls.api.context.ControlThreadContext;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.controls.runtime.servlet.ServletBeanContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponseWrapper;

/**
 * This is a test control that intends to grow up beyond what
 * it currently does.  For the moment, it is simply a copy of the pflifecycle version.
 */
@ControlImplementation(isTransient=true)
public class ContainerControlImpl
    implements ContainerControl {

    private boolean _onReleaseCalled = true;

    @Context
    private ControlBeanContext _context;

    @Context
    private ResourceContext _resourceContext;

    @Context
    private ServletContext _sc;

    @Context
    private ServletRequest _sreq;

    @Context
    private ServletResponse _sresp;

    public String echo(String value) {
        return "Echo: '" + value + "'";
    }

    public String getInfo() {
        return "Context: " + (_sc != null) +
               ", Response: " + (_sresp != null) +
               ", Request: " + (_sreq != null);
    }

    @EventHandler(field = "_context", eventSet = ControlBeanContext.LifeCycle.class, eventName = "onCreate")
    public void onCreate() {
        checkContainerContext();
    }

    @EventHandler(field = "_resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onAcquire")
    public void onAquire() {
        checkContainerContext();

        if (!_onReleaseCalled)
            throw new IllegalStateException("onAcquire called without having called on Release on the previous request!");

        _onReleaseCalled = false;
    }

    @EventHandler(field = "_resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onRelease")
    public void onRelease() {
        checkContainerContext();
        _onReleaseCalled = true;
    }

    private void checkContainerContext() {
        ControlContainerContext ccc = ControlThreadContext.getContext();
        if (ccc == null)
            throw new IllegalStateException("Control could not find a valid ControlContainerContext!");
        if (!(ccc instanceof ServletBeanContext))
            throw new IllegalStateException("Control container context is not a ServletBeanContext");

        ServletBeanContext servletBeanContext = (ServletBeanContext)ccc;
        if (servletBeanContext.getServletRequest() == null)
            throw new IllegalStateException("ServletBeanContext could not provide a valid ServletRequest!");

        boolean diff = (_sreq != servletBeanContext.getServletRequest());
        if (diff && _sreq instanceof ServletRequestWrapper)
            diff = (_sreq == ((ServletRequestWrapper) _sreq).getRequest());
        if (diff)
            throw new IllegalStateException("Wired Up ServletRequest doesn't match ServletBeanContext request");

        if (servletBeanContext.getServletResponse() == null)
            throw new IllegalStateException("ServletBeanContext could not provide a valid ServletResponse!");

        diff = (_sresp != servletBeanContext.getServletResponse());
        if (diff && _sresp instanceof ServletResponseWrapper)
            diff = (_sresp == ((ServletResponseWrapper) _sresp).getResponse());
        if (diff)
            throw new IllegalStateException("Wired Up ServletResponse doesn't match ServletBeanContext response");

        if(servletBeanContext.getServletContext() == null)
            throw new IllegalStateException("ServletBeanContext could not provide a valid ServletContext!");
        if (_sc == null)
            throw new IllegalStateException("The ServletContext doesn't exist");
    }
}
