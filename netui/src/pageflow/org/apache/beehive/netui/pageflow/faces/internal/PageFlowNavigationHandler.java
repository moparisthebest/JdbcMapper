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
package org.apache.beehive.netui.pageflow.faces.internal;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import java.io.IOException;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.PageFlowConstants;
import org.apache.beehive.netui.pageflow.FlowControllerFactory;
import org.apache.beehive.netui.pageflow.RequestContext;
import org.apache.beehive.netui.pageflow.internal.InternalConstants;
import org.apache.beehive.netui.util.logging.Logger;


/**
 * Internal class used in JSF/Page Flow integration.  This NavigationHandler raises Page Flow actions for JSF pages
 * that are in Page Flow directories.
 * @see org.apache.beehive.netui.pageflow.faces.PageFlowApplicationFactory
 */ 
public class PageFlowNavigationHandler
    extends NavigationHandler
{
    private static final Logger _log = Logger.getInstance( PageFlowNavigationHandler.class );
    static final String ALREADY_FORWARDED_ATTR = InternalConstants.ATTR_PREFIX + "navHandled"; 
    
    private NavigationHandler _baseHandler;


    public PageFlowNavigationHandler( NavigationHandler base )
    {
        if ( _log.isDebugEnabled() )
        {
            _log.debug( "Adapting NavigationHandler" + base );
        }
        
        _baseHandler = base;
    }

    public void handleNavigation( FacesContext context, String fromAction, String outcome )
    {
        Object request = context.getExternalContext().getRequest();
        Object response = context.getExternalContext().getResponse();
        Object extContext = context.getExternalContext().getContext();

        if ( request instanceof HttpServletRequest && response instanceof HttpServletResponse
            && extContext instanceof ServletContext )
        {
            HttpServletRequest httpRequest = ( HttpServletRequest ) request;
            HttpServletResponse httpResponse = ( HttpServletResponse ) response;
            
            //
            // If we're coming in on a forwarded request from this NavigationHandler, bail out; we don't want to
            // forward again.
            //
            if ( httpRequest.getAttribute( ALREADY_FORWARDED_ATTR ) != null )
            {
                httpRequest.removeAttribute( ALREADY_FORWARDED_ATTR );
                return;
            }
            
            try
            {
                //
                // We only forward to Page Flow actions if there's a page flow appropriate for this request.
                //
                ServletContext servletContext = ( ServletContext ) extContext;
                FlowControllerFactory fcFactory = FlowControllerFactory.get( servletContext );
                PageFlowController pfc = fcFactory.getPageFlowForRequest( new RequestContext( httpRequest, httpResponse ) );
    
                if ( pfc != null )
                {
                    if ( outcome != null )
                    {
                        String actionURI = outcome + PageFlowConstants.ACTION_EXTENSION;
                        
                        if ( _log.isDebugEnabled() )
                        {
                            _log.debug( "Forwarding to " + actionURI );
                        }
                        
                        context.responseComplete();
                        httpRequest.setAttribute( ALREADY_FORWARDED_ATTR, actionURI );
                        
                        try
                        {
                            httpRequest.getRequestDispatcher( actionURI ).forward( httpRequest, httpResponse );
                        }
                        catch ( IOException e )
                        {
                            _log.error( "Could not forward to " + actionURI, e );
                        }
                        catch ( ServletException e )
                        {
                            _log.error( "Could not forward to " + actionURI, e.getRootCause() );
                        }
                    }
    
                    return;
                }
            }
            catch ( InstantiationException e )
            {
                _log.error( "Could not instantiate PageFlowController for request " + httpRequest.getRequestURI(), e );
                return;
            }
            catch ( IllegalAccessException e )
            {
                _log.error( "Could not instantiate PageFlowController for request " + httpRequest.getRequestURI(), e );
                return;
            }
        }

        // Fall back to base JSF navigation.
        _baseHandler.handleNavigation( context, fromAction, outcome );
    }
}
