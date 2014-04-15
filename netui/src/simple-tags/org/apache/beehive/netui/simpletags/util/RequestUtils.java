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
package org.apache.beehive.netui.simpletags.util;

import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.pageflow.scoping.ScopedRequest;
import org.apache.beehive.netui.pageflow.PageFlowContext;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * This class contains utility methods that deal with requests.  The primary features
 * are the ability to set/get attributes on the outer request (or real request).
 */
public class RequestUtils
{
    /**
     * @param name
     * @param value
     */
    public static void setOuterAttribute(String name, Object value)
    {
        HttpServletRequest req = PageFlowContext.getContext().getRequest();
        ServletRequest realReq = ScopedServletUtils.getOuterRequest(req);
        realReq.setAttribute(name, value);
    }

    /**
     * @param name
     * @return Object
     */
    public static Object getOuterAttribute(String name)
    {
        HttpServletRequest req = PageFlowContext.getContext().getRequest();
        ServletRequest realReq = ScopedServletUtils.getOuterRequest(req);
        return realReq.getAttribute(name);
    }

    /**
     * Return a Scoped Request
     * @return Return the <code>ScopedRequest</code>
     */
    public static ScopedRequest getScopedRequest()
    {
        HttpServletRequest req = PageFlowContext.getContext().getRequest();
        return ScopedServletUtils.unwrapRequest(req);
    }

    /**
     * This method will return the user local of the request.
     * @return the Locale object to use when rendering this tag
     */
    public static Locale getUserLocale() {
        HttpServletRequest req = PageFlowContext.getContext().getRequest();
        return InternalUtils.lookupLocale(req);
    }

    /**
     * This method will look for the <code>ScopeKey</code> value from the <code>ScopedRequest</code>.
     * If it's found it will be returned, otherwise <code>null</code> is returned.
     * @return The ScopeKey value or null
     */
    public static String getScopeKey() 
    {
        HttpServletRequest req = PageFlowContext.getContext().getRequest();
        ScopedRequest scopedRequest = ScopedServletUtils.unwrapRequest(req);
        if (scopedRequest != null) {
            return scopedRequest.getScopeKey().toString();
        }
        return null;
    }
}
