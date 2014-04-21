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
package org.apache.beehive.controls.api.context;

import org.apache.beehive.controls.api.events.EventSet;

/**
 * The ResourceContext interface defines a basic contextual service for coordinating the 
 * resource utilization of a control implementation within a resource scope defined external 
 * to the control.   This contextual service that provides assistance to a Control in managing 
 * any external resources (connections, sesssions, etc) that may be relatively expensive to 
 * obtain and/or can only be held for a relatively short duration.  
 * <p>
 * A ResourceContext implementation may be provided by an external container of Controls, such 
 * as a servlet engine or EJB container, that coordinates the resource lifecycle of controls with 
 * the activities of the external container.    For example, the resource scope for a 
 * ResourceContext provider associated with the web tier might enable control resources to be 
 * used for the duration of a single http request;  for the EJB tier it might mean for the 
 * lifetime of the current EJB invocation or active transaction.
 * <p>
 * A control implementation participates in this resource management contract by declaring a 
 * ResourceContext instance annotated with the
 * <sp>@Context annotation, the standard service provider model of the Control runtime will
 * associate the control instance with a ResourceControl provider implementation that is
 * associated with the current execution context.  This is demonstrated by the following
 * code excerpt from a ControlImplementation class:
 * <p>
 * <pre><code>
 * <sp>@org.apache.beehive.controls.api.bean.ControlImplementation
 * public class MyControlImpl
 * {
 *     ...
 *     // Declare need for resource mgmt support via the ResourceContext service
 *     <sp>@org.apache.beehive.controls.api.context.Context
 *     ResourceContext resourceContext;
 *     ...
 * </code></pre>
 * <p>
 * Once the control has been associated with a ResourceContext provider, the provider will
 * deliver events to the Control Implementation instance according to the following basic
 * contract:
 * <p>
 * <ul>
 * <li>the ResourceContext provider notifies a control implementation when it should acquire its
 * resources using the onAcquire event.
 * <li>the ResourceContext provider notifies a control implementation when it should release its
 * resources using the onRelease event.
 * </ul>
 * <p>
 * The following code fragment shows how to receive resource events from within a Control
 * implementation:
 * <p>
 * <pre><code>
 * import org.apache.beehive.controls.api.events.EventHandler;
 * 
 * ...
 *
 * <sp>@EventHandler(field="resourceContext",
 *                eventSet=ResourceContext.ResourceEvents.class,
 *                eventName="onAcquire")
 * public void onAcquire() 
 * { 
 *      // code to obtain connections/sessions/...
 * }
 * 
 * <sp>@EventHandler(field="resourceContext", 
 *                eventSet=ResourceContext.ResourceEvents.class,
 *                eventName="onRelease")
 * public void onRelease() 
 * { 
 *      // code to release connections/sessions/...
 * }
 * </code></pre>
 * <p>
 * The onAcquire resource event is guaranteed to be delivered once before any operation declared 
 * on a public or extension interface associated with the control.  This event will be delivered
 * once, and only once, within a particular resource scope associated with the ResourceContext.
 *
 * If a control needs to utilize its resources in another context (such as in response to a 
 * PropertyChange notification), the ResourceContext also provides support for manually
 * acquiring and releasing resources.
 * 
 * @see org.apache.beehive.controls.api.context.ResourceContext.ResourceEvents
 * @see org.apache.beehive.controls.api.context.Context
 * @see org.apache.beehive.controls.api.events.EventHandler
 */
public interface ResourceContext
{
    /**
     * The acquire method allows a Control implementation to manually request acquisition.
     * This is useful in contexts where the control needs access to associated resources
     * from outside the scope of an operation.  If invoked when the control has not currently
     * acquired resources, the onAcquire event will be delivered to the control and it will 
     * be registered in the current resource scope as holding resources.  If the control has
     * previously acquired resources in the current resource scope, then calling acquire()
     * will have no effect.
     */
    public void acquire();

    /**
     * The release method allows a Control implement to manually release resources immediately,
     * instead of waiting until the end of the current resource scope.  If invoked when the
     * control has currently acquired resources, the onRelease event will be delivered immediately
     * and the control will no longer be in the list of controls holding resources in the current
     * resource scope.  If the control has not previously acquired resources, then calling
     * release() will have no effect.
     */
    public void release();

    /** 
     * The hasResources method returns true if the control has currently acquired resources,
     * false otherwise.
     */
    public boolean hasResources();

    /**
     * The ResourceEvents interface defines the resource events delivered by a ResourceContext
     * provider.
     */
    @EventSet
    public interface ResourceEvents
    {
        /** 
         * The onAcquire event will be delivered by a ResourceContext provider to the 
         * Control implementation <b>before</b> any operation on the control is invoked within 
         * the resource scope associated with the provider and its associated container.  This 
         * provides the opportunity for the implementation instance to obtain any resource it 
         * uses to provide its services.
         */
        public void onAcquire();

        /**
         * The onRelease event will be delivered by a ResourceContext provider to the 
         * Control implementation <b>immediately before</b> before the end of the resource 
         * scope associated with the provider and its associated container.  This provides 
         * the opportunity for the implementation instance to relinquish any resources it 
         * obtained during <i>onAcquire</i> event handling.
         */
        public void onRelease();
    }

    /**
     * Registers a listener that implements the ResourceEvents interface for the ResourceContext. 
     */
    public void addResourceEventsListener(ResourceEvents resourceListener);

    /**
     * Unregisters a listener that implements the ResourceEvents interface for the ResourceContext. 
     */
    public void removeResourceEventsListener(ResourceEvents resourceListener);
}
