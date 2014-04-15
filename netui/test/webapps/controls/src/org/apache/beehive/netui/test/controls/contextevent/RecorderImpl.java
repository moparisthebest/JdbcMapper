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

package org.apache.beehive.netui.test.controls.contextevent;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.ResourceContext;
import org.apache.beehive.controls.api.events.EventHandler;

/**
 * A control impl that listens to and records context events
 * This impl listens to context events by declaring EventHandler.
 * <p/>
 * There are two sources of control context events: ControlBeanContext and
 * ResouceContext.
 * This class listens to context events from both sources.
 */

@ControlImplementation
public class RecorderImpl implements Recorder, java.io.Serializable {

    private String event_log = "init";

    @Context
    ControlBeanContext context;
    @Context
    ResourceContext resourceContext;

    /**
     * A EventHandler that listens to onCreate event
     */
    @EventHandler(field = "context", eventSet = ControlBeanContext.LifeCycle.class, eventName = "onCreate")
    public void onCreate() {
        //System.out.println("+++++++++++++++++++++++++++++++++++++++++");
        //System.out.println("onCreate invoked on RecorderImpl");
        //System.out.println("++++++++++++++++++++++++++++++++++++++++++");

        event_log = event_log + "onCreate";
    }

    /**
     * A EventHandler that listens to onAcquire event
     */
    @EventHandler(field = "resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onAcquire")
    public void onAcquire() {
        //System.out.println("+++++++++++++++++++++++++++++++++++++++++");
        //System.out.println("onAcquire invoked on RecorderImpl");
        //System.out.println("++++++++++++++++++++++++++++++++++++++++++");
        event_log = event_log + "onAcquire";
    }

    /**
     * A EventHandler that listens to onRelease event
     */
    @EventHandler(field = "resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onRelease")
    public void onRelease() {
        //System.out.println("+++++++++++++++++++++++++++++++++++++++++");
        //System.out.println("onRelease invoked on RecorderImpl");
        //System.out.println("++++++++++++++++++++++++++++++++++++++++++");

        event_log = event_log + "onRelease";
    }

    /**
     * Returns the event log
     */
    public String getRecord() {
        //System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
        //System.out.println("getRecord method invoked on RecorderImpl");
        //System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");

        return event_log;
    }
}
