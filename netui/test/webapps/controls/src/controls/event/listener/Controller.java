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

package controls.event.listener;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.test.controls.event.Event2Listener;
import org.apache.beehive.netui.test.controls.event.Hello;
import org.apache.beehive.netui.test.controls.event.HelloBean;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * Test receiving events raised by a control using a registered listener
 * The control is instantiated declaratively
 */
@Jpf.Controller(forwards = {@Jpf.Forward(name = "index", path = "../index.jsp")})
public class Controller extends PageFlowController {

    @Control
    public HelloBean hello;

    private boolean innerClassReceiveEvent = false;
    private Event2Listener event2listener;

    @Jpf.Action()
    protected Forward begin() {

        try {
            // Register an inner class as listener for event1
            hello.addEventSet1Listener(
                    new Hello.EventSet1() {
                        public void method1() {
                            innerClassReceiveEvent = true;
                        }
                    });

            /* Creates a listener for event2 and register it*/
            event2listener = new Event2Listener();
            hello.addEventSet2Listener(event2listener);

            /* Invokes methods on myHelloBean to trigger the events*/
            hello.triggerEvents();
            Thread.sleep(1000);
        }
        catch (Exception e) {
            return new Forward("index", "message", "Error: " + e.getMessage());
        }

        if (!innerClassReceiveEvent) {
            return new Forward("index", "message", "Error: Innner Class didn't receive event.");
        }

        if (event2listener == null) {
            return new Forward("index", "message", "Error: Event2Listerner is NULL");
        }

        String listenerResult = event2listener.getAllResult();
        if (!listenerResult.equals("0000")) {
            return new Forward("index", "message", "Error: Listener Result:" + listenerResult);
        }

        return new Forward("index", "message", "PASSED");
    }
}
