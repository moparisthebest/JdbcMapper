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
package org.apache.beehive.controls.runtime.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import org.apache.beehive.controls.api.context.ControlHandle;
import org.apache.beehive.controls.api.context.ControlThreadContext;
import org.apache.beehive.controls.api.context.ResourceContext;
import org.apache.beehive.controls.api.events.EventDispatcher;
import org.apache.beehive.controls.api.events.EventRef;

/**
 * The ControlContainerContext class provides a base class implementation for external containers
 * of ControlBeans.  It provides additional services, such as:
 *
 * - defines a contextual service provider for the ResourceManager interface
 * - defines a simplified contract for the external container to interact with resource
 *   management (beginContext/endContext)
 */
public class ControlContainerContext
       extends ControlBeanContext
       implements EventDispatcher, org.apache.beehive.controls.api.context.ControlContainerContext
{
    public ControlContainerContext()
    {
        super(null);
    }

    protected ControlContainerContext(BeanContextServicesFactory beanContextServicesFactory) {
        super(null, beanContextServicesFactory);
    }

    /**
     * Defines the beginning of a new control container execution context.
     */
    public void beginContext()
    {
        ControlThreadContext.beginContext(this);
    }

    /**
     * Ends the current control container execution context
     */
    public void endContext()
    {
        try
        {
            //
            // Release all resources associated with the current execution context.
            //
            releaseResources();
        }
        finally
        {
            ControlThreadContext.endContext(this);
        }
    }

    /**
     * Called by BeanContextSupport superclass during construction and deserialization to
     * initialize subclass transient state
     */
    public void initialize()
    {
        super.initialize();

        //
        // Register the ResourceContext provider on all new ControlContainerContext instances.
        //
        addService(org.apache.beehive.controls.api.context.ResourceContext.class,
                   ResourceContextImpl.getProvider());
    }

    /**
     * Adds a new managed ResourceContext to the ControlContainerContext.  This method
     * is used to register a resource context that has just acquired resources
     * @param resourceContext the ResourceContext service that has acquired resources
     * @param bean the acquiring ControlBean.  Unused by the base implementation, but
     *             available so subclassed containers can have access to the bean.
     */
    protected synchronized void addResourceContext(ResourceContext resourceContext, ControlBean bean)
    {
        if (!resourceContext.hasResources())
            _resourceContexts.push(resourceContext); 
    }

    /**
     * Removes a managed ResourceContext from the ControlContainerContext.  This method
     * is used to unregister a resource context that has already acquired resources
     * @param resourceContext the ResourceContext service to be removed
     * @param bean the acquiring ControlBean.  Unused by the base implementation, but
     *             available so subclassed containers can have access to the bean.
     */
    protected synchronized void removeResourceContext(ResourceContext resourceContext, ControlBean bean)
    {
        //
        // Ignore removal requests received within the context of global cleanup.  The
        // stack is already being popped, so these are just requests for resources that
        // already have in-flight removal taking place.
        //
        if (!_releasingAll && resourceContext.hasResources())
            _resourceContexts.remove(resourceContext);
    }

    /**
     * Releases all ResourceContexts associated with the current ControlContainerContext.  
     * This method is called by the associated container whenever all managed ResourceContexts 
     * that have acquired resources should release them.
     */
    protected synchronized void releaseResources()
    {
        // Set the local flag indicating global resource release is occuring
        _releasingAll = true;

        //
        // Iterate through the list of acquired ResourceContexts and release them
        //
        while (!_resourceContexts.empty())
        {
            ResourceContext resourceContext = _resourceContexts.pop();
            resourceContext.release();
        }

        // Clear the local flag indicating global resource release is occuring
        _releasingAll = false;
    }

    /**
     * Dispatch an operation or an event to a bean within this container bean context.
     * @param handle the control handle identifying the target bean
     * @param event the event to be invoked on the target bean
     * @param args the arguments to be passed to the target method invocation
     */
    public Object dispatchEvent(ControlHandle handle, EventRef event, Object [] args) 
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        ControlBean bean = getBean(handle.getControlID());
        if (bean == null)
            throw new IllegalArgumentException("Invalid bean ID: " + handle.getControlID());

        return bean.dispatchEvent(event, args);
    }

    /**
     * Returns a ControlHandle to the component containing the control.  This handle can be
     * used to dispatch events and operations to a control instance.  This method will return
     * null if the containing component does not support direct dispatch.
     *
     * @param bean the target control bean
     */
    public ControlHandle getControlHandle(org.apache.beehive.controls.api.bean.ControlBean bean)
    {
        //
        // The base implementation doesn't support dispatch.  Containers should override
        // and return a valid service handle that does component-specific dispatch.
        //
        return null;
    }

    /**
     * Returns true if this container guarantees single-threaded behaviour.  By default, top-level
     * containers are assumed to NOT guarantee this; specific container implementations (for example,
     * for EJB containers) should override this appropriately.
     */
    public boolean isSingleThreadedContainer()
    {
        return false;
    }

    boolean _releasingAll;
    Stack<ResourceContext> _resourceContexts = new Stack<ResourceContext>();
}