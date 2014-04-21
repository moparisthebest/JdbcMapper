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
package org.apache.beehive.netui.tags;

import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * This class contains utility methods that deal with requests.  The primary features
 * are the ability to set/get attributes on the outer request (or real request).
 */
public class RequestUtils
{

    /**
     * @param req
     * @param name
     * @param value
     */
    public static void setOuterAttribute(HttpServletRequest req, String name, Object value)
    {
        ServletRequest realReq = ScopedServletUtils.getOuterRequest(req);
        realReq.setAttribute(name, value);
    }

    /**
     * @param req
     * @param name
     * @return Object
     */
    public static Object getOuterAttribute(HttpServletRequest req, String name)
    {
        ServletRequest realReq = ScopedServletUtils.getOuterRequest(req);
        return realReq.getAttribute(name);
    }
}
