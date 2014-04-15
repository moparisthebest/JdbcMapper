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

package org.apache.beehive.controls.test.controls.encoding;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.bean.Extensible;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.events.Client;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.controls.api.properties.PropertySet;

import java.beans.PropertyChangeEvent;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@ControlImplementation
public class PropsImpl implements Props, Extensible, java.io.Serializable {
    static final long serialVersionUID = 1L;

    @Context
    ControlBeanContext context;

    @Client
    PropertyEvents client;

    /**
     * Provides a simple test API to externally query the control instance PropertySet values
     * returned by ControlBeanContext APIs
     */
    public Annotation getControlPropertySet(Class propertySet) {
        return context.getControlPropertySet(propertySet);
    }

    /**
     * Set up a handler for context property change events, then expose them using the
     * EventSet declared on the public interface.
     */
    @EventHandler(field = "context", eventSet = ControlBeanContext.LifeCycle.class,
                  eventName = "onPropertyChange")
    public void onContextChange(PropertyChangeEvent pce) {
        client.onChange(pce);
    }

    /**
     * This implementation of Extensible.invoke allows the testing of annotations found
     * on JCX methods
     */
    public Object invoke(Method m, Object [] args) throws Throwable {
        if (!(args[0] instanceof Class) ||
                !(((Class) args[0]).isAnnotationPresent(PropertySet.class)))
            throw new IllegalArgumentException("Arg 0 must be an PropertySet interface!");

        return context.getMethodPropertySet(m, (Class) args[0]);
    }
}
