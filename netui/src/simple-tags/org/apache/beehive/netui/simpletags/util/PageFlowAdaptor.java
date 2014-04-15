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

import org.apache.beehive.netui.pageflow.PageFlowContext;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.struts.Globals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class PageFlowAdaptor
{
    /**
     * Return the current Locale for this request, creating a new one if
     * necessary.  If there is no current Locale, and locale support is not
     * requested, return <code>null</code>.
     */
    public static Locale currentLocale(boolean setLocale)
    {
        PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
        HttpServletRequest req = pfCtxt.getRequest();

        // Create a new session if necessary (setLocale = true) will create the session
        HttpSession session = req.getSession(setLocale);
        if (session == null)
            return null;

        assert(session != null) : "Session is null";

        // Return any currently set Locale in our session
        Locale current = (Locale) session.getAttribute(Globals.LOCALE_KEY);
        if (current != null || !setLocale)
            return current;

        // get the local from the request and set it in the session
        current = req.getLocale();
        if (current != null)
            session.setAttribute(Globals.LOCALE_KEY, current);
        return current;
    }

    /**
     * This method will return the user local of the request.
     * @return the Locale object to use when rendering this tag
     */
    public static Locale getUserLocale() {
        PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
        return InternalUtils.lookupLocale(pfCtxt.getRequest());
    }

}
