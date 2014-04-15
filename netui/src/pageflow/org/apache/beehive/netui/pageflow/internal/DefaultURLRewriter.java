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

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.core.urls.MutableURI;
import org.apache.beehive.netui.core.urls.URLRewriter;
import org.apache.beehive.netui.core.urls.URLType;
import org.apache.beehive.netui.core.urls.AjaxUrlInfo;
import org.apache.beehive.netui.pageflow.ServletContainerAdapter;
import org.apache.beehive.netui.pageflow.requeststate.INameable;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.util.logging.Logger;


public class DefaultURLRewriter extends URLRewriter
{
    private static final Logger _log = Logger.getInstance( DefaultURLRewriter.class );

    public String getNamePrefix( ServletContext servletContext, ServletRequest request, String name )
    {
        return "";
    }

    /**
     * This method will get the prefix for all URLs that are used for AJAX processing
     * @param servletContext the current ServletContext.
     * @param request the current ServletRequest.
     * @param nameable the INamable object that will handle the request
     */
    public AjaxUrlInfo getAjaxUrl(ServletContext servletContext, ServletRequest request, Object nameable)
    {
        HttpServletRequest req = (HttpServletRequest) request;
        // the default behavior is to set the parameter as the nameable's name or null
        String name = null;
        if (nameable instanceof INameable)
            name = ((INameable) nameable).getObjectName();

        String path = req.getServletPath();
        int idx = path.lastIndexOf('/');
        if (idx != -1) {
            path = "/" + path.substring(1, idx);
        }

        path = req.getContextPath() + path;
        return new AjaxUrlInfo(path,name);
    }

    public void rewriteURL( ServletContext servletContext, ServletRequest request,
                            ServletResponse response, MutableURI url, URLType type, boolean needsToBeSecure )
    {
        ServletContainerAdapter servletContainerAdapter = AdapterManager.getServletContainerAdapter( servletContext );

        // If url is not absolute, then do default secure/unsecure rewriting
        if ( !url.isAbsolute() )
        {
            if ( needsToBeSecure )
            {
                if ( !request.isSecure() )
                {
                    int securePort = servletContainerAdapter.getSecureListenPort( (HttpServletRequest) request );

                    if (securePort != -1)
                    {
                        internalRewriteUrl( url, "https", securePort, request.getServerName() );
                    }
                    else
                    {
                        if (_log.isWarnEnabled())
                        {
                            _log.warn("Could not rewrite URL " + url.getURIString( null ) + " to be secure because" +
                                      " a secure port was not provided by the ServletContainerAdapter.");
                        }
                    }
                }
            }
            else
            {
                if ( request.isSecure() )
                {
                    int listenPort = servletContainerAdapter.getListenPort((HttpServletRequest) request);

                    if (listenPort != -1 )
                    {
                        internalRewriteUrl( url, "http", listenPort, request.getServerName() );
                    }
                    else
                    {
                        if (_log.isWarnEnabled())
                        {
                            _log.warn("Could not rewrite URL " + url.getURIString( null ) + " to be non-secure" +
                                      " because a port was not provided by the ServletContainerAdapter.");
                        }
                    }
                }
            }
        }

        //
        // If the current request has a special parameter that addresses a named 'scope',
        // add the parameter to the URL.
        //
        String scopeID = request.getParameter( ScopedServletUtils.SCOPE_ID_PARAM );
        if ( scopeID != null )
        {
            // check to see if the param is already there.
            if ( url.getParameter( ScopedServletUtils.SCOPE_ID_PARAM ) == null )
            {
                url.addParameter( ScopedServletUtils.SCOPE_ID_PARAM, scopeID, true );
            }
        }
    }

    private static void internalRewriteUrl( MutableURI url, String protocol, int port, String serverName )
    {
        // Need to build up the url
        url.setScheme( protocol );
        url.setHost( serverName );
        url.setPort( port );
    }

    /**
     * Determines if the passed-in Object is equivalent to this DefaultURLRewriter.
     * Since there is no member data for this class they will all be equal.
     *
     * @param object the Object to test for equality.
     * @return true if object is another instance of DefaultURLRewriter.
     */
    
    public boolean equals( Object object )
    {
        if ( object == this ) { return true; }

        if ( object == null || !object.getClass().equals( this.getClass() ) )
        {
            return false;
        }

        return true;
    }

    /**
     * Returns a hash code value for the object.
     * Implemented in conjunction with equals() override.
     * Since there is no member data for this class we
     * always return the same value.
     *
     * @return a hash code value for this object.
     */
    
    public int hashCode()
    {
        return 0;
    }
}
