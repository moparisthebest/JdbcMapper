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
package org.apache.beehive.netui.core.urltemplates;

import java.util.Collection;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import org.apache.beehive.netui.core.factory.Factory;

/**
 * Access point to URL templates (an optional config file to help format
 * rewritten URLs) used by a {@link org.apache.beehive.netui.core.urls.TemplatedURLFormatter}
 * via the {@link org.apache.beehive.netui.core.urls.URLRewriterService}.
 */
public abstract class URLTemplatesFactory extends Factory
{
    private static final String URL_TEMPLATE_FACTORY_ATTR = "_netui:urlTemplatesFactory";

    // Constants for URL template types
    public static final String DEFAULT_TEMPLATE = "default";
    public static final String SECURE_DEFAULT_TEMPLATE = "secure-default";
    public static final String ACTION_TEMPLATE = "action";
    public static final String SECURE_ACTION_TEMPLATE = "secure-action";
    public static final String RESOURCE_TEMPLATE = "resource";
    public static final String SECURE_RESOURCE_TEMPLATE = "secure-resource";
    public static final String RENDER_TEMPLATE = "render";
    public static final String SECURE_RENDER_TEMPLATE = "secure-render";

    /** Default value for path from the web app to the URL templates. */
    public static final String DEFAULT_URL_TEMPLATE_CONFIG_FILE_PATH = "/WEB-INF/beehive-url-template-config.xml";

    // Path to the URL templates config file.
    protected String _configFilePath = DEFAULT_URL_TEMPLATE_CONFIG_FILE_PATH;

    // The known tokens (collection of String objects) in a valid template.
    protected Collection _knownTokens = null;

    // The required tokens (collection of String objects) in a valid template.
    protected Collection _requiredTokens = null;

    /**
     * Gets the URLTemplatesFactory instance from a ServletContext attribute.
     *
     * @param servletContext the current ServletContext.
     * @return the URLTemplatesFactory instance from the ServletContext.
     */
    public static URLTemplatesFactory getURLTemplatesFactory( ServletContext servletContext )
    {
        assert servletContext != null : "The ServletContext cannot be null.";

        if ( servletContext == null )
        {
            throw new IllegalArgumentException( "The ServletContext cannot be null." );
        }

        return ( URLTemplatesFactory ) servletContext.getAttribute( URL_TEMPLATE_FACTORY_ATTR );
    }

    /**
     * Adds a given URLTemplatesFactory instance as an attribute on the ServletContext.
     *
     * @param servletContext the current ServletContext.
     * @param templatesFactory the URLTemplatesFactory instance to add as an attribute of the context
     */
    public static void initServletContext( ServletContext servletContext, URLTemplatesFactory templatesFactory )
    {
        assert servletContext != null : "The ServletContext cannot be null.";

        if ( servletContext == null )
        {
            throw new IllegalArgumentException( "The ServletContext cannot be null." );
        }

        servletContext.setAttribute( URL_TEMPLATE_FACTORY_ATTR, templatesFactory );
    }

    /**
     * Gets the URLTemplatesFactory instance from a ServletRequest attribute.
     *
     * @param servletRequest the current ServletRequest.
     * @return the URLTemplatesFactory instance from the ServletRequest.
     */
    public static URLTemplatesFactory getURLTemplatesFactory(ServletRequest servletRequest)
    {
        assert servletRequest != null : "The ServletRequest cannot be null.";

        if (servletRequest == null) {
            throw new IllegalArgumentException("The ServletRequest cannot be null.");
        }

        return (URLTemplatesFactory) servletRequest.getAttribute(URL_TEMPLATE_FACTORY_ATTR);
    }

    /**
     * Adds a given URLTemplatesFactory instance as an attribute on the ServletRequest.
     *
     * @param servletRequest   the current ServletRequest.
     * @param templatesFactory the URLTemplatesFactory instance to add as an attribute of the request
     */
    public static void initServletRequest(ServletRequest servletRequest, URLTemplatesFactory templatesFactory)
    {
        assert servletRequest != null : "The ServletRequest cannot be null.";

        if (servletRequest == null) {
            throw new IllegalArgumentException("The ServletRequest cannot be null.");
        }

        servletRequest.setAttribute(URL_TEMPLATE_FACTORY_ATTR, templatesFactory);
    }

    /**
     * Allow clients to set their own URL template config file name/path.
     *
     * @param configFilePath An absolute path from the web app root to the URL template config file.
     */
    public void setConfigFilePath( String configFilePath )
    {
        if ( configFilePath == null )
        {
            throw new IllegalArgumentException( "Config file path cannot be null." );
        }

        _configFilePath = configFilePath;
    }

    /**
     * Allow clients to define a set of known tokens for the
     * template verification. Tokens are expected to be qualified
     * in braces. E.g. {url:path}
     * <p/>
     * The template verification will ensure the known tokens in the
     * URL template conforms to a valid format.
     *
     * @param knownTokens The set of known tokens for a valid template.
     */
    public void setKnownTokens( Collection knownTokens )
    {
        _knownTokens = knownTokens;
    }

    /**
     * Allow clients to define a set of required tokens for the
     * template verification. Tokens are expected to be qualified
     * in braces. E.g. {url:path}
     * <p>
     * The template verification will ensure the URL template conforms to
     * a valid format for known tokens and contains the required tokens.
     * </p>
     *
     * @param requiredTokens The set of required tokens in a valid template.
     */
    public void setRequiredTokens( Collection requiredTokens )
    {
        _requiredTokens = requiredTokens;
    }

    /**
     * Returns an array of the URL templates.
     *
     * @return the URL templates
     */
    public abstract URLTemplate[] getURLTemplates();

    /**
     * Returns URL template given the name of the template.
     *
     * @param name name of the template
     * @return template
     */
    public abstract URLTemplate getURLTemplate( String name );

    /**
     * Returns URL template name of the given type (by key) from the
     * desired reference group.
     *
     * @param refGroupName name of a group of templates from the config file.
     * @param key type of the template
     * @return template name
     */
    public abstract String getTemplateNameByRef( String refGroupName, String key );

    /**
     * Initialization method that parses the URL template config file to
     * get the URL templates and template reference groups.
     *
     * @param servletContext the current ServletContext.
     */
    public abstract void load( ServletContext servletContext );
}
