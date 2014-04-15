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

package org.apache.beehive.controls.test.controls.extension;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.bean.Extensible;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.ResourceContext;
import org.apache.beehive.controls.api.events.EventHandler;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * A control impl.
 * By implementing Extensible, this impl makes itself extensible
 */

@ControlImplementation
public class ExtensibleControlImpl implements ExtensibleControl, Extensible, Serializable {
    @Context
    ControlBeanContext context;

    @Context
    ResourceContext resourceContext;

    @EventHandler(field = "resourceContext",
                  eventSet = ResourceContext.ResourceEvents.class,
                  eventName = "onAcquire")
    public void onAcquire() {
        // System.err.println("ExtensibleControlImpl.onAcquire()");
    }

    @EventHandler(field = "resourceContext",
                  eventSet = ResourceContext.ResourceEvents.class,
                  eventName = "onRelease")
    public void onRelease() {
        // System.err.println("ExtensibleControlImpl.onRelease()");
    }

    public String hello() {
        return "Hello from super control";
    }

    public String getLayerByContext() {
        /**BUG: could not refer to Greeting directly*/
        WhereAbout whereabout = (WhereAbout) context.getControlPropertySet(WhereAbout.class);

        return whereabout.Layer();
    }

    public Object invoke(Method m, Object[] args) throws Throwable {
        return null;
    }

}
