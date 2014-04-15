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

package org.apache.beehive.controls.test.controls.property;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.events.Client;
import org.apache.beehive.controls.api.events.EventHandler;

import java.beans.PropertyChangeEvent;
import java.lang.annotation.Annotation;

@ControlImplementation
public class NestPropsImpl implements NestProps, java.io.Serializable {
    static final long serialVersionUID = 1L;

    @Context
    ControlBeanContext context;

    @Control
    @Props.SimpleProps(simpleString = "A field annotation value")
    Props propControl;

    @Control
    @PropsExtension.ArrayProps(arrayString = {"One", "Two", "Three"})
    private PropsExtension propExtControl;

    @Client
    PropertyEvents client;

    /**
     * Provides a simple test API to externally query the PropertySet values on this
     * control.
     */
    public Annotation getControlPropertySet(Class propertySet) {
        return context.getControlPropertySet(propertySet);
    }

    /**
     * Provides a simple test API to externally query the PropertySet values on a
     * nested control.
     */
    public Annotation getNestedPropertySet(Class propertySet) {
        return propControl.getControlPropertySet(propertySet);
    }

    public Annotation getExtensionControlPropertySet(Class propertySet) {
        return propExtControl.getControlPropertySet(propertySet);
    }

    //
    // Expose PropertyEvents from three potential sources: local properties, or from either of
    // the two nested controls
    //
    @EventHandler(field = "context", eventSet = ControlBeanContext.LifeCycle.class,
                  eventName = "onPropertyChange")
    public void onContextChange(PropertyChangeEvent pce) {
        client.onChange(pce);
    }

    @EventHandler(field = "propControl", eventSet = Props.PropertyEvents.class,
                  eventName = "onChange")
    public void onPropsChange(PropertyChangeEvent pce) {
        client.onChange(pce);
    }

    @EventHandler(field = "propExtControl", eventSet = Props.PropertyEvents.class,
                  eventName = "onChange")
    public void onExtPropsChange(PropertyChangeEvent pce) {
        client.onChange(pce);
    }
} 
