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

import org.apache.beehive.netui.pageflow.handler.StorageHandler;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.pageflow.internal.InternalConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;

public class PageFlowControlContainerFactory
{
    private static String PAGEFLOW_CONTROL_CONTAINER = InternalConstants.ATTR_PREFIX + "PageFlowControlContainerInstance";

    /**
     * This method will return the <code>PageFlowControlContainer</code> that is acting as the
     * control container for the page flow runtime.
     * @param request The current request
     * @param servletContext The servlet context
     * @return The <code>pageFLowControlContainer</code> acting as the control container.
     */
    public static synchronized PageFlowControlContainer getControlContainer(HttpServletRequest request, ServletContext servletContext)
    {
        PageFlowControlContainer pfcc = (PageFlowControlContainer) getSessionVar(request, servletContext, PAGEFLOW_CONTROL_CONTAINER);
        if (pfcc != null)
            return pfcc;

        pfcc = new PageFlowControlContainerImpl();
        setSessionVar(request,servletContext,PAGEFLOW_CONTROL_CONTAINER,pfcc);
        return pfcc;
    }

    /**
     * This is a generic routine that will retrieve a value from the Session through the
     * StorageHandler.
     * @param request
     * @param servletContext
     * @param name The name of the value to be retrieved
     * @return The requested value from the session
     */
    private static Object getSessionVar(HttpServletRequest request, ServletContext servletContext, String name)
    {
        StorageHandler sh = Handlers.get( servletContext ).getStorageHandler();

        HttpServletRequest unwrappedRequest = PageFlowUtils.unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        String attrName = ScopedServletUtils.getScopedSessionAttrName(name, unwrappedRequest);
        return sh.getAttribute( rc, attrName );
    }

    /**
     * This is a generic routine that will store a value into the Session through the StorageHandler.
     * @param request
     * @param servletContext
     * @param name The name of the variable to be stored
     * @param value The value of the variable to be stored
     */
    private static void setSessionVar(HttpServletRequest request, ServletContext servletContext, String name, Object value)
    {
        StorageHandler sh = Handlers.get( servletContext ).getStorageHandler();

        HttpServletRequest unwrappedRequest = PageFlowUtils.unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        String attrName = ScopedServletUtils.getScopedSessionAttrName(name, unwrappedRequest);
        sh.setAttribute( rc, attrName, value );
    }
}
