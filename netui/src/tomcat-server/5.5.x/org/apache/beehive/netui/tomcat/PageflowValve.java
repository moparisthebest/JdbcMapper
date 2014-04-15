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
import org.apache.catalina.Session;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Tomcat valve implementation to give the netui pageflow infrastructure access to tomcat
 * internal functionality. When this valve is configured in a context, an instance of
 * org.apache.beehive.netui.tomcat.PageflowHelper will be placed in the session, which can
 * be used by the TomcatServletContainerAdapter implementation of org.apache.beehive.netui.pageflow.ServletAdapter.
 */
public class PageflowValve extends BasicAuthenticator
{
    private static Log log = LogFactory.getLog(PageflowValve.class);

    public String getInfo() {
        return info;
    }
    /**
     * Descriptive information about this implementation.
     */
    protected static final String info =
        "org.apache.beehive.netui.tomcat.PageFlowValve/1.0";

    public void invoke(Request request, Response response)
            throws IOException, ServletException
    {
        // NOTE: this code copied from org.apache.catalina.authenticator.AuthenticatorBase.invoke()
        // Have we got a cached authenticated Principal to put in the request?
        if (cache) {
            Principal principal = request.getUserPrincipal();
            if (principal == null) {
                Session session = request.getSessionInternal(false);
                if (session != null) {
                    principal = session.getPrincipal();
                    if (principal != null) {
                        if (log.isDebugEnabled())
                            log.debug("We have cached auth type "
                                    + session.getAuthType() + " for principal "
                                    + session.getPrincipal());
                        request.setAuthType(session.getAuthType());
                        request.setUserPrincipal(principal);
                    }
                }
            }
        }

        // initialize pageflow helper
        if (log.isDebugEnabled()) {
            log.debug("PageFlowValve initializing request property with the helper.");
        }
        HttpServletRequest hreq =
            (HttpServletRequest) request.getRequest();
        PageflowHelper helper = new PageflowHelperImpl();
        ((PageflowHelperImpl)helper).initRequest( request, response, this );
        hreq.setAttribute( PageflowHelper.PAGEFLOW_HELPER_KEY, helper );

        // Any and all specified constraints have been satisfied
        if (log.isDebugEnabled()) {
            log.debug("PageflowValve successfully invoked. Now calling Tomcat AuthenticatorBase...");
        }
        super.invoke(request, response);
    }

    void login( String username, String password, Request request, Response response )
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

    void logout( boolean invalidateSessions, Request request, Response response )
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
    public boolean checkSecurity( Request request, Response response, SecurityConstraint constraint )
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
