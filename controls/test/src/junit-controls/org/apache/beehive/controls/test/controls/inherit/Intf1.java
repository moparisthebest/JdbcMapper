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

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.events.EventSet;
import org.apache.beehive.controls.api.properties.PropertySet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is the first level interface for the inheritence test hiearachy.  It extends an interface
 * that is not a ControlInterface, to ensure that it is possible to derive a Control from an
 * existing interface.
 */
@ControlInterface
public interface Intf1 extends Root {
    public int intf1Operation1();

    public int intf1Operation2();

    /**
     * Declare a PropertySet for this interface
     */
    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
    public @interface Intf1Props {
        public int intf1Prop1() default 1;

        public int intf1Prop2() default 2;
    }

    /**
     * Declare an EventSet that extends one on the Root interface
     */
    @EventSet
    public interface Intf1Events extends RootEvents {
        public void intf1Event1();

        public void intf1Event2();
    }

    /**
     * Declare an EventSet that is new and unique to this interface
     */
    @EventSet(unicast = true)
    public interface Intf1NewEvents {
        public void intf1NewEvent1();

        public void intf1NewEvent2();
    }
}
