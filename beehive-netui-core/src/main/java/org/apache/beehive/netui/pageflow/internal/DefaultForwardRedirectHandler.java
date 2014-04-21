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

import org.apache.beehive.netui.pageflow.handler.ForwardRedirectHandler;
import org.apache.beehive.netui.pageflow.handler.FlowControllerHandlerContext;
import org.apache.beehive.netui.pageflow.PageFlowStack;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import java.io.IOException;

import org.apache.struts.upload.MultipartRequestWrapper;


/**
 * Handler for redirects and server forwards.
 */
public class DefaultForwardRedirectHandler
        extends DefaultHandler
        implements ForwardRedirectHandler
{
    private static final Logger _log = Logger.getInstance( DefaultForwardRedirectHandler.class );
    
    
    public DefaultForwardRedirectHandler( ServletContext servletContext )
    {
        init( null, null, servletContext );
    }
    
    public void redirect( FlowControllerHandlerContext context, String uri )
        throws IOException, ServletException
    {
        assert context.getResponse() instanceof HttpServletResponse :
                context.getResponse().getClass().getName() + " does not implement HttpServletResponse";
        HttpServletResponse response = ( HttpServletResponse ) context.getResponse();
        response.sendRedirect( response.encodeRedirectURL( uri ) );
    }

    public void forward( FlowControllerHandlerContext context, String uri )
        throws IOException, ServletException
    {
        LegacySettings settings = LegacySettings.get( getServletContext() );
        assert context.getRequest() instanceof HttpServletRequest :
                context.getRequest().getClass().getName() + " does not implement HttpServletRequest";
        assert context.getResponse() instanceof HttpServletResponse :
                context.getResponse().getClass().getName() + " does not implement HttpServletResponse";
        HttpServletRequest request = ( HttpServletRequest ) context.getRequest();
        HttpServletResponse response = ( HttpServletResponse ) context.getResponse();
        int requestCount = PageFlowRequestWrapper.get( request ).getForwardedRequestCount();
        
        //
        // See if we've exceeded the maximum number of forwards.
        //
        int forwardOverflowCount = settings.getForwardOverflowCount();
        if ( requestCount > forwardOverflowCount )
        {
            InternalUtils.sendDevTimeError( "PageFlow_Forward_Overflow", null,
                                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request, response,
                                            getServletContext(),
                                            new Object[]{ new Integer( forwardOverflowCount ), uri } );
            return;
        }
        
        //
        // See if we've exceeded the maximum nesting depth.
        //
        PageFlowStack pfStack = PageFlowStack.get( request, getServletContext(), false );
        
        int nestingOverflowCount = settings.getNestingOverflowCount();
        if ( pfStack != null && pfStack.size() > nestingOverflowCount )
        {
            Object[] args = new Object[]{ new Integer( pfStack.size() ), new Integer( nestingOverflowCount ) };
            InternalUtils.sendDevTimeError( "PageFlow_Nesting_Overflow", null,
                                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request, response,
                                            getServletContext(), args );
            return;
        }
            
        //
        // We're OK -- do the forward.
        //
        PageFlowRequestWrapper.get( request ).setForwardedRequestCount( requestCount + 1 );
        
        // Unwrap the multipart request, if there is one.
        if ( request instanceof MultipartRequestWrapper )
        {
            request = ( ( MultipartRequestWrapper ) request ).getRequest();
        }

        //
        // Note that we get a RequestDispatcher from the request, not from the ServletContext.
        // The request may be a ScopedRequest, which provides a special RequestDispatcher.
        //
        RequestDispatcher rd = request.getRequestDispatcher( uri );

        if ( rd == null )
        {
            if ( _log.isErrorEnabled() )
            {
                _log.error( "Could not get requestDispatcher for URI " + uri );
            }
            
            response.sendError( HttpServletResponse.SC_NOT_FOUND );
            return;
        }
    
        rd.forward( request, response );
    }
}
