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
package org.apache.beehive.netui.pageflow.internal;

import org.apache.beehive.netui.core.urls.TemplatedURLFormatter;
import org.apache.beehive.netui.core.urltemplates.URLTemplatesFactory;
import org.apache.beehive.netui.pageflow.FacesBackingBeanFactory;
import org.apache.beehive.netui.pageflow.FlowControllerFactory;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.ProcessPopulate;
import org.apache.beehive.netui.pageflow.RequestParameterHandler;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.util.config.ConfigInitializationException;
import org.apache.beehive.netui.util.config.ConfigUtil;
import org.apache.beehive.netui.util.config.bean.PrefixHandlerConfig;
import org.apache.beehive.netui.util.config.bean.UrlConfig;
import org.apache.beehive.netui.util.internal.DiscoveryUtils;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.xml.XmlInputStreamResolver;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Handles initialization of the Page Flow runtime.
 */
public class PageFlowInitialization {

    private static final String ALREADY_INIT_ATTR = InternalConstants.ATTR_PREFIX + "contextInit";

    private static final Logger _log = Logger.getInstance( PageFlowInitialization.class );

    private static class TransientFlag implements Serializable
    {
        private transient boolean _flag;

        public TransientFlag()
        {
            _flag = true;
        }

        public boolean isSet()
        {
            return _flag;
        }
    }

    public static boolean isInit( ServletContext servletContext )
    {
        //
        // If the flag is present, but was serialized, then the webapp was redeployed.  At this point, we want
        // to go through the init logic again.
        //
        TransientFlag flag = ( TransientFlag ) servletContext.getAttribute( ALREADY_INIT_ATTR );
        return flag != null && flag.isSet();
    }

    public static void performInitializations( ServletContext servletContext, XmlInputStreamResolver overrideConfigInput )
    {
        servletContext.setAttribute( ALREADY_INIT_ATTR, new TransientFlag() );

        //
        // Initialize the config file, unless it's already initialized.  This can happen because the scope for the 
        // config (static) isn't the same as the scope for PageFlowActionServlet, which may get created and destroyed
        // within a classloader (which is the case during StrutsTestCase tests).
        //
        if ( ! ConfigUtil.isInit() )
        {
            XmlInputStreamResolver resolver =
                    overrideConfigInput != null ? overrideConfigInput : new NetUIConfigResolver(servletContext);
            try
            {
                ConfigUtil.init(resolver);
            }
            catch ( ConfigInitializationException e )
            {
                _log.fatal( "Could not initialize from " + resolver.getResourcePath(), e );
                throw new IllegalStateException( "Could not initialize from " + resolver.getResourcePath(), e );
            }
        }

        AdapterManager.initServletContext( servletContext );
        LegacySettings.init( servletContext );
        Handlers.init( servletContext );
        FlowControllerFactory.init( servletContext );
        FacesBackingBeanFactory.init( servletContext );
        initPrefixHandlers();
        initURLTemplates( servletContext );
    }

    /**
     * This method will initialize all of the PrefixHandlers registered in the netui config.
     * The prefix handlers are registered with ProcessPopulate and are typically implemented as
     * public inner classes in the tags that require prefix handlers.
     */
    private static void initPrefixHandlers()
    {
        PrefixHandlerConfig[] prefixHandlers = ConfigUtil.getConfig().getPrefixHandlers();
        if ( prefixHandlers == null )
        {
            return;
        }
        for ( int i = 0; i < prefixHandlers.length; i++ )
        {
            try
            {
                Class prefixClass = Class.forName( prefixHandlers[i].getHandlerClass() );
                String name = prefixHandlers[i].getName();
                if ( name == null || name.equals( "" ) )
                {
                    _log.warn( "The name for the prefix handler '" + prefixHandlers[i].getHandlerClass()
                            + "' must not be null" );
                    continue;
                }
                Object o = prefixClass.newInstance();
                if ( !( o instanceof RequestParameterHandler ) )
                {
                    _log.warn( "The class '" + prefixHandlers[i].getHandlerClass()
                            + "' must be an instance of RequestParameterHandler" );
                    continue;
                }
                ProcessPopulate.registerPrefixHandler( name, ( RequestParameterHandler ) o );
            }
            catch ( ClassNotFoundException e )
            {
                _log.warn( "Class '" + prefixHandlers[i].getHandlerClass() + "' not found", e );
            }
            catch ( IllegalAccessException e )
            {
                _log.warn( "Illegal access on Class '" + prefixHandlers[i].getHandlerClass() + "'", e );

            }
            catch ( InstantiationException e )
            {
                _log.warn( "InstantiationException on Class '" + prefixHandlers[i].getHandlerClass() + "'",
                        e.getCause() );
            }
        }
    }

    /**
     * Creates a URLTemplatesFactory (may be container specific from the
     * ServletContainerAdapter) and the the default TemplatedURLFormatter
     * (registered in the netui config). These classes are used by the
     * URLRewriterService.
     */
    private static void initURLTemplates( ServletContext servletContext )
    {
        TemplatedURLFormatter formatter = TemplatedURLFormatter.getTemplatedURLFormatter( servletContext );
        if ( formatter == null )
        {
            // get the default template formatter class name from the config file
            formatter = getTemplatedURLFormatter();

            assert formatter != null : "Found a non-null URL formatter";

            // set the TemplatedURLFormatter attribute on the context.
            TemplatedURLFormatter.initServletContext( servletContext, formatter );
        }

        URLTemplatesFactory templatesFactory = URLTemplatesFactory.getURLTemplatesFactory( servletContext );
        if ( templatesFactory == null )
        {
            // URLTemplatesFactory has not been initialized,
            // get a URLTemplatesFactory object from the containerAdapter.
            templatesFactory = PageFlowUtils.createURLTemplatesFactory( servletContext );

            // get the known/req tokens from the default formatter for the factory to use to verify templates
            templatesFactory.setKnownTokens( formatter.getKnownTokens() );
            templatesFactory.setRequiredTokens( formatter.getRequiredTokens() );
            templatesFactory.load( servletContext );

            // set the URLTemplatesFactory attribute on the context.
            URLTemplatesFactory.initServletContext( servletContext, templatesFactory );
        }
    }

    private static TemplatedURLFormatter getTemplatedURLFormatter()
    {
        TemplatedURLFormatter formatter = null;

        // check for a default template formatter class name from the config file
        UrlConfig urlConfig = ConfigUtil.getConfig().getUrlConfig();
        if ( urlConfig != null )
        {
            String className = urlConfig.getTemplatedUrlFormatterClass();
            if ( className != null )
            {
                className = className.trim();

                // create an instance of the def template formatter class
                ClassLoader cl = DiscoveryUtils.getClassLoader();

                try
                {
                    Class formatterClass = cl.loadClass( className );
                    if ( ! TemplatedURLFormatter.class.isAssignableFrom( formatterClass ) )
                    {
                        _log.error( "The templated-url-formatter-class, " + className
                                + ", does not extend TemplatedURLFormatter." );
                    }
                    else
                    {
                        formatter = ( TemplatedURLFormatter ) formatterClass.newInstance();
                    }
                }
                catch ( ClassNotFoundException e )
                {
                    _log.error( "Could not find templated-url-formatter-class " + className, e );
                }
                catch ( InstantiationException e )
                {
                    _log.error( "Could not instantiate templated-url-formatter-class " + className, e );
                }
                catch ( IllegalAccessException e )
                {
                    _log.error( "Could not instantiate templated-url-formatter-class " + className, e );
                }
            }
        }

        return formatter;
    }

    private static class NetUIConfigResolver
            extends XmlInputStreamResolver {

        private ServletContext _servletContext = null;

        private NetUIConfigResolver(ServletContext servletContext) {
            _servletContext = servletContext;
        }

        public String getResourcePath() {
            return InternalConstants.NETUI_CONFIG_PATH;
        }

        public InputStream getInputStream() {
            return _servletContext.getResourceAsStream(getResourcePath());
        }
    }
}
