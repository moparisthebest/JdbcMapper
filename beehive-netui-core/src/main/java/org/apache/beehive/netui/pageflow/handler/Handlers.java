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
package org.apache.beehive.netui.pageflow.handler;

import org.apache.beehive.netui.util.internal.DiscoveryUtils;
import org.apache.beehive.netui.util.config.ConfigUtil;
import org.apache.beehive.netui.util.config.bean.NetUIConfig;
import org.apache.beehive.netui.util.config.bean.PageFlowHandlersConfig;
import org.apache.beehive.netui.util.config.bean.CustomPropertyConfig;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.pageflow.internal.InternalConstants;
import org.apache.beehive.netui.pageflow.internal.DefaultLoginHandler;
import org.apache.beehive.netui.pageflow.internal.DefaultForwardRedirectHandler;
import org.apache.beehive.netui.pageflow.internal.DefaultReloadableClassHandler;
import org.apache.beehive.netui.pageflow.internal.DefaultExceptionsHandler;
import org.apache.beehive.netui.pageflow.internal.DefaultActionForwardHandler;
import org.apache.beehive.netui.pageflow.internal.DefaultHandler;
import org.apache.beehive.netui.pageflow.internal.DeferredSessionStorageHandler;
import org.apache.beehive.netui.pageflow.PageFlowActionServlet;
import org.apache.beehive.netui.pageflow.PageFlowContextListener;

import javax.servlet.ServletContext;
import java.io.Serializable;

/**
 * ServletContext-scoped container for various Page Flow {@link Handler} instances.
 */
public class Handlers
        implements Serializable
{
    private static final Logger _log = Logger.getInstance( Handlers.class );

    private static final String CONTEXT_ATTR = InternalConstants.ATTR_PREFIX + "_handlers";

    private ActionForwardHandler _actionForwardHandler = null;
    private ExceptionsHandler _exceptionsHandler = null;
    private ForwardRedirectHandler _forwardRedirectHandler = null;
    private LoginHandler _loginHandler = null;
    private StorageHandler _storageHandler = null;
    private ReloadableClassHandler _reloadableClassHandler = null;
    private transient ServletContext _servletContext;

    public static Handlers get( ServletContext servletContext )
    {
        Handlers handlers = ( Handlers ) servletContext.getAttribute( CONTEXT_ATTR );

        if ( handlers == null )
        {
            if ( _log.isErrorEnabled() )
            {
                _log.error( "Page Flow Handlers not initialized; either "
                            + PageFlowActionServlet.class.getName() + " must be the Struts action servlet, or "
                            + PageFlowContextListener.class.getName() + " must be registered as a listener in web.xml." );
            }

            //
            // We can initialize it now, but it's not good because many requests could conceivably be in this
            // code at the same time.
            //
            return init( servletContext );
        }

        handlers.reinit( servletContext );
        return handlers;
    }

    public static Handlers init( ServletContext servletContext )
    {
        Handlers handlers = new Handlers( servletContext );
        servletContext.setAttribute( CONTEXT_ATTR, handlers );
        return handlers;
    }

    private Handlers( ServletContext servletContext )
    {
        _servletContext = servletContext;

        //
        // Load/create Handlers.
        //
        NetUIConfig netuiConfig = ConfigUtil.getConfig();
        PageFlowHandlersConfig handlers = netuiConfig.getPageFlowHandlers();

        DefaultHandler defaultActionForwardHandler = new DefaultActionForwardHandler( servletContext );
        DefaultHandler defaultExceptionsHandler = new DefaultExceptionsHandler( servletContext );
        DefaultHandler defaultForwardRedirectHandler = new DefaultForwardRedirectHandler( servletContext );
        DefaultHandler defaultLoginHandler = new DefaultLoginHandler( servletContext );
        DefaultHandler defaultStorageHandler = new DeferredSessionStorageHandler( servletContext );
        DefaultHandler defaultReloadableClassHandler = new DefaultReloadableClassHandler( servletContext );

        _actionForwardHandler = ( ActionForwardHandler )
                adaptHandler( handlers != null ? handlers.getActionForwardHandlers() : null,
                              defaultActionForwardHandler, ActionForwardHandler.class, servletContext );

        _exceptionsHandler = ( ExceptionsHandler )
                adaptHandler( handlers != null ? handlers.getExceptionsHandlers() : null, defaultExceptionsHandler,
                              ExceptionsHandler.class, servletContext );

        _forwardRedirectHandler = ( ForwardRedirectHandler )
                adaptHandler( handlers != null ? handlers.getForwardRedirectHandlers() : null,
                              defaultForwardRedirectHandler, ForwardRedirectHandler.class, servletContext );

        _loginHandler = ( LoginHandler )
                adaptHandler( handlers != null ? handlers.getLoginHandlers() : null, defaultLoginHandler,
                              LoginHandler.class, servletContext );

        _storageHandler = ( StorageHandler )
                adaptHandler( handlers != null ? handlers.getStorageHandlers() : null, defaultStorageHandler,
                              StorageHandler.class, servletContext );

        _reloadableClassHandler = ( ReloadableClassHandler )
                adaptHandler( handlers != null ? handlers.getReloadableClassHandlers() : null,
                              defaultReloadableClassHandler, ReloadableClassHandler.class, servletContext );
    }

    public void reinit( ServletContext servletContext )
    {
        if ( _servletContext == null )
        {
            _servletContext = servletContext;
            _actionForwardHandler.reinit( servletContext );
            _exceptionsHandler.reinit( servletContext );
            _forwardRedirectHandler.reinit( servletContext );
            _loginHandler.reinit( servletContext );
            _storageHandler.reinit( servletContext );
            _reloadableClassHandler.reinit( servletContext );
        }
    }

    public ActionForwardHandler getActionForwardHandler()
    {
        return _actionForwardHandler;
    }

    public ExceptionsHandler getExceptionsHandler()
    {
        return _exceptionsHandler;
    }

    public ForwardRedirectHandler getForwardRedirectHandler()
    {
        return _forwardRedirectHandler;
    }

    public LoginHandler getLoginHandler()
    {
        return _loginHandler;
    }

    public StorageHandler getStorageHandler()
    {
        return _storageHandler;
    }

    public ReloadableClassHandler getReloadableClassHandler()
    {
        return _reloadableClassHandler;
    }

    private static Handler adaptHandler( org.apache.beehive.netui.util.config.bean.HandlerConfig[] handlerBeanConfigs,
                                         DefaultHandler defaultHandler, Class baseClassOrInterface,
                                         ServletContext servletContext )
    {
        Handler retVal = defaultHandler;

        if ( handlerBeanConfigs != null )
        {
            for ( int i = 0; i < handlerBeanConfigs.length; i++ )
            {
                String handlerClass = handlerBeanConfigs[i].getHandlerClass();
                CustomPropertyConfig[] props = handlerBeanConfigs[i].getCustomProperties();
                Handler handler = createHandler( handlerClass, baseClassOrInterface, retVal, servletContext );

                if ( handler != null )
                {
                    HandlerConfig config = new HandlerConfig( handlerClass );

                    if(props != null) {
                        for ( int j = 0; j < props.length; j++ )
                        {
                            CustomPropertyConfig prop = props[j];
                            config.addCustomProperty( prop.getName(), prop.getValue() );
                        }
                    }
                    handler.init( config, retVal, servletContext );
                    retVal = handler;
                }
            }
        }
        
        defaultHandler.setRegisteredHandler( retVal );
        return retVal;
    }
    
    /**
     * Instantiates a handler, based on the class name in the given HandlerConfig.
     * 
     * @param className the class name of the desired Handler.
     * @param baseClassOrInterface the required base class or interface.  May be <code>null</code>.
     * @return an initialized Handler.
     */ 
    private static Handler createHandler( String className, Class baseClassOrInterface, Handler previousHandler,
                                         ServletContext servletContext )
    {
        assert Handler.class.isAssignableFrom( baseClassOrInterface )
                : baseClassOrInterface.getName() + " cannot be assigned to " + Handler.class.getName();
        
        ClassLoader cl = DiscoveryUtils.getClassLoader();
        
        try
        {
            Class handlerClass = cl.loadClass( className );
            
            if ( ! baseClassOrInterface.isAssignableFrom( handlerClass ) )
            {
                _log.error( "Handler " + handlerClass.getName() + " does not implement or extend "
                            + baseClassOrInterface.getName() );
                return null;
            }
            
            Handler handler = ( Handler ) handlerClass.newInstance();
            return handler;
        }
        catch ( ClassNotFoundException e )
        {
            _log.error( "Could not find Handler class " + className, e );
        }
        catch ( InstantiationException e )
        {
            _log.error( "Could not instantiate Handler class " + className, e );
        }
        catch ( IllegalAccessException e )
        {
            _log.error( "Could not instantiate Handler class " + className, e );
        }
        
        return null;
    }
}
