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

import java.beans.beancontext.BeanContextServiceProvider;
import java.beans.beancontext.BeanContextServices;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

import org.apache.beehive.controls.api.context.ResourceContext;

/**
 * The ResourceContextImpl class provides an implementation of the ResourceContext service,
 * as well as a simple singleton provider that can be used to obtain new instances.
 */
public class ResourceContextImpl implements ResourceContext, InvokeListener
{
    /**
     * The ResourceContextProvider inner class acts as a single BeanContext service
     * provider for the ResourceContext service class. 
     */
    private static class ResourceContextProvider
        implements BeanContextServiceProvider {

        //
        // BeanContextServiceProvider.getService()
        //
        public Object getService(BeanContextServices bcs, Object requestor, Class serviceClass, Object serviceSelector)
        {
            //
            // There is an implied contract between ControlContainerContext and ControlBean
            // classes required to implement the resource management contract.  This cannot
            // be supported for any generic BeanContextChild class.
            //
            if (requestor instanceof ControlBean)
            {
                return new ResourceContextImpl((ControlContainerContext)bcs, (ControlBean)requestor);
            }

            return null;
        }

        //
        // BeanContextServiceProvider.releaseService()
        //
        public void releaseService(BeanContextServices bcs, Object requestor, Object service)
        {
            return; // Should not happen, service is never unregistered
        }

        //
        // BeanContextServiceProvider.getContextServiceSelectors()
        //
        public Iterator getCurrentServiceSelectors(BeanContextServices bcs, Class serviceClass)
        {
            return null;    // no selectors
        }
    }

    /**
     * A singleton instance of the ResourceContextProvider class is what will be registered
     * on all ControlContainerContext instances.  The provider can be a singleton because it is
     * completely stateless and thread-safe.
     */
    static private ResourceContextProvider _theProvider = new ResourceContextProvider();

    /**
     * Returns the ResourceContextProvider used to create new ResourceContext instances
     */
    static /* package */ ResourceContextProvider getProvider() { return _theProvider; }

    /**
     * Constructs a new ResourceContext service implementation to manage resources for
     * a target ControlBean within a specific ControlContainerContext
     */
    public ResourceContextImpl(ControlContainerContext containerContext, ControlBean bean)
    {
        _containerContext = containerContext;
        _bean = bean;

        //
        // Register to receive invocation notifications from the target bean
        //
        _bean.addInvokeListener(this);
    }

    /**
     * Implements the InvokeListener.preInvoke method.  This hook will be called before the
     * managed beans' operations are invoked
     */
    public void preInvoke(Method m, Object [] args)
    {
        if (!_hasAcquired)
            acquire();
    }

    /**
     * Implements the InvokeListener.postInvoke method.
     */
    public void postInvoke(Object retval, Throwable t) {}

    // ResourceContext.acquire()
    public void acquire() 
    { 
        if (_hasAcquired)
            return;

        // Deliver the onAcquire event to registered listeners
        for (ResourceEvents resourceListener : _listeners)
            resourceListener.onAcquire();

        // Register this ResourceContext with associated container context
        _containerContext.addResourceContext(this, _bean);

        // Set the flag to indicate resources have been acquired.
        _hasAcquired = true;
    }

    // ResourceContext.release()
    public void release() 
    { 
        if (!_hasAcquired)
            return;

        // Deliver the onRelease event to the registered listeners
        for (ResourceEvents resourceListener : _listeners)
            resourceListener.onRelease();

        // Unregister this ResourceContext with associated container context
        _containerContext.removeResourceContext(this, _bean);

        // Reset the flag to indicate resources have been released.
        _hasAcquired = false;
    }

    // ResourceContext.hasResources()
    public boolean hasResources() { return _hasAcquired; } 

    // ResourceContext.addResourceEventsListener
    public void addResourceEventsListener(ResourceEvents resourceListener)
    {
        _listeners.add(resourceListener);
    }

    // ResourceContext.removeResourceEventsListener
    public void removeResourceEventsListener(ResourceEvents resourceListener)
    {
        _listeners.remove(resourceListener);
    }

    private Vector<ResourceEvents> _listeners = new Vector<ResourceEvents>();
    private boolean _hasAcquired = false;
    private ControlContainerContext _containerContext;
    private ControlBean _bean;
}
