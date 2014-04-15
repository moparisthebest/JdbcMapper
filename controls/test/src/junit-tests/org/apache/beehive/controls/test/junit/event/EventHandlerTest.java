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

package org.apache.beehive.controls.test.junit.event;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.controls.test.controls.event.HelloBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;

/**
 * A TestCase that tests receiving events from a control by @EventHandler
 * All tests on controls instantiated declaratively are deactivated until this feature is supported.
 */
public class EventHandlerTest extends ControlTestCase {
    private boolean event1Received = false;
    private boolean event2Received = false;

    /**
     * A control that raises events
     */
    @Control
    private HelloBean myHellobean;

    /**
     * EventHandler that receives EventSet1 from myHelloBean
     */
    @EventHandler(field = "myHellobean",
                  eventSet = HelloBean.EventSet1.class,
                  eventName = "method1")
    public void myHelloBeanMessageHandler() {
        // Invoked with event is received
        event1Received = true;
    }

    /**
     * EventHandler that receives EventSet2 from myHelloBean
     */
    @EventHandler(field = "myHellobean",
                  eventSet = HelloBean.EventSet2.class,
                  eventName = "set2Method2")
    public int myHelloBeanMessageHandler2() {
        // Invoked when event is received
        event2Received = true;
        return 0;
    }

    /**
     * Triggers the events and waits for the arrivals of the events
     */
    public void testHandlerReceiveEvents() throws Exception {
        assertNotNull(myHellobean);

        // Invokes the method on the control while will trigger the events
        myHellobean.triggerEvents();

        /*Wait for the events*/
        Thread.sleep(1000);

        /*Verified result*/
        assertTrue(event1Received);
        assertTrue(event2Received);
    }
}
