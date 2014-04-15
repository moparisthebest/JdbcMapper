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
package org.apache.beehive.netui.test.controls.pfcontainer;

import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.ResourceContext;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.netui.pageflow.FlowController;
import org.apache.beehive.netui.pageflow.PageFlowController;

/**
 *
 */
@ControlImplementation(isTransient=true)
public class PageFlowContextControlImpl
    implements PageFlowContextControl {

    @Context
    private ControlBeanContext _context;

    @Context
    private ResourceContext _resourceContext;

    @Context
    private PageFlowController _pageFlowController;

    public boolean checkPageFlow(FlowController flowController) {
        System.out.println("_pageFlowController.identityHashCode: " + System.identityHashCode(_pageFlowController));
        System.out.println("flowController.identityHashCode: " + System.identityHashCode(flowController));

        boolean fieldMatches = (flowController == _pageFlowController);
        boolean serviceMatches = (flowController == _context.getService(PageFlowController.class, null));

        System.out.println("field matches: " + fieldMatches);
        System.out.println("service matches: " + serviceMatches);

        return fieldMatches && serviceMatches;
    }

    @EventHandler(field = "_context", eventSet = ControlBeanContext.LifeCycle.class, eventName = "onCreate")
    public void onCreate() {
        if(_pageFlowController == null)
            throw new IllegalStateException("PageFlowController field null in onCreate");

        if(_context.getService(PageFlowController.class, null) == null)
            throw new IllegalStateException("PageFlowController service null in onCreate");

        System.out.println(getClass().getName() + " onCreate: valid PageFlowController");
    }

    @EventHandler(field = "_resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onAcquire")
    public void onAquire() {
        if(_pageFlowController == null)
            throw new IllegalStateException("PageFlowController field null in onAcquire");

        if(_context.getService(PageFlowController.class, null) == null)
            throw new IllegalStateException("PageFlowController service null in onAcquire");

        System.out.println(getClass().getName() + " onAcquire: valid PageFlowController");
    }

    @EventHandler(field = "_resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onRelease")
    public void onRelease() {
        if(_pageFlowController == null)
            throw new IllegalStateException("PageFlowController field null in onRelease");

        if(_context.getService(PageFlowController.class, null) == null)
            throw new IllegalStateException("PageFlowController service null in onRelease");

        System.out.println(getClass().getName() + " onRelease: valid PageFlowController");
    }
}
