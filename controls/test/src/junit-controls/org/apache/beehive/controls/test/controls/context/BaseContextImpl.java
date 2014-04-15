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

package org.apache.beehive.controls.test.controls.context;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.ControlBeanContext.LifeCycle;
import org.apache.beehive.controls.api.context.ResourceContext;
import org.apache.beehive.controls.api.context.ResourceContext.ResourceEvents;
import org.apache.beehive.controls.api.events.EventHandler;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

@ControlImplementation
public class BaseContextImpl implements BaseContext, java.io.Serializable {
    @Context
    private ControlBeanContext context;
    @Context
    private ResourceContext resourceContext;

    private transient LinkedList<String> _events;

    public String hello(String name) {
        _events.add("BaseContextImpl.hello " + name);
        return "Hello, " + name;
    }

    @EventHandler(field = "context", eventSet = LifeCycle.class, eventName = "onCreate")
    public void onCreate() {
        _events = new LinkedList<String>();
        _events.add("BaseContextImpl.onCreate");
    }

    @EventHandler(field = "resourceContext", eventSet = ResourceEvents.class, eventName = "onAcquire")
        /* package */ void onAcquire() {
        _events.add("BaseContextImpl.onAcquire");
    }

    @EventHandler(field = "resourceContext", eventSet = ResourceEvents.class, eventName = "onRelease")
    protected void onRelease() {
        _events.add("BaseContextImpl.onRelease");
    }

    //
    // Implement the serialization writeObject method.  By design, contextual services should
    // never be serialized, and this is done by making sure that they are set to null prior
    // to impl instance serialization.  This implementation of writeObject just verifies this,
    // then uses the default algorithm
    //
    public void writeObject(ObjectOutputStream oos)
            throws IOException, ClassNotFoundException {
        if (context != null || resourceContext != null)
            throw new RuntimeException("Contextual service(s) not reset prior to serialization");

        oos.defaultWriteObject();
    }

    public String[] getEventLog() {
        String[] ret = new String[1];
        return _events.toArray(ret);
    }
}
