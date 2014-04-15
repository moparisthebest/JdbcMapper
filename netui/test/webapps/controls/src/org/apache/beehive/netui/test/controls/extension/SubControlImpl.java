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

package org.apache.beehive.netui.test.controls.extension;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.events.Client;

/**
 * A control impl of a SubControl and extending ExtensibleControlImpl.
 * It accesses the propertySets inherited from ExtensibleControl, and extended by
 * its own interface.
 */
@ControlImplementation
public class SubControlImpl extends ExtensibleControlImpl
        implements SubControl, java.io.Serializable {
    @Context
    ControlBeanContext context;

    @Client
    SuperClassEvent superevent;
    @Client
    SubClassEvent subevent;

    public String hello2() {
        return "Hello from _subcontrol";
    }

    /*Accessing the propertySet inherited from ExtensibleControl*/
    public String accessInheritedProperty() {
        /**Bug: could not refer to WhereAbout directly*/
        WhereAbout where = context.getControlPropertySet(WhereAbout.class);

        return where.Position();
    }

    public String getAnnotatedInheritedPropertyByContext() {
        Origin origin = context.getControlPropertySet(Origin.class);
        return origin.Birthplace();
    }

    public String getExtendedPropertyByContext() {

        NewProperty newproperty = context.getControlPropertySet(NewProperty.class);

        return newproperty.Message();
    }

    public int invokeInheritedEventFromSubControl() {

        superevent.method1();
        return 0;
    }

    public int invokeExtendedEventFromSubControl() {

        subevent.method1();
        return 0;
    }

}
