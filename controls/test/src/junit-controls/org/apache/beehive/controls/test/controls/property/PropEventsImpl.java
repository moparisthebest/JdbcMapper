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

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.events.Client;
import org.apache.beehive.controls.api.events.EventHandler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

@ControlImplementation
public class PropEventsImpl implements PropEvents, java.io.Serializable {
    static final long serialVersionUID = 1L;

    @Context
    ControlBeanContext context;

    @Client
    ImplPropertyChange changeNotifier;

    @Client
    ImplVetoableChange vetoNotifier;

    //
    // Receive context propertyChange events and echo them as external callbacks
    //
    @EventHandler(field = "context", eventSet = ControlBeanContext.LifeCycle.class,
                  eventName = "onPropertyChange")
    public void onPropertyChange(PropertyChangeEvent pce) {
        changeNotifier.propertyChange(pce);
    }

    //
    // Receive context vetoableChange events and echo them as external callbacks
    //
    @EventHandler(field = "context", eventSet = ControlBeanContext.LifeCycle.class,
                  eventName = "onVetoableChange")
    public void onVetoableChange(PropertyChangeEvent pce) throws PropertyVetoException {
        vetoNotifier.vetoableChange(pce);
    }
}
