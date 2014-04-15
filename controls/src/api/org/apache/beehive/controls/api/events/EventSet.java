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
package org.apache.beehive.controls.api.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The EventSet annotation type is used to mark an interface that defines a group of events
 * associated with a Java Control.  By convention, event interfaces are defined as inner 
 * classes on the Java Control public interface.  Each method defined within a
 * event interface indicates an event that can be delivered by the control.
 * <p>
 * Here is a simple example:
 * <code><pre>
 * public interface MyControl extends org.apache.beehive.controls.api.Control
 * {
 *     <sp>@EventSet
 *     public interface MyEvents
 *     {
 *         public void anEvent();
 *     }
 *
 *     ...
 * }
 * </pre></code>
 * This will declare an event interface named <code>MyEvents</code> that declares a single
 * event: <code>anEvent</code>
 * 
 * The declaration of an EventSet for a control also means that the associated Control
 * JavaBean will have listener registration/deregistration APIs.  The name of these
 * APIs will be <i>add/remove<EventSetName>Listener</i>, and the argument will be an
 * listener instance that implements the EventSet interface.
 * <p>
 * The above example would result in the following APIs on <code>MyControlBean</code>
 *
 * <code><pre>
 * public class MyControlBean implements MyControl
 * {
 *     ...
 *     public void addMyEventsListener(MyEvents listener) { ... }
 *     public void removeMyEventsListener(MyEvents listener) { ... }
 * </pre></code>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EventSet
{
    /**
     * Defines whether the events defined by the interface are unicast events.  A unicast
     * event set may have only a single listener registered to receive events for any
     * given bean instance.   Any attempt to register additional listeners will result in
     * a <code>java.util.TooManyListenersException</code> being thrown by the event
     * listener registration method.
     * <p>
     * If an event set provides multicast support (the default), then it may only declare
     * event methods that have a <code>void</code> return type.  Unicast event sets may
     * support event return values, that will be provided by the (single) registered
     * listener.
     */
    public boolean unicast() default false;
}
