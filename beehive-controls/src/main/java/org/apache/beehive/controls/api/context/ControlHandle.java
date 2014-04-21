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

import org.apache.beehive.controls.api.events.EventRef;
import java.lang.reflect.InvocationTargetException;

/**
 * The ControlHandle interface defines a reference object to a control instance that enables
 * control events to be fired on the control.  Control container implementations will provide
 * implementation of this interface that use container-specific dispatch mechanisms to locate
 * the appropriate control container instance when events are fired.
 * 
 * Classes implementing the ControlHandle interface should also implement the <code>
 * java.io.Serializable</code> interface.  This will enable handles to be serialized /
 * deserialized as part of event queueing or routing.
 */
public interface ControlHandle
{
    /**
     * Returns the controlID of the target control referenced by this handle
     */
    public String getControlID();

    /**
     * Delivers the specified event to the target control referenced by this handle.
     */
    public Object sendEvent(EventRef event, Object [] args) 
        throws IllegalAccessException,IllegalArgumentException,InvocationTargetException;
}
