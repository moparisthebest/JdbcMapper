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

package org.apache.beehive.netui.test.controls.composition;

/**
 * A listener class for event raised by InnerControl
 */
public class InnerControlEventListener implements InnerControl.Activity, InnerControl.Action, java.io.Serializable {

    private boolean wakeupReceived = false;
    private boolean readMessageReceived = false;
    private boolean reportReceived = false;
    private boolean shoppingReceived = false;
    private boolean doStuffReceived = false;

    /*
      * BUG!!?? although the event declares methods wakeup, readMessage and
      * report using default accessor,
      * implmentation must change the method accessor to public,
      * or, a compile error!
      *
      *  attempting to assign weaker access privileges; was public
      * [apt]     void wakeup(){wakeupReceived=true;}
      *
      */

    public void wakeup() {
        wakeupReceived = true;
    }

    public int readMessage(String message) {
        readMessageReceived = true;
        return 0;
    }

    public String report() {
        reportReceived = true;
        return "a report from event listener";
    }

    public Object[] shopping(double credit) {
        shoppingReceived = true;
        //return (Object){"clothes","shoes","food"};
        return null;
    }

    public void doStuff(String value) {
        doStuffReceived = true;
    }

    public boolean getWakeupResult() {
        return wakeupReceived;
    }

    public boolean getReadMessageResult() {
        return readMessageReceived;
    }

    public boolean getReportResult() {
        return reportReceived;
    }

    public boolean getShoppingResult() {
        return shoppingReceived;
    }

    public boolean getDoStuffResult() {
        return doStuffReceived;
    }

    /**
     * Checks all the event records and returns '0' if all the events have been received.
     */
    public String getFinalResult() {

        String result = "";

        if (!wakeupReceived)
            result = "WakeUp not received.";
        if (!readMessageReceived)
            result = result + "readMessage not received.";
        if (!reportReceived)
            result = result + "report not received.";
        if (!shoppingReceived)
            result = result + "shopping not received.";
        if (!doStuffReceived)
            result = result + "dostuff not received.";

        if (result.length() == 0)
            result = "0";

        return result;
    }
}
