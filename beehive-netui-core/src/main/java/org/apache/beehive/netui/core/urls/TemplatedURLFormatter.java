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
package org.apache.beehive.netui.core.urls;

import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

/**
 * Used by URLRewriterService to apply any relevant templates to a URL,
 * after all other rewriting has been done on the URL.
 *
 * <p>
 * Offers a mechanism for formatting URLs based on templates from a URL
 * template config file. A template is chosen from a reference group
 * based on the template type (key).
 * </p>
 *
 * <p>
 * This class also contains the collection of known and required tokens
 * it expects within URL templates it handles.
 * </p>
 *
 * <p>
 * In general, an implementation of this abstract class should be thread-safe,
 * not having state. If it is used as per-webapp default <code>TemplatedURLFormatter</code>,
 * defined in beehive-netui-config.xml (with a class name),
 * <code>URLRewriterService.getTemplatedURL()</code>
 * will share the same instance (from the ServletContext - see
 * {@link #getTemplatedURLFormatter}) for multiple simultaneous requests.
 * </p>
 */
public abstract class TemplatedURLFormatter
{
    private static final String TEMPLATED_URL_FORMATTER_ATTR = "_netui:templatedURLFormatter";

    /** The default name for a reference group. */
    public static final String DEFAULT_TEMPLATE_REF = "default-url-templates";

    // Base set of tokens
    public static final String SCHEME_TOKEN = "{url:scheme}";
    public static final String DOMAIN_TOKEN = "{url:domain}";
    public static final String PORT_TOKEN = "{url:port}";
    public static final String PATH_TOKEN = "{url:path}";
    public static final String QUERY_STRING_TOKEN = "{url:queryString}";
    public static final String FRAGMENT_TOKEN = "{url:fragment}";

    protected List _knownTokens =
            Arrays.asList( new String[]{ SCHEME_TOKEN, DOMAIN_TOKEN, PORT_TOKEN, FRAGMENT_TOKEN } );

    protected List requiredTokens =
            Arrays.asList( new String[]{ PATH_TOKEN, QUERY_STRING_TOKEN } );

    /**
     * Gets the TemplatedURLFormatter instance from a ServletContext attribute.
     *
     * @param servletContext the current ServletContext.
     * @return the TemplatedURLFormatter instance from the ServletContext.
     */
    public static TemplatedURLFormatter getTemplatedURLFormatter( ServletContext servletContext )
    {
        assert servletContext != null : "The ServletContext cannot be null.";

        if ( servletContext == null )
        {
            throw new IllegalArgumentException( "The ServletContext cannot be null." );
        }

        return ( TemplatedURLFormatter ) servletContext.getAttribute( TEMPLATED_URL_FORMATTER_ATTR );
    }

    /**
     * Adds a given TemplatedURLFormatter instance as an attribute on the ServletContext.
     *
     * @param servletContext the current ServletContext.
     * @param formatter the TemplatedURLFormatter instance to add as an attribute of the context
     */
    public static void initServletContext( ServletContext servletContext, TemplatedURLFormatter formatter )
    {
        assert servletContext != null : "The ServletContext cannot be null.";

        if ( servletContext == null )
        {
            throw new IllegalArgumentException( "The ServletContext cannot be null." );
        }

        servletContext.setAttribute( TEMPLATED_URL_FORMATTER_ATTR, formatter );
    }

    /**
     * Gets the TemplatedURLFormatter instance from a ServletRequest attribute.
     *
     * @param servletRequest the current ServletRequest.
     * @return the TemplatedURLFormatter instance from the ServletRequest.
     */
    public static TemplatedURLFormatter getTemplatedURLFormatter(ServletRequest servletRequest)
    {
        assert servletRequest != null : "The ServletRequest cannot be null.";

        if (servletRequest == null) {
            throw new IllegalArgumentException("The ServletRequest cannot be null.");
        }

        return (TemplatedURLFormatter) servletRequest.getAttribute(TEMPLATED_URL_FORMATTER_ATTR);
    }

    /**
     * Adds a given TemplatedURLFormatter instance as an attribute on the ServletRequest.
     *
     * @param servletRequest the current ServletRequest.
     * @param formatter      the TemplatedURLFormatter instance to add as an attribute of the request
     */
    public static void initServletRequest(ServletRequest servletRequest, TemplatedURLFormatter formatter)
    {
        assert servletRequest != null : "The ServletRequest cannot be null.";

        if (servletRequest == null) {
            throw new IllegalArgumentException("The ServletRequest cannot be null.");
        }

        servletRequest.setAttribute(TEMPLATED_URL_FORMATTER_ATTR, formatter);
    }

    /**
     * Returns the list of the known tokens (strings) that this URL template
     * formatter handles. These strings can be used for the template verification.
     * Tokens are expected to be qualified in braces. E.g. {url:path}
     *
     * @return the list of strings for the known tokens
     */
    public List getKnownTokens()
    {
        return _knownTokens;
    }

    /**
     * Returns the list of the tokens (strings) that this URL template
     * requires when formatting a URL with a template. These strings can
     * be used for the template verification. Tokens are expected to be
     * qualified in braces. E.g. {url:path}
     *
     * @return the list of strings for the known tokens
     */
    public List getRequiredTokens()
    {
        return requiredTokens;
    }

    /**
     * Format the given URL using a URL template, if defined in a URL
     * template config file. The {@link URIContext}
     * encapsulates some additional data needed to write out the string form.
     * E.g. It defines if the &quot;&amp;amp;&quot; entity or the
     * '&amp;' character should be used to separate quary parameters.
     *
     * @param servletContext the current ServletContext.
     * @param request the current ServletRequest.
     * @param uri the MutableURI to be formatted into a String.
     * @param key key for the URL template type to use for formatting the URI
     * @param uriContext data required to write out the string form.
     * @return the URL as a <code>String</code>
     */
    public abstract String getTemplatedURL( ServletContext servletContext,
                                            ServletRequest request, MutableURI uri,
                                            String key, URIContext uriContext );
}
