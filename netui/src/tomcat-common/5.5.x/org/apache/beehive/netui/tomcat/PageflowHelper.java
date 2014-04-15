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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.security.auth.login.LoginException;

public interface PageflowHelper
{
    public static final String PAGEFLOW_HELPER_KEY = "PAGEFLOW_HELPER";

    public void login( String username, String password, HttpServletRequest request )
            throws LoginException;

    public void logout( boolean invalidateSessions, HttpServletRequest request );

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
                                       ServletContext servletContext );

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
     * @param request the current Request
     * @return <code>Boolean.TRUE</code> if a transport-guarantee of <code>CONFIDENTIAL</code> or
     *         <code>INTEGRAL</code> is associated with the given resource; <code>Boolean.FALSE</code>
     *         a transport-guarantee of <code>NONE</code> is associated with the given resource; or
     *         <code>null</code> if there is no transport-guarantee associated with the given resource.
     */
    public Boolean isSecureResource( String uri, HttpServletRequest request );

    public int getListenPort( HttpServletRequest request );

    public int getSecureListenPort( HttpServletRequest request );


}
