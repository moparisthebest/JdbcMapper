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
 * This is the second level interface for the inheritence test hiearachy.  It extends another
 * ControlInterface and also adds new operations/events via an extended interface.
 */
@ControlInterface
public interface Intf2 extends Intf1, AddIn {
    public int intf2Operation1();

    public int intf2Operation2();

    /**
     * Declare a PropertySet for this interface
     */
    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
    public @interface Intf2Props {
        public int intf2Prop1() default 1;

        public int intf2Prop2() default 2;
    }

    @EventSet
    public interface Intf2Events extends Intf1Events, AddInEvents {
        public void intf2Event1();

        public void intf2Event2();
    }

    /**
     * Declare an EventSet that is new and unique to this interface
     */
    @EventSet(unicast = true)
    public interface Intf2NewEvents extends AddInEvents {
        public void intf2NewEvent1();

        public void intf2NewEvent2();
    }
}
