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
package bugs.j1097.controls;

import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.ResourceContext;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.controls.api.ControlException;

/**
 * Control to test NetUI exception handling when
 * ControlContainerContext endContext() throws. It is
 * designed to throw twice, once during setThrowException
 * and then again in onRelease(), during endContext().
 */
@ControlImplementation(isTransient=true)
public class PageFlowControlImpl
    implements PageFlowControl {

    @Context
    private ControlBeanContext _context;

    @Context
    private ResourceContext _resourceContext;

    private boolean _throwException = false;

    public void setThrowException(boolean throwException) {
        _throwException = throwException;
        if (_throwException) {
            throw new ControlException("Thrown from setThrowException() to test exception handling.");
        }
    }

    @EventHandler(field = "_context", eventSet = ControlBeanContext.LifeCycle.class, eventName = "onCreate")
    public void onCreate() {
        //System.out.println(getClass().getName() + ".onCreate()");
    }

    @EventHandler(field = "_resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onAcquire")
    public void onAquire() {
        //System.out.println(getClass().getName() + ".onAquire()");
    }

    @EventHandler(field = "_resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onRelease")
    public void onRelease() {
        //System.out.println(getClass().getName() + ".onRelease()");
        if (_throwException) {
            throw new ControlException("Thrown from onRelease() to test exception handling.");
        }
    }
}
