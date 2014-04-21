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

import org.apache.beehive.netui.core.factory.Factory;
import org.apache.beehive.netui.core.factory.FactoryConfig;
import org.apache.beehive.netui.pageflow.adapter.AdapterContext;
import org.apache.beehive.netui.pageflow.internal.PageFlowBeanContext;
import org.apache.beehive.netui.util.logging.Logger;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Default implementation of a Servlet container adapter.
 */ 
public abstract class DefaultServletContainerAdapter
        implements ServletContainerAdapter
{
    private static boolean _productionMode = true;
    
    private ServletContext _servletContext;
    private PageFlowEventReporter _eventReporter;
    
    static
    {
        String productionModeFlag = System.getProperty( "beehive.productionmode" );
        
        if ( productionModeFlag != null )
        {
            _productionMode = Boolean.valueOf( productionModeFlag ).booleanValue();
        }
        else
        {
            //
            // This is our default definition of "production mode": when asserts are disabled (the following statement
            // sets _productionMode to false when asserts are enabled).
            //
            assert ( _productionMode = false ) || true;
        }
    }

    protected DefaultServletContainerAdapter()
    {
    }
    
    /**
     * Tell whether the system is in production mode.
     * 
     * @return <code>true</code> if the system property "beehive.productionmode" is set to "true", or if asserts are
     *         disabled for this class in the case where the system property has no value; <code>false</code>  if the
     *         system property is set to "false", or if asserts are enabled for this class in the case where the
     *         system property has no value.
     */ 
    public boolean isInProductionMode()
    {
        return _productionMode;
    }

    /**
     * Tell whether a web application resource requires a secure transport protocol.  This default implementation
     * simply returns {@link SecurityProtocol#UNSPECIFIED} for all paths.
     * 
     * @param path a webapp-relative path for a resource.
     * @param request the current HttpServletRequest.
     * @return {@link SecurityProtocol#UNSPECIFIED}.
     */
    public SecurityProtocol getSecurityProtocol( String path, HttpServletRequest request )
    {
        // TODO: implement this based on parsing of web.xml
        return SecurityProtocol.UNSPECIFIED;
    }

    /**
     * Cause the server to do a security check for the given path.  This default implementation does nothing.
     * @return <code>false</code>
     */ 
    public boolean doSecurityRedirect( String path, HttpServletRequest request, HttpServletResponse response )
    {
        return false;
    }

    /**
     * Get the port on which the server is listening for unsecure connections.  This default implementation always
     * returns <code>-1</code>.
     * @param request the current HttpServletRequest.
     * @return <code>-1</code>.
     */ 
    public int getListenPort( HttpServletRequest request )
    {
        // TODO: have a configuration in beehive-netui-config.xml to specify this; an alternative to having to have an adapter.
        return -1;
    }

    /**
     * Get the port on which the server is listening for secure connections.  This default implementation always
     * returns <code>-1</code>.
     * @param request the current HttpServletRequest.
     * @return <code>-1</code>.
     */ 
    public int getSecureListenPort( HttpServletRequest request )
    {
        // TODO: have a configuration in beehive-netui-config.xml to specify this; an alternative to having to have an adapter.
        return -1;
    }

    /**
     * Log in the user, using "weak" username/password authentication.  This default implementation always throws
     * {@link UnsupportedOperationException}.
     * 
     * @throws UnsupportedOperationException in all cases.
     */ 
    public void login( String username, String password, HttpServletRequest request, HttpServletResponse response )
            throws LoginException
    {
        throw new UnsupportedOperationException( "login is not supported by "
                                                 + DefaultServletContainerAdapter.class.getName() );
    }

    /**
     * Log out the user.  This default implementation always throws {@link UnsupportedOperationException}.
     * 
     * @throws UnsupportedOperationException in all cases.
     */ 
    public void logout( boolean invalidateSessions, HttpServletRequest request, HttpServletResponse response )
    {
        throw new UnsupportedOperationException( "logout is not supported by "
                                                 + DefaultServletContainerAdapter.class.getName() );
    }
    
    /**
     * Return the webapp context path for the given request.  This can differ from HttpServletRequest.getContextPath()
     * only in that it will return a valid value even if the request is for the default webapp.  This default 
     * implementation always returns the value of <code>getContextPath()</code> on the request.
     * 
     * @param request the current HttpServletRequest.
     * @return the value of <code>getContextPath()</code> on the current request.
     */ 
    public String getFullContextPath( HttpServletRequest request )
    {
        return request.getContextPath();
    }

    /**
     * Ensure that the given session attribute is replicated in a cluster for session failover.
     * This method does not need to be implemented for servers that do not support clustering and
     * session failover.  The default implementation does nothing.
     * 
     * @param attrName the name of the session attribute for which failover should be ensured.
     * @param attrVal the value of the given session attribute.
     * @param request the current HttpServletRequest.
     */ 
    public void ensureFailover( String attrName, Object attrVal, HttpServletRequest request )
    {
    }

    /**
     * Called at the beginning of each processed request.  This default implementation does nothing.
     * 
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     */ 
    public void beginRequest( HttpServletRequest request, HttpServletResponse response )
    {
    }

    /**
     * Called at the end of each processed request.  This default implementation does nothing.
     * 
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     */ 
    public void endRequest( HttpServletRequest request, HttpServletResponse response )
    {
    }

    /**
     * Get a context object to support Beehive Controls.  This default implementation returns an instance of
     * {@link PageFlowBeanContext}.
     * 
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @return a new ControlBeanContext.
     */
    public Object createControlBeanContext( HttpServletRequest request, HttpServletResponse response )
    {
        return new PageFlowBeanContext();
    }

    /**
     * Get the current ServletContext.
     * @return the current ServletContext.
     */ 
    protected ServletContext getServletContext()
    {
        return _servletContext;
    }
    
    /**
     * Set the AdapterContext.
     * @param context the AdapterContext to set.
     */ 
    public void setContext( AdapterContext context )
    {
        Object servletContext = context.getExternalContext();
        assert servletContext instanceof ServletContext : servletContext;
        _servletContext = ( ServletContext ) servletContext;
        _eventReporter = createEventReporter();
    }
    
    /**
     * Get the name of the platform, which may be used to find platform-specific configuration files.  This default
     * implementation returns "generic".
     * 
     * @return the name of the platform ("generic" in this default implementation).
     */ 
    public String getPlatformName()
    {
        return "generic";
    }

    /**
     * Get an event reporter, which will be notified of events like "page flow created", "action raised", etc.
     * This default implementation returns an instance of {@link DefaultPageFlowEventReporter}.
     * 
     * @return a {@link PageFlowEventReporter}.
     */ 
    public PageFlowEventReporter getEventReporter()
    {
        return _eventReporter;
    }
    
    protected PageFlowEventReporter createEventReporter()
    {
        return new DefaultPageFlowEventReporter( _servletContext );
    }

    /**
     * Generic method to get a Factory class that may be container dependent.
     *
     * <p>
     * This method is called to get the following Factory implementations:
     * </p>
     * <ul>
     *   <li>{@link org.apache.beehive.netui.core.urltemplates.URLTemplatesFactory}</li>
     * </ul>
     *
     * @param factoryType the class type that the factory should extend or implement
     * @param id can be used for the case where there is more than one possible Factory
     *           that extends or implaments the class type.
     * @param config a configuration object passed to a {@link Factory}
     * @return a Factory class that extends or implemtents the given class type.
     */
    public Factory getFactory( Class factoryType, String id, FactoryConfig config)
    {
        return null;
    }
}
