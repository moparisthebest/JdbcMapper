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

import java.rmi.RemoteException;

import org.apache.beehive.controls.api.context.ControlHandle;

/**
 * The RemoteEventDispatcher interface defines the method signature that a container supporting
 * the external dispatch of Control events would implement if events can be dispatched using RMI.
 */
public interface RemoteEventDispatcher
{
    /**
     * Dispatches a Control event to a target control.
     * @param target the target control
     * @param event the event to deliver to the control
     * @param args the parameters to the control event
     * @throws IllegalAccessException the underlying event method is not accessible due to
     *         access control.
     * @throws IllegalArgumentException the target is not valid, the event is not a valid event
     *         type for the requested target, or the argument types do not match the event
     *         signature.
     * @throws InvocationTargetException wraps any exception thrown by the underlying event
     *         handler.
     */ 
    public Object dispatchEvent(ControlHandle target, EventRef event, Object [] args)
        throws RemoteException;
}
