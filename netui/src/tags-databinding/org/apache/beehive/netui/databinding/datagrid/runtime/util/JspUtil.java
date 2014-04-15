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
package org.apache.beehive.netui.databinding.datagrid.runtime.util;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.tags.internal.PageFlowTagUtils;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * Utility class used for operations related to JSPs.
 * </p>
 */
public final class JspUtil {

    /* do not construct */
    private JspUtil() {
    }

    /**
     * Attempt to obtain a {@link HttpServletRequest} from a {@link JspContext}.
     * @param jspContext the jsp context
     * @return the {@link HttpServletRequest}
     * @throws IllegalStateException if the {@link JspContext} is unable to provide a {@link HttpServletRequest}
     */
    public static HttpServletRequest getRequest(JspContext jspContext) {
        PageContext pageContext = getPageContext(jspContext);
        return (HttpServletRequest)pageContext.getRequest();
    }

    /**
     * Attempt to convert a {@link JspContext} into a {@link PageContext}.
     * @param jspContext the jsp context
     * @return the page context
     * @throws IllegalStateException if the {@link JspContext} can't be converted into a {@link PageContext}
     */
    public static PageContext getPageContext(JspContext jspContext) {
        if(!(jspContext instanceof PageContext))
            throw new IllegalStateException(Bundle.getErrorString("DataGridUtil_IllegalJspContext", new Object[]{(jspContext != null ? jspContext.getClass().getName() : "null")}));
        else
            return (PageContext)jspContext;
    }

    /**
     * <p>
     * Utility method used by data grid related classes to create URLs.  If both an action name and href are provided,
     * the action name will be used to construct the URL string.
     * </p>
     * @param href the href
     * @param action the action
     * @param location the intra page location
     * @param scope the scope into which to create the URL
     * @param params a map of parameters to attach to the URL
     * @param jspContext the jsp context
     * @return a URL represented as a string.  This URL will be correctly encoded by calling {@link HttpServletResponse#encodeURL(String)}
     * @throws URISyntaxException
     */
    public static String createURL(String href, String action, String location, String scope, Map params, JspContext jspContext)
            throws URISyntaxException {
        PageContext pageContext = getPageContext(jspContext);

        /* add the jpfScopeID parameter, if the scope attribute is present. */
        if(scope != null) {
            if(params == null)
                params = new HashMap();

            params.put(ScopedServletUtils.SCOPE_ID_PARAM, scope);
        }

        String uri = null;
        if(action != null)
            uri = PageFlowTagUtils.rewriteActionURL(pageContext, action, params, location);
        else if(href != null)
            uri = PageFlowTagUtils.rewriteHrefURL(pageContext, href, params, location);
        else
            return ((HttpServletRequest)pageContext.getRequest()).getPathTranslated();

        assert uri != null;

        HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
        return response.encodeURL(uri);
    }

    /**
     * Get the {@link Locale} from the {@link JspContext}
     * @param jspContext the jsp context
     * @return the current locale
     */
    public static Locale getLocale(JspContext jspContext) {
        return InternalUtils.lookupLocale(jspContext);
    }
}
