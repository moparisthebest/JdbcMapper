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

package org.apache.beehive.netui.test.controls.event;

import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.ControlHandle;
import org.apache.beehive.controls.api.events.Client;
import org.apache.beehive.controls.api.events.EventRef;

/**
 * A control implementation that raises events when the method is invoked
 */
@ControlImplementation
public class HelloImpl implements Hello, java.io.Serializable {
    @Client
    EventSet0 eventSet0;
    @Client
    EventSet1 eventSet1;
    @Client
    EventSet2 eventSet2;

    @Context
    ControlBeanContext beanContext;

    public void triggerEvents() {
        eventSet1.method1();
        eventSet2.method1();
        eventSet2.set2Method2();
        eventSet2.set2OverloadedMethod();
        eventSet2.set2OverloadedMethod(68);

    }

    public void triggerEventsUsingHandle() {
        ControlHandle handle = beanContext.getControlHandle();
        if (handle == null)
            throw new ControlException("No control handle for context:" + beanContext);

        Object [] emptyArgs = new Object []{};

        try {
            // Create an event ref using the method descriptor string format
            EventRef eventRef = new EventRef(EventSet1.class.getName() + ".method1()V");
            handle.sendEvent(eventRef, emptyArgs);

            // Create an event ref using Method reflection
            eventRef = new EventRef(EventSet2.class.getMethod("method1", new Class []{}));
            handle.sendEvent(eventRef, emptyArgs);

            // Create an event ref using string descriptor, the serialize/deserialize before use
            eventRef = new EventRef(EventSet2.class.getName() + ".set2Method2()I");
            eventRef = SerializeUtils.testSerialize(eventRef);
            handle.sendEvent(eventRef, emptyArgs);

            // Create an event ref using Method reflection, then serialize/deserialize before use
            eventRef = new EventRef(EventSet2.class.getMethod("set2OverloadedMethod",
                                                              new Class []{}));
            eventRef = SerializeUtils.testSerialize(eventRef);
            handle.sendEvent(eventRef, emptyArgs);

            // Create an event ref using string descriptor, where arg matching is required
            eventRef = new EventRef(EventSet2.class.getName() + ".set2OverloadedMethod(I)Z");
            handle.sendEvent(eventRef, new Object []{68});
        }
        catch (Exception e) {
            throw new ControlException("Event dispatch error", e);
        }
    }
}
