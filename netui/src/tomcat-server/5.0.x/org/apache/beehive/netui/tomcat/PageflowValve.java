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

import org.apache.catalina.*;
import org.apache.catalina.deploy.SecurityConstraint;
import org.apache.catalina.authenticator.BasicAuthenticator;
import org.apache.catalina.authenticator.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.FailedLoginException;
import java.io.IOException;
import java.security.Principal;

/**
 * Tomcat valve implementation to give the netui pageflow infrastructure access to tomcat
 * internal functionality. When this valve is configured in a context, an instance of
 * org.apache.beehive.netui.tomcat.PageflowHelper will be placed in the session, which can
 * be used by the TomcatServletContainerAdapter implementation of org.apache.beehive.netui.pageflow.ServletAdapter.
 */
public class PageflowValve extends BasicAuthenticator
{
    public void invoke(Request request, Response response, ValveContext valveContext)
            throws IOException, ServletException
    {
        // If this is not an HTTP request and response, just pass them on
        if (!(request instanceof HttpRequest) ||
            !(response instanceof HttpResponse)) {
            valveContext.invokeNext(request, response);
            return;
        }

        // NOTE: this code copied from org.apache.catalina.authenticator.AuthenticatorBase.invoke()
        // Have we got a cached authenticated Principal to put in the request?
        if (cache) {
            Principal principal =
                ((HttpServletRequest) request.getRequest()).getUserPrincipal();
            if (principal == null) {
                Session session = getSession((HttpRequest)request);
                if (session != null) {
                    principal = session.getPrincipal();
                    if (principal != null) {
                        if (debug >= 1)
                            log("We have cached auth type " +
                                session.getAuthType() +
                                " for principal " +
                                session.getPrincipal());
                        ((HttpRequest)request).setAuthType(session.getAuthType());
                        ((HttpRequest)request).setUserPrincipal(principal);
                    }
                }
            }
        }

        // initialize pageflow helper
        HttpServletRequest hreq =
            (HttpServletRequest) request.getRequest();
        PageflowHelper helper = new PageflowHelperImpl();
        ((PageflowHelperImpl)helper).initRequest( (HttpRequest)request, (HttpResponse)response, this );
        hreq.setAttribute( PageflowHelper.PAGEFLOW_HELPER_KEY, helper );

        valveContext.invokeNext(request, response);
    }

    void login( String username, String password, HttpRequest request, HttpResponse response )
        throws LoginException
    {
        // Note: if the login is not successful, we don't reset the current principal (if there is one).
        Principal principal = context.getRealm().authenticate(username, password);
        if (principal != null)
        {
            register(request, response, principal, Constants.BASIC_METHOD,
                     username, password);
            return;
        }

        throw new FailedLoginException( "Page Flow login failed: " + username ); // TODO: I18N
    }

    void logout( boolean invalidateSessions, HttpRequest request, HttpResponse response )
    {
        if ( invalidateSessions )
        {
            // invalidate the session - this will also nuke the request, so save the pageflow helper
            // and put it back when we're done
            HttpServletRequest hreq =
                (HttpServletRequest) request.getRequest();
            PageflowHelper pfh = (PageflowHelper)hreq.getAttribute( PageflowHelper.PAGEFLOW_HELPER_KEY );
            HttpSession session = hreq.getSession(false);
            if ( session != null )
                session.invalidate();
            if ( pfh != null )
                hreq.setAttribute( PageflowHelper.PAGEFLOW_HELPER_KEY, pfh );
        }
        register( request, response, null, null, null, null );
    }

    /**
     * Causes the server to do a security check for the given URI.  If required, it does a redirect to
     * change the scheme (http/https).
     *
     * @param request
     * @param response
     * @param constraint The SecurityConstraint to check against
     * @return <code>true</code> if a redirect occurred.
     */
    public boolean checkSecurity( HttpRequest request, HttpResponse response, SecurityConstraint constraint )
        throws IOException
    {
        // The tomcat version of this returns false if the user was redirected, so we want the opposite of that.
        return ! authenticate( request, response, null );
    }


    Context getContext()
    {
        return context;
    }
}
