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
package org.apache.beehive.controls.api.context;

import org.apache.beehive.controls.api.bean.ControlBean;
import org.apache.beehive.controls.api.events.EventDispatcher;

/**
 * The ControlContainerContext interface defines the basic contract between an external container
 * of controls and the Controls runtime.
 */
public interface ControlContainerContext extends EventDispatcher, ControlBeanContext
{
    /**
     *  Makes the ControlContainerContext instance the current active context.  This is
     *  called at the beginning of the execution scope for the control container.
     */
    public void beginContext();

    /**
     * Ends the active context associated with the ControlContainerContext.  This is called
     * at the end of the execution scope for the control container.
     */
    public void endContext();

    /**
     * Returns a ControlHandle to the component containing the control.  This handle can be
     * used to dispatch events and operations to a control instance.  This method will return
     * null if the containing component does not support direct dispatch.
     */
    public ControlHandle getControlHandle(ControlBean bean);
}
