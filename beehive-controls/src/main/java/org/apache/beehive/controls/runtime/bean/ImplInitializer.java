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

/**
 * The ImplInitializer class is an abstract base class that all generated Control
 * initalization classes will extend.  It provides common utilities and supporting code
 * for initialization, and has a shared package relationship with the base ControlBean
 * class providing access to internals not available in a more general context.
 */
abstract public class ImplInitializer
{
    /**
     * Initializes a new ControlImplementation instance associated with the specified bean.
     */
    public void initialize(ControlBean bean, Object target)
    {
        initServices(bean, target);
        initControls(bean, target);
        initEventProxies(bean, target);
    }

    /**
     * Initializes all contextual services required by the target implementation instance.
     * The default initializer implementation is a noop, but will be overridden by
     * generated subclasses that contain contextual services.
     */
    public void initServices(ControlBean bean, Object target) { };

    /**
     * Resets all contextual services on the target implementation instance to null.
     * The default initializer implementation is a noop, but will be overridden by
     * generated subclasses that contain contextual services.
     */
    public void resetServices(ControlBean bean, Object target) { };

    /**
     * Initializes all nested controls required by the target implementation instance.
     * The default initializer implementation is a noop, but will be overridden by
     * generated subclasses that contain nested controls
     */
    public void initControls(ControlBean bean, Object target) { };

    /**
     * Initializes all event proxies required by the target implementation instance.
     * The default initializer implementation is a noop, but will be overridden by
     * generated subclasses that contain event proxies
     */
    public void initEventProxies(ControlBean bean, Object target) { };


    /**
     * Returns the ControlBean event notifier for the specified eventSet
     */
    public Object getEventNotifier(ControlBean bean, Class eventSet)
    {
        return bean.getEventNotifier(eventSet);
    }
}
