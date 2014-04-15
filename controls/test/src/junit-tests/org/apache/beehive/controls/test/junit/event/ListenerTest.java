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
import org.apache.beehive.controls.test.controls.event.Hello;
import org.apache.beehive.controls.test.controls.event.HelloBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;

/**
 * A Testcase that tests receiving events from a control with an inner class or
 * an instance of listener class.
 * All tests on controls instantiated declaratively are deactivated until this feature is supported.
 */
public class ListenerTest extends ControlTestCase {

    /**
     * A control that raises events
     */
    @Control
    private HelloBean _myHellobean;
    private boolean innerClassReceiveEvent = false;
    private Event2Listener event2listener;

    /*
     * 1. Instantiates a control bean programmatically;
     * 2. Registers two event listeners to the bean instance;
     * 3. Invokes the method on the bean instance that triggers the event1 and event2
     */
    public void testListenerReceiveEventsProgram() throws Exception {
        HelloBean hellobean = (HelloBean) java.beans.Beans.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.event.HelloBean");
        receiveEventsHelper(hellobean, false);
    }

    // todo: control handles not currently supported by the ControlTestContainer
//    public void testListenerReceiveEventsProgramUsingHandle()
//            throws Exception {
//        HelloBean hellobean = (HelloBean) java.beans.Beans.instantiate(
//                Thread.currentThread().getContextClassLoader(),
//                "org.apache.beehive.controls.test.controls.event.HelloBean");
//        receiveEventsHelper(hellobean, true);
//    }

    /*
     * 1. Instantiates a control bean by declaration;
     * 2. Registers two event listeners to the bean instance;
     * 3. Invokes the method on the bean instance that triggers the event1 and event2
     */
    public void testListenerReceiveEventsDeclare() throws Exception {
        receiveEventsHelper(_myHellobean, false);
    }

    // todo: control handles not currently supported by the ControlTestContainer
//    public void testListenerReceiveEventsDeclareUsingHandle() throws Exception {
//        receiveEventsHelper(_myHellobean, true);
//    }

    /*
     * Main body of test logic used by the various programmatic and declarative tests
     */
    private void receiveEventsHelper(HelloBean hellobean, boolean useHandle) throws Exception {

        assertNotNull(hellobean);

        // Register an inner class as listener for event1
        hellobean.addEventSet1Listener(
                new Hello.EventSet1() {
                    public void method1() {
                        innerClassReceiveEvent = true;
                    }
                });

        // Creates a listener for event2 and register it
        event2listener = new Event2Listener();
        hellobean.addEventSet2Listener(event2listener);

        // Invokes methods on myHelloBean to trigger the events
        if (!useHandle) {
            hellobean.triggerEvents();
        }
        else {
            hellobean.triggerEventsUsingHandle();
        }

        Thread.sleep(1000);

        assertTrue(innerClassReceiveEvent);
        assertNotNull(event2listener);
        assertEquals("0000", event2listener.getAllResult());
    }
}
