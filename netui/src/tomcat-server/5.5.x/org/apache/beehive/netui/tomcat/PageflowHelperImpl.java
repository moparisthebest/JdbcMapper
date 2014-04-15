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

import org.apache.catalina.Context;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.deploy.SecurityConstraint;
import org.apache.catalina.deploy.SecurityCollection;
import org.apache.catalina.authenticator.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.security.auth.login.LoginException;
import java.io.IOException;

public class PageflowHelperImpl implements PageflowHelper
{

    private Request _request = null;
    private Response _response = null;
    private PageflowValve _valve = null;

    public void login( String username, String password, HttpServletRequest request )
            throws LoginException
    {
        _valve.login( username, password, _request, _response );
    }

    public void logout(boolean invalidateSessions, HttpServletRequest request)
    {
        _valve.logout( invalidateSessions, _request, _response );
    }

    /**
     * Causes the server to do a security check for the given URI.  If required, it does a redirect to
     * change the scheme (http/https).
     *
     * @param uri
     * @param request
     * @param response
     * @return <code>true</code> if a redirect occurred.
     */
    public boolean doSecurityRedirect( String uri, HttpServletRequest request, HttpServletResponse response,
                                       ServletContext servletContext )
    {
        SecurityConstraint constraint = findBestMatchSecurityConstraint( uri, _valve.getContext() );

        if ( constraint == null )
            return false;

        try
        {
            return _valve.checkSecurity(_request, _response, constraint);
        }
        catch ( IOException e )
        {
            e.printStackTrace(); // TODO?
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
    public Boolean isSecureResource( String uri, HttpServletRequest request )
    {
        Boolean result = null;
        Context ctx = _valve.getContext();

        SecurityConstraint constraint = findBestMatchSecurityConstraint( uri, ctx );
        if ( constraint != null )
        {
            String userDataConstraint = constraint.getUserConstraint();
            if ( userDataConstraint != null )
            {
                String transportGuarantee = userDataConstraint;
                if ( transportGuarantee.equalsIgnoreCase( Constants.CONFIDENTIAL_TRANSPORT )
                     || transportGuarantee.equalsIgnoreCase( Constants.INTEGRAL_TRANSPORT ) )
                {
//                    cache.put( uri, PROTOCOL_SECURE );
                    return Boolean.TRUE;
                }
                else if ( transportGuarantee.equalsIgnoreCase( Constants.NONE_TRANSPORT ) )
                {
//                    cache.put( uri, PROTOCOL_UNSECURE );
                    return Boolean.FALSE;
                }
            }
        }

        return result;
    }

    public int getListenPort( HttpServletRequest request )
    {
        int port = -1;
        Connector conn = _request.getConnector();
        if ( !conn.getSecure() )
        {
            // this is the regular connector, so use it's port
            port = conn.getPort();
        }
        else
        {
            // look for a non-secure connector on this service with our port as the redirectPort
            Service svc = conn.getService();
            Connector[] connectors = svc.findConnectors();
            for ( int i=0; i<connectors.length; i++ )
            {
                if (connectors[i] != conn && !connectors[i].getSecure()
						&& connectors[i].getRedirectPort() == conn.getPort())
                {
                    port = connectors[i].getPort();
                    break;
                }
            }
            
            if ( port == -1 )
            {
                // last resort, just use the first non-secure connector we find 
                for ( int i=0; i<connectors.length; i++ )
                {
                    if (connectors[i] != conn && !connectors[i].getSecure())
                    {
                        port = connectors[i].getPort();
                        break;
                    }
                }
            }
        }
        return port;
    }

    public int getSecureListenPort( HttpServletRequest request )
    {
        int port = -1;
        Connector conn = _request.getConnector();
        if ( conn.getSecure() )
        {
            // this is the secure connector, so use it's port
            port = conn.getPort();
        }
        else
        {
            // use the redirect port specified in the connector configuration
            port = conn.getRedirectPort();
        }
        return port;
    }


    /**
     * Determine the best matching security constraint. An exact url pattern match will always win; followed
     * by the longest matching path pattern (i.e. /foo/bar/*); followed by a file extension match.
     * @param uri
     * @param ctx
     * @return
     */
    private SecurityConstraint findBestMatchSecurityConstraint( String uri, Context ctx )
    {
        // NOTE: org.apache.catalina.authenticator.AuthenticatorBase.findConstraint() has a similar method but it only
        // finds the first SecurityConstraint that matches, not necessarily the best match

        SecurityConstraint[] securityConstraints = ctx.findConstraints();

        if ( securityConstraints == null || securityConstraints.length == 0 )
        {
            return null;
        }

        String fileExtension = getFileExtension( uri );
        SecurityConstraint matchingConstraint = null;
        int matchingPathLen = -1;
        boolean foundExact = false;

        for ( int i = 0; i < securityConstraints.length && ! foundExact; ++i )
        {
            SecurityConstraint securityConstraint = securityConstraints[i];
            SecurityCollection[] wrcs = securityConstraint.findCollections();

            for ( int j = 0; j < wrcs.length && ! foundExact; ++j )
            {
                String[] urlPatterns = wrcs[j].findPatterns();

                for ( int k = 0; k < urlPatterns.length; ++k )
                {
                    String pattern = urlPatterns[k];

                    if ( pattern.length() > matchingPathLen && pattern.endsWith( "/*" ) )
                    {
                        if ( uri.startsWith( pattern.substring( 0, pattern.length() - 1 ) ) )
                        {
                            matchingConstraint = securityConstraint;
                            matchingPathLen = pattern.length();
                        }
                    }
                    else if ( matchingConstraint == null && pattern.equals( "*." + fileExtension ) )
                    {
                        matchingConstraint = securityConstraint;
                    }
                    else if ( pattern.equals( uri ) )
                    {
                        matchingConstraint = securityConstraint;
                        foundExact = true;
                        break;
                    }
                }
            }
        }

        return matchingConstraint;
    }

    private static String getFileExtension( String filename )
    {
        int lastDot = filename.lastIndexOf( '.' );
        return lastDot != -1 ? filename.substring( lastDot + 1 ) : "";
    }

    void initRequest( Request request, Response response, PageflowValve valve )
    {
        _request = request;
        _response = response;
        _valve = valve;
    }
}
