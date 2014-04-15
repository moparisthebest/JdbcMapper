/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at
   
       http://www.apache.org/licenses/LICENSE-2.0
   
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
  
   $Header:$
*/
package org.apache.beehive.controls.test.controls.serialization;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ResourceContext;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.ControlThreadContext;
import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.controls.api.bean.ControlImplementation;

/**
 * Control implementation for testing control serialization and associated lifecycle events.
 */
@ControlImplementation
public class ControlSerializationImpl
    implements ControlSerialization, java.io.Serializable {

    private LinkedList<String> _lifecycleEventStrings = new LinkedList<String>();
    private boolean _onReleaseCalled = true;
    private int _controlState = -1;

    @Context
    private ControlBeanContext _controlBeanContext;

    @Context
    private ResourceContext _resourceContext;

    public List<String> getLifecycleEvents() {
        return Collections.unmodifiableList(_lifecycleEventStrings);
    }

    public void clearLifecycleEvents() {
        _lifecycleEventStrings.clear();
    }

    public int getControlState() {
        return _controlState;
    }

    public void setControlState(int controlState) {
        _controlState = controlState;
    }

    @EventHandler(field = "_controlBeanContext", eventSet = ControlBeanContext.LifeCycle.class, eventName = "onCreate")
    public void onCreate() {
        checkContainerContext();
        _lifecycleEventStrings.add("onCreate");
    }

    @EventHandler(field = "_resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onAcquire")
    public void onAquire() {
        // System.out.println("bean context event -- onAcquire : state " + _controlState + " ctrl " + this);
        checkContainerContext();

        if(!_onReleaseCalled)
            throw new IllegalStateException("onAcquire called without having called onRelease on the previous request! Ctrl state = " + _controlState);

        _onReleaseCalled = false;
        _lifecycleEventStrings.add("onAcquire");
    }

    @EventHandler(field = "_resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onRelease")
    public void onRelease() {
        // System.out.println("bean context event -- onRelease : state = " + _controlState + " ctrl " + this);
        checkContainerContext();
        _onReleaseCalled = true;

        _lifecycleEventStrings.add("onRelease");
    }

    private void checkContainerContext() {
        ControlContainerContext ccc = ControlThreadContext.getContext();
        if(ccc == null)
            throw new IllegalStateException("Control could not find a valid ControlContainerContext!");
    }
}
