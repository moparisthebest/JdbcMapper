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
import org.apache.beehive.netui.pageflow.adapter.Adapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.security.auth.login.LoginException;


/**
 * Adapter interface for plugging into various Servlet containers.  An implementor of this interface is "discovered" at
 * runtime.  The discovery process is as follows:
 * <ul>
 *     <li>
 *         A list of META-INF/services/org.apache.beehive.netui.pageflow.ServletContainerAdapter resources is obtained
 *         from classpath.  This means, for example, that a file called
 *         "org.apache.beehive.netui.pageflow.ServletContainerAdapter" under directory META-INF/services would be
 *         found inside any JAR file on classpath.
 *     </li>
 *     <li>
 *         Inside each of these resources is the name of a class that implements ServletContainerAdapter.  This class
 *         is loaded, and its {@link #accept accept} method is called.
 *     </li>
 *     <li>
 *         If {@link #accept accept} returns <code>true</code>, then that implementation class is chosen as the current
 *         adapter; otherwise, the next one in the list is tried.
 *     </li>
 *     <li>If no adapters are discovered, an instance of {@link DefaultServletContainerAdapter} is used.
 * </ul>
 */ 
public interface ServletContainerAdapter
        extends Adapter
{
    /**
     * Tell whether the server is running in production mode.
     * @return <code>true</code> if the server is running in production mode.
     */ 
    public boolean isInProductionMode();
    
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
     * @param path a webapp-relative path for a resource.
     * @param request the current HttpServletRequest.
     * @return <code>Boolean.TRUE</code> if a transport-guarantee of <code>CONFIDENTIAL</code> or
     *         <code>INTEGRAL</code> is associated with the given resource; <code>Boolean.FALSE</code> 
     *         a transport-guarantee of <code>NONE</code> is associated with the given resource; or
     *         <code>null</code> if there is no transport-guarantee associated with the given resource.
     */     
    public SecurityProtocol getSecurityProtocol( String path, HttpServletRequest request );

    /**
     * Cause the server to do a security check for the given path.  If required, it does a redirect to
     * change the scheme (http/https).
     * 
     * @param path the webapp-relative path on which to run security checks.
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @return <code>true</code> if a redirect occurred.
     */ 
    boolean doSecurityRedirect( String path, HttpServletRequest request, HttpServletResponse response );
                           
    
    /**
     * Get the port on which the server is listening for unsecure connections.
     * 
     * @param request the current HttpServletRequest.
     * @return the port on which the server is listening for unsecure connections.
     */ 
    public int getListenPort( HttpServletRequest request );

    /**
     * Get the port on which the server is listening for secure connections.
     * 
     * @param request the current HttpServletRequest.
     * @return the port on which the server is listening for secure connections.
     */ 
    public int getSecureListenPort( HttpServletRequest request );

    /**
     * Log in the user, using "weak" username/password authentication.
     *
     * @param username the user's login name.
     * @param password the user's password.
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * 
     * @exception LoginException if the authentication failed
     */
    public void login( String username, String password, HttpServletRequest request, HttpServletResponse response )
        throws LoginException;
    
    /**
     * Log out the current user.
     * 
     * @param invalidateSessions if <code>true</code>, the session is invalidated (on all single-signon webapps); 
     *            otherwise the session and its data are left intact.  To invalidate the session in only the
     *            current webapp, set this parameter to <code>false</code> and call invalidate() on the HttpSession.
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     */
    public void logout( boolean invalidateSessions, HttpServletRequest request, HttpServletResponse response );
    
    /**
     * Return the webapp context path for the given request.  This differs from HttpServletRequest.getContextPath()
     * only in that it will return a valid value even if the request is for the default webapp.
     * 
     * @param request the current HttpServletRequest.
     */ 
    public String getFullContextPath( HttpServletRequest request );
    
    /**
     * Ensure that the given session attribute is replicated in a cluster for session failover.
     * This method does not need to be implemented for servers that do not support clustering and
     * session failover.
     * 
     * @param attrName the name of the session attribute for which failover should be ensured.
     * @param attrVal the value of the given session attribute.
     * @param request the current HttpServletRequest.
     */ 
    public void ensureFailover( String attrName, Object attrVal, HttpServletRequest request );
    
    /**
     * Called at the beginning of each processed request.
     * 
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     */ 
    public void beginRequest( HttpServletRequest request, HttpServletResponse response );
    
    /**
     * Called at the end of each processed request.
     * 
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     */ 
    public void endRequest( HttpServletRequest request, HttpServletResponse response );
    
    /**
     * Get a context object to support Beehive Controls.
     * 
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @return a new ControlBeanContext.
     */
    public Object createControlBeanContext( HttpServletRequest request, HttpServletResponse response );
    
    /**
     * Get the name of the platform, which may be used to find platform-specific configuration files.
     * 
     * @return the name of the platform
     */ 
    public String getPlatformName(); 
    
    /**
     * Get an event reporter, which will be notified of events like "page flow created", "action raised", etc.
     */ 
    public PageFlowEventReporter getEventReporter();

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
     * @param classType the class type that the factory should extend or implement.
     * @param id can be used for the case where there is more than one possible Factory
     *           that extends or implaments the class type.
     * @param config a configuration object passed to a {@link Factory}
     * @return a Factory class that extends or implemtents the given class type.
     */
    public Factory getFactory( Class classType, String id, FactoryConfig config );
}
