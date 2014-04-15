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

import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.api.events.EventSet;

/**
 * This is the third level interface for the inheritence test hiearachy.  It defines a
 * ControlExtension interface that extends the 2nd level ControlInterface
 */
@ControlExtension
public interface Ext1 extends Intf2 {
    @Intf1Props(intf1Prop1 = 1)
    public int [] ext1Operation1();

    @Intf2Props(intf2Prop2 = 2)
    public int [] ext1Operation2();

    @EventSet
    public interface Ext1Events extends Intf2Events {
        @Intf1Props(intf1Prop1 = 1)
        public void ext1Event1();

        @Intf2Props(intf2Prop2 = 2)
        public void ext1Event2();
    }

    /**
     * Declare an EventSet that is new and unique to this extension
     */
    @EventSet(unicast = true)
    public interface Ext1NewEvents {
        @Intf1Props(intf1Prop1 = 1)
        public void ext1NewEvent1();

        @Intf2Props(intf2Prop2 = 2)
        public void ext2NewEvent2();
    }
}
