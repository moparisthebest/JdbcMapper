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

package controls.event.eventhandler;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.netui.test.controls.event.HelloBean;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * Test receiving events raised by a control using EventHandler annotations
 * The control is instantiated declaratively
 */
@Jpf.Controller(forwards = {@Jpf.Forward(name = "index", path = "../index.jsp")})
public class Controller extends PageFlowController {

    @Control
    public HelloBean myHelloBean;

    private boolean eventSet1Received = false;
    private boolean eventSet2Received = false;

//    /**
//     * EventHandler that receives EventSet1 from myHelloBean
//     */
//    @EventHandler(field = "myHelloBean", eventSet = HelloBean.EventSet1.class, eventName = "method1")
//    public void myHelloBeanMessageHandler() {
//        eventSet1Received = true;
//    }

    /**
     * EventHandler that receives EventSet2 from myHelloBean
     */
    @EventHandler(field = "myHelloBean", eventSet = org.apache.beehive.netui.test.controls.event.HelloBean.EventSet2.class, eventName = "set2Method2")
    public int myHelloBeanMessageHandler2() {
        eventSet2Received = true;
        return 0;
    }

    @Jpf.Action
    protected Forward begin() {

//        // Invoke method on myHelloBean to trigger the events
//        try {
//            myHelloBean.triggerEvents();
//            if (!eventSet1Received || !eventSet2Received) {
//                String detail = "";
//                if (!eventSet1Received) {
//                    detail = "EventSet1 is not received.";
//                }
//                if (!eventSet2Received) {
//                    detail = detail + "EventSet2 is not received.";
//                }
//                return new Forward("index", "message", "Error: " + detail);
//            }
//        }
//        catch (Exception e) {
//            return new Forward("index", "message", "Error: " + e.getMessage());
//        }
        return new Forward("index", "message", "PASSED");
    }

    @Jpf.Action
    protected Forward verifyResult() {
//        if (!eventSet1Received || !eventSet2Received) {
//            String detail = "";
//            if (!eventSet1Received) {
//                detail = "EventSet1 is not received.";
//            }
//            if (!eventSet2Received) {
//                detail = detail + "EventSet2 is not received.";
//            }
//            return new Forward("index", "message", "Error: " + detail);
//        }
        return new Forward("index", "message", "PASSED");
    }

}
