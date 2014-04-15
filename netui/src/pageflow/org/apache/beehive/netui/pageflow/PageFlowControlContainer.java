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
package org.apache.beehive.netui.pageflow;

import org.apache.beehive.controls.api.context.ControlContainerContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

public interface PageFlowControlContainer
{
    /**
     * This method will ensure that a page flow's <code>ControlContainerContext</code> exists, and
     * will make make sure that the begin context is called.  Once this is called, you must insure that
     * the endContext method is called.  This is typically called when a page flow instance is being created.
     * If a Control bean is created programmatically in a page flow using
     * {@link java.beans.Beans#instantiate(ClassLoader, String)},
     * without <code>@Control</code> annotations, you must call this method on the page flow object before
     * the bean is instantiated.
     * @param pfmo
     * @param request
     * @param response
     * @param servletContext
     */
    void createAndBeginControlBeanContext(PageFlowManagedObject pfmo,
                                          HttpServletRequest request,
                                          HttpServletResponse response,
                                          ServletContext servletContext);

    /**
     * This will return the page flows <code>ControlContainerContext</code>.  This call should be made after
     * either the <code>createAndBeginControlBeanContext</code> or <code>beginContextOnPageFlow</code>  has
     * been called.
     * @param pfmo
     * @return Returns the ControlContainerContext for the page flow.  This may return null if the page flow
     * currently doesn't have a ControlContainerContext context.
     */
    ControlContainerContext getControlContainerContext(PageFlowManagedObject pfmo);

    /**
     * This method will perform the beginContext() on any and all <code>ControlContainerContext</code> objects
     * that are managed by the page flow runtime. If you call this, you must call the
     * <code>endContextOnPageFlow</code> method is also called.  The implementation may hold locks on the
     * control container and not calling the endContext may result in hangs and deadlocks.
     * @param pfmo
     * @param request
     * @param response
     * @param servletContext
     */
    void beginContextOnPageFlow(PageFlowManagedObject pfmo,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                ServletContext servletContext);


    /**
     * This method will perform the endContext() on any and all <code>ControlContainerContext</code> objects
     * that are managed by the page flow runtime.
     * @param flowController
     */
    void endContextOnPageFlow(PageFlowManagedObject flowController);
}
