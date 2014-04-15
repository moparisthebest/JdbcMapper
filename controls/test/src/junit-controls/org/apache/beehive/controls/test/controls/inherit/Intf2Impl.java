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
import org.apache.beehive.controls.api.bean.Extensible;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.events.Client;

import java.lang.reflect.Method;

/**
 * This is the second level control implementation, that tests subclassing one ControlImplementation
 * from another. This implementation also adds support for the Extensible interface, to enable
 * ControlExtension subinterfaces to be declared.
 */
@ControlImplementation
public class Intf2Impl extends Intf1Impl
        implements Intf2, Extensible, java.io.Serializable {
    @Context
    ControlBeanContext context;
    @Client
    Intf2Events intfEvents;
    @Client
    Intf2NewEvents intfNewEvents;

    @Control
    Hello _h2;

    public int intf2Operation1() {
        super.intf1Operation1();

        intfEvents.addInEvent1();
        intfEvents.intf1Event1();
        intfEvents.intf2Event1();

        intfNewEvents.addInEvent1();
        intfNewEvents.intf2NewEvent1();

        return context.getControlPropertySet(Intf2Props.class).intf2Prop1();
    }

    public int intf2Operation2() {
        super.intf1Operation2();

        intfEvents.intf1Event1();
        intfEvents.addInEvent1();
        intfEvents.intf2Event1();

        intfNewEvents.addInEvent1();
        intfNewEvents.intf2NewEvent1();

        return context.getControlPropertySet(Intf2Props.class).intf2Prop2();
    }

    public void addInOperation1() {
        // Same as above
        intf2Operation1();
    }

    public void addInOperation2() {
        // Same as above
        intf2Operation2();
    }

    public Object invoke(Method method, Object [] args) {
        return null;
    }
}
