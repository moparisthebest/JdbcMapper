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
package org.apache.beehive.controls.test.controls.composition;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.events.Client;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.controls.api.properties.BeanPropertyMap;

/**
 * A control implementation to test control composition.  Makes two instances of nested control be declaration.
 */
@ControlImplementation
public class OuterControlImpl
    implements OuterControl, java.io.Serializable {

    static final long serialVersionUID = 1L;
    static final String EVENT_RECEIVED = "Event Received";

    private String innerControlEventHandlerWakeUp = "";
    private String innerControlEventHandlerReadMessage = "";
    private String innerControlEventHandlerReport = "";
    private String innerControlEventHandlerShopping = "";
    private String innerControlEventHandlerDoStuff = "";

    private boolean innerClassWakeUp = false;
    private boolean innerClassReadMessage = false;
    private boolean innerClassReport = false;
    private boolean innerClassShopping = false;
    private boolean innerClassDoStuff = false;

    private boolean innerControlInnerClassWakeUp = false;
    private boolean innerControlInnerClassReadMessage = false;
    private boolean innerControlInnerClassReport = false;
    private boolean innerControlInnerClassShopping = false;
    private boolean innerControlInnerClassDoStuff = false;

    @Client
    transient OuterEvents outerEvents;

    /*Instantiates a nested control without reconfiguring the property*/
    @Control
    InnerControlBean innerControl;

    @Control
    @InnerControl.Identity(job = "farmer")
    InnerControlBean innerControl2;

    //
    // Define various event handlers for the nested controls
    //
    @EventHandler(field = "innerControl", eventSet = InnerControl.Activity.class, eventName = "wakeup")
    public void innerControlwakeup() {
        innerControlEventHandlerWakeUp = EVENT_RECEIVED;
    }

    @EventHandler(field = "innerControl", eventSet = InnerControl.Activity.class, eventName = "readMessage")
    public int innerControlreadMessage(String message) {
        innerControlEventHandlerReadMessage = EVENT_RECEIVED;
        return 0;
    }

    @EventHandler(field = "innerControl", eventSet = InnerControl.Activity.class, eventName = "report")
    public String innerControlreport() {
        innerControlEventHandlerReport = EVENT_RECEIVED;
        return "a report";
    }

    @EventHandler(field = "innerControl", eventSet = InnerControl.Action.class, eventName = "shopping")
    public Object [] innerControlshopping(double credit) {
        innerControlEventHandlerShopping = EVENT_RECEIVED;
        return null;
    }

    @EventHandler(field = "innerControl", eventSet = InnerControl.Action.class, eventName = "doStuff")
    public void innerControldoStuff(String vakue) {
        innerControlEventHandlerDoStuff = EVENT_RECEIVED;
    }

    @EventHandler(field = "innerControl2", eventSet = InnerControl.Activity.class, eventName = "wakeup")
    public void innerControl2wakeup() {
    }

    @EventHandler(field = "innerControl2", eventSet = InnerControl.Activity.class, eventName = "readMessage")
    public int innerControl2readMessage(String message) {
        return 0;
    }

    @EventHandler(field = "innerControl2", eventSet = InnerControl.Activity.class, eventName = "report")
    public String innerControl2report() {
        return "a report";
    }

    @EventHandler(field = "innerControl2", eventSet = InnerControl.Action.class, eventName = "shopping")
    public Object [] innerControl2shopping(double credit) {
        return null;
    }

    @EventHandler(field = "innerControl2", eventSet = InnerControl.Action.class, eventName = "doStuff")
    public void innerControl2doStuff(String vakue) {
    }

    public void fireOuterEvents(String message) {
        outerEvents.report(message);
    }

    public InnerControlBean getDeclaredNestedControl() {
        return innerControl;
    }

    public InnerControlBean getDeclaredNestedControl2() {
        return innerControl2;
    }

    public InnerControlBean instantiateNestedControlProgrammatically() {
        try {
            InnerControlBean inner =
                (InnerControlBean) java.beans.Beans.instantiate(Thread.currentThread().getContextClassLoader(),
                    "org.apache.beehive.controls.test.controls.composition.InnerControlBean");
            return inner;
        }
        catch (Exception e) {
            return null;
        }
    }

    public InnerControlBean instantiateNestedControlWithProperty() {
        try {
            BeanPropertyMap props = new BeanPropertyMap(InnerControl.Identity.class);
            props.setProperty(InnerControlBean.NameKey, "ken");
            props.setProperty(InnerControlBean.JobKey, "engineer");
            props.setProperty(InnerControlBean.RankKey, new Integer(2));
            InnerControlBean inner = (InnerControlBean) org.apache.beehive.controls.api.bean.Controls.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.composition.InnerControlBean", props);
            return inner;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to instantiate nested control", e);
        }
    }

    public String testActivityWakeup() {

        String result = "";
        if (innerControl == null)
            result = "inner control is NULL";
        else {
            innerControl.fireEvent("Activity", "wakeup");
            /*Wait for the events*/
            try {
                Thread.sleep(1000);
                if (innerControlEventHandlerWakeUp.equals(EVENT_RECEIVED))
                    result = "0";
                else
                    result = "Acivity.wakeup not received by EventHandler";
            }
            catch (Exception e) {
                result = "Thread sleep interrupted." + e.toString();
            }
        }
        return result;
    }

    public String testActivityReadMessage() {

        String result = "";
        if (innerControl == null)
            result = "inner control is NULL";
        else {
            innerControl.fireEvent("Activity", "readMessage");
            /*Wait for the events*/
            try {
                Thread.sleep(1000);

                if (innerControlEventHandlerReadMessage.equals(EVENT_RECEIVED))
                    result = "0";
                else
                    result = "Acivity.readMessage not received by EventHandler";
            }
            catch (Exception e) {
                result = "Thread sleep interrupted." + e.toString();
            }
        }
        return result;

    }

    public String testActivityReport() {

        String result = "";
        if (innerControl == null)
            result = "inner control is NULL";
        else {
            innerControl.fireEvent("Activity", "report");
            /*Wait for the events*/
            try {
                Thread.sleep(1000);

                if (innerControlEventHandlerReport.equals(EVENT_RECEIVED))
                    result = "0";
                else
                    result = "Acivity.report not received by EventHandler";
            }
            catch (Exception e) {
                result = "Thread sleep interrupted." + e.toString();
            }
        }
        return result;

    }

    public String testActionShopping() {

        String result = "";
        if (innerControl == null)
            result = "inner control is NULL";
        else {
            innerControl.fireEvent("Action", "shopping");
            /*Wait for the events*/
            try {
                Thread.sleep(1000);

                if (innerControlEventHandlerShopping.equals(EVENT_RECEIVED))
                    result = "0";
                else
                    result = "Action.shopping not received by EventHandler";
            }
            catch (Exception e) {
                result = "Thread sleep interrupted." + e.toString();
            }
        }
        return result;

    }

    public String testActionDostuff() {

        String result = "";
        if (innerControl == null)
            result = "inner control is NULL";
        else {
            innerControl.fireEvent("Action", "doStuff");
            /*Wait for the events*/
            try {
                Thread.sleep(1000);

                if (innerControlEventHandlerDoStuff.equals(EVENT_RECEIVED))
                    result = "0";
                else
                    result = "Action.doStuff not received by EventHandler";
            }
            catch (Exception e) {
                result = "Thread sleep interrupted." + e.toString();
            }
        }
        return result;

    }

    /*Tests outer control receiving event from nested control using
     * event listener. The nested control is instantiated programmatically
     */
    public String testEventListener() {

        String result = "init";
        try {

            InnerControlBean nested = (InnerControlBean) java.beans.Beans.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.composition.InnerControlBean");
            if (nested == null)
                result = "Nested control instantiated programmatically is NULL.";
            else {
                //Create an Event Listener
                InnerControlEventListener listener = new InnerControlEventListener();
                nested.addActivityListener(listener);
                nested.addActionListener(listener);
                nested.fireAllEvents();
                try {
                    Thread.sleep(1000);
                    result = listener.getFinalResult();
                }
                catch (Exception e) {
                    result = "Thread sleep interrupted." + e.toString();
                }


            }
        }
        catch (Exception e) {
            result = "Exception caught:" + e.toString();
        }
        return result;
    }

    /*Tests outer control receiving event from nested control using
     * event listener. The nested control is instantiated decalratively
     */
    public String testEventListenerByDeclare() {

        String result = "init";

        if (innerControl == null)
            result = "Nested control instantiated declaratively is NULL.";
        else {
            try {
                //Create an Event Listener
                InnerControlEventListener listener = new InnerControlEventListener();
                innerControl.addActivityListener(listener);
                innerControl.addActionListener(listener);
                innerControl.fireAllEvents();

                Thread.sleep(1000);
                result = listener.getFinalResult();
            }
            catch (Exception e) {
                result = "Thread sleep interrupted." + e.toString();
            }


        }
        return result;

    }

    /*Tests outer control receiving event from nested control using
     * inner class listener. The nested control is instantiated programmatically
     */
    public String testInnerClassListener() {

        String result = "init";
        try {
            InnerControlBean nested = (InnerControlBean) java.beans.Beans.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.composition.InnerControlBean");
            if (nested == null)
                result = "Nested control instantiated programmatically is NULL.";
            else {
                nested.addActivityListener(
                    new InnerControl.Activity() {

                        public void wakeup() {
                            innerClassWakeUp = true;
                        }

                        public int readMessage(String message) {
                            innerClassReadMessage = true;
                            return 0;
                        }

                        public String report() {
                            innerClassReport = true;
                            return "event received.";
                        }
                    }
                );
                nested.addActionListener(
                    new InnerControl.Action() {

                        public Object[] shopping(double credit) {
                            innerClassShopping = true;
                            //return {"food","drinks","candies"};
                            return null;
                        }

                        public void doStuff(String value) {
                            innerClassDoStuff = true;
                        }
                    }
                );
                nested.fireAllEvents();
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e) {
                    /* ignore */
                }

                result = getInnerClassListenerResult();
            }
        }
        catch (Exception e) {
            result = "Exception caught:" + e.toString();
        }
        return result;
    }

    /*Tests outer control receiving event from nested control using
     * inner class listener. The nested control is instantiated decalratively
     */
    public String testInnerClassListenerByDeclare() {

        String result = "init";

        if (innerControl == null)
            result = "Nested control instantiated declaratively is NULL.";
        else {
            try {
                innerControl.addActivityListener(
                    new InnerControl.Activity() {
                        public void wakeup() {
                            innerControlInnerClassWakeUp = true;
                        }

                        public int readMessage(String message) {
                            innerControlInnerClassReadMessage = true;
                            return 0;
                        }

                        public String report() {
                            innerControlInnerClassReport = true;
                            return "event received.";
                        }
                    }
                );
                innerControl.addActionListener(
                    new InnerControl.Action() {

                        public Object[] shopping(double credit) {
                            innerControlInnerClassShopping = true;
                            //return {"food","drinks","candies"};
                            return null;
                        }

                        public void doStuff(String value) {
                            innerControlInnerClassDoStuff = true;
                        }
                    }
                );
                innerControl.fireAllEvents();

                Thread.currentThread().sleep(1000);
            }
            catch (Exception e) {
            }

            result = getInnerControlInnerClassListenerResult();
        }
        return result;

    }

    private String getInnerClassListenerResult() {

        String result = "";

        if (!innerClassWakeUp)
            result = "WakeUp not received.";
        if (!innerClassReadMessage)
            result = result + "readMessage not received.";
        if (!innerClassReport)
            result = result + "report not received.";
        if (!innerClassShopping)
            result = result + "shopping not received.";
        if (!innerClassDoStuff)
            result = result + "dostuff not received.";

        if (result.length() == 0)
            result = "0";

        return result;
    }

    private String getInnerControlInnerClassListenerResult() {

        String result = "";

        if (!innerControlInnerClassWakeUp)
            result = "WakeUp not received.";
        if (!innerControlInnerClassReadMessage)
            result = result + "readMessage not received.";
        if (!innerControlInnerClassReport)
            result = result + "report not received.";
        if (!innerControlInnerClassShopping)
            result = result + "shopping not received.";
        if (!innerControlInnerClassDoStuff)
            result = result + "dostuff not received.";

        if (result.length() == 0)
            result = "0";

        return result;
    }
}
