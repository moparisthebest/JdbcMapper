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

package org.apache.beehive.controls.test.controls.inherit;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.events.Client;

/**
 * This is the first level interface for the inheritence test hiearachy.  It extends an interface
 * that is not a ControlInterface, to ensure that it is possible to derive a Control from an
 * existing interface.
 */
@ControlImplementation
public class Intf1Impl extends RootImpl
        implements Intf1, java.io.Serializable {
    @Context
    ControlBeanContext context;
    @Client
    Intf1Events intfEvents;
    @Client
    Intf1NewEvents intfNewEvents;

    @Control
    Hello _h1;

    public int intf1Operation1() {
        intfEvents.rootEvent1();
        intfEvents.intf1Event1();
        intfNewEvents.intf1NewEvent1();

        return context.getControlPropertySet(Intf1Props.class).intf1Prop1();
    }

    public int intf1Operation2() {
        intfEvents.rootEvent2();
        intfEvents.intf1Event2();
        intfNewEvents.intf1NewEvent2();

        return context.getControlPropertySet(Intf1Props.class).intf1Prop2();
    }
}
