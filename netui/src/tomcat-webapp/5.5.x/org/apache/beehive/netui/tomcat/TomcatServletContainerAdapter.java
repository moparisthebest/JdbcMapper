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
package org.apache.beehive.netui.tomcat;

import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.pageflow.DefaultServletContainerAdapter;
import org.apache.beehive.netui.pageflow.SecurityProtocol;
import org.apache.beehive.netui.pageflow.adapter.AdapterContext;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.internal.DiscoveryUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.security.auth.login.LoginException;

public class TomcatServletContainerAdapter extends DefaultServletContainerAdapter
{
    private static final Logger _log = Logger.getInstance( TomcatServletContainerAdapter.class );
    
    private static final String CATALINA_HOME_PROP = "catalina.home";
    private static final String HELPER_INTERFACE_CLASSNAME = "org.apache.beehive.netui.tomcat.PageflowHelper";
    
    
    public boolean accept( AdapterContext context )
    {
        if ( System.getProperty( CATALINA_HOME_PROP ) != null )
        {
            try
            {
                //
                // See if our helper interface is in the common classloader.
                //
                DiscoveryUtils.getClassLoader().loadClass( HELPER_INTERFACE_CLASSNAME );
            }
            catch ( ClassNotFoundException e )
            {
                _log.error( "Could not load helper interface " + HELPER_INTERFACE_CLASSNAME + "; cannot use "
                            + TomcatServletContainerAdapter.class.getName() + ".  Make sure that the netui tomcat common" +
                            " and server jars are in Tomcat's common and server lib directories, respectively.", e );
                return false;
            }
            
            if ( _log.isInfoEnabled() )
            {
                _log.info( "Using " + TomcatServletContainerAdapter.class.getName() + " as the ServletContainerAdapter." );
            }
                    
            return true;
        }
        else
        {
            if ( _log.isInfoEnabled() )
            {
                _log.info( "Not running in Tomcat (" + CATALINA_HOME_PROP + " is not set); "
                           + TomcatServletContainerAdapter.class.getName() + " will not be used." );
            }
            
            return false;
        }
    }
    
    /**
     * Tell whether a web application resource requires a secure transport protocol.  This is
     * determined from web.xml; for example, the following block specifies that all resources under
     * /login require a secure transport protocol.
     * <pre>
     *    &lt;security-constraint&gt;
     *        &lt;web-resource-collection&gt;
     *          &lt;web-resource-name&gt;Secure PageFlow - begin&lt;/web-resource-name&gt;
     *          &lt;url-pattern&gt;/login/*&lt;/url-pattern&gt;
     *        &lt;/web-resource-collection&gt;
     *        &lt;user-data-constraint&gt;
     *           &lt;transport-guarantee&gt;CONFIDENTIAL&lt;/transport-guarantee&gt;
     *        &lt;/user-data-constraint&gt;
     *    &lt;/security-constraint&gt;
     * </pre>
     *
     * @param uri a webapp-relative URI for a resource.  There must not be query parameters or a scheme
     *            on the URI.
     * @param request the current request.
     * @return <code>Boolean.TRUE</code> if a transport-guarantee of <code>CONFIDENTIAL</code> or
     *         <code>INTEGRAL</code> is associated with the given resource; <code>Boolean.FALSE</code>
     *         a transport-guarantee of <code>NONE</code> is associated with the given resource; or
     *         <code>null</code> if there is no transport-guarantee associated with the given resource.
     */
    
    public SecurityProtocol getSecurityProtocol( String uri, HttpServletRequest request )
    {
        uri = ScopedServletUtils.normalizeURI( uri );
        Boolean isSecure= getHelper( request ).isSecureResource( uri, request );
        
        return isSecure != null
               ? ( isSecure.booleanValue() ? SecurityProtocol.SECURE : SecurityProtocol.UNSECURE )
               : SecurityProtocol.UNSPECIFIED;
    }

    
    public boolean doSecurityRedirect( String uri, HttpServletRequest request, HttpServletResponse response )
    {
        return getHelper( request ).doSecurityRedirect( uri, request, response, getServletContext() );
    }

    
    public int getListenPort( HttpServletRequest request )
    {
        return getHelper( request ).getListenPort( request );
    }

    
    public int getSecureListenPort( HttpServletRequest request )
    {
        return getHelper( request ).getSecureListenPort( request );
    }

    
    public void login( String username, String password, HttpServletRequest request, HttpServletResponse response )
            throws LoginException
    {
        getHelper( request ).login( username, password, request );
    }

    
    public void logout( boolean invalidateSessions, HttpServletRequest request, HttpServletResponse response )
    {
        getHelper( request ).logout( invalidateSessions, request );
    }

    private static PageflowHelper getHelper( HttpServletRequest request )
    {
        HttpServletRequest outerRequest = ScopedServletUtils.getOuterRequest( request ); 
        PageflowHelper helper = ( PageflowHelper ) outerRequest.getAttribute( PageflowHelper.PAGEFLOW_HELPER_KEY );
        
        if ( helper == null )
        {
            String msg =
                    "Page Flow helper not found in session. Make sure PageflowValve is installed in this context.";
            _log.error( msg );
            throw new UnsupportedOperationException( msg );
        }
        
        return helper;
    }
}
