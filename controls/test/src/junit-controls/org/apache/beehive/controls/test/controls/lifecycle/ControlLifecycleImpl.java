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
package org.apache.beehive.controls.test.controls.lifecycle;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 */
@ControlImplementation(isTransient=true)
public class ControlLifecycleImpl
    implements ControlLifecycle {

    private static final Log LOG = LogFactory.getLog(ControlLifecycleImpl.class);

    @Context
    private ControlBeanContext _controlBeanContext;

    @Context
    private ResourceContext _resourceContext;

    private LinkedList _lifecycleEventStrings = new LinkedList();
    private boolean _onReleaseCalled = true;

    public String echo(String value) {
        return "Echo: '" + value + "'";
    }

    public ResourceContext getTheResourceContext() {
        return _resourceContext;
    }

    public ControlBeanContext getTheControlBeanContext() {
        return _controlBeanContext;
    }

    public List getLifecycleEvents() {
        return Collections.unmodifiableList(_lifecycleEventStrings);
    }

    public void clearLifecycleEvents() {
        _lifecycleEventStrings.clear();
    }

    @EventHandler(field = "_controlBeanContext", eventSet = ControlBeanContext.LifeCycle.class, eventName = "onCreate")
    public void onCreate() {
        // System.out.println("bean context event -- onCreate");
        checkContainerContext();
        _lifecycleEventStrings.add("onCreate");
    }

    @EventHandler(field = "_resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onAcquire")
    public void onAquire() {
        // System.out.println("bean context event -- onAcquire");
        checkContainerContext();

        if(!_onReleaseCalled)
            throw new IllegalStateException("onAcquire called without having called onRelease on the previous request!");

        _onReleaseCalled = false;
        _lifecycleEventStrings.add("onAcquire");
    }

    @EventHandler(field = "_resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onRelease")
    public void onRelease() {
        // System.out.println("bean context event -- onRelease");
        checkContainerContext();
        _onReleaseCalled = true;

        _lifecycleEventStrings.add("onRelease");
    }

    private void checkContainerContext() {
        ControlContainerContext ccc = ControlThreadContext.getContext();
        //System.out.println("control container context: " + (ccc != null ? ccc.hashCode() : "is null"));

        if(ccc == null)
            throw new IllegalStateException("Control could not find a valid ControlContainerContext!");
    }
}
