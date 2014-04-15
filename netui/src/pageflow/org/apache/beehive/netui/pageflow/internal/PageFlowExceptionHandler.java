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

import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.util.ModuleException;
import org.apache.struts.Globals;
import org.apache.beehive.netui.pageflow.ExpressionMessage;
import org.apache.beehive.netui.pageflow.config.PageFlowExceptionConfig;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

public class PageFlowExceptionHandler
        extends ExceptionHandler
{
    private static final Logger _log = Logger.getInstance( PageFlowExceptionHandler.class );
    
    public ActionForward execute( Exception ex, ExceptionConfig ec, ActionMapping mapping, ActionForm formInstance,
                                  HttpServletRequest request, HttpServletResponse response )
            throws ServletException
    {
        ActionForward forward = null;
        ActionMessage error = null;
        String property = null;
        String path = ec.getPath();

        // Build the forward from the exception mapping if it exists or from the form input
        forward = path != null ? new ActionForward( path ) : mapping.getInputForward();
        
        PageFlowExceptionConfig pfec = ec instanceof PageFlowExceptionConfig ? (PageFlowExceptionConfig) ec : null;
        if (pfec != null && pfec.isPathContextRelative()) {
            forward.setContextRelative(true);
        }
        
        // Figure out the error
        if ( ex instanceof ModuleException )
        {
            error = ( ( ModuleException ) ex ).getError();
            property = ( ( ModuleException ) ex ).getProperty();
        }
        else
        {
            if ( pfec != null )
            {
                String expressionMessage = pfec.getDefaultMessage();
                if ( expressionMessage != null )
                {
                    error = new ExpressionMessage( expressionMessage, new Object[]{ ex.getMessage() } );
                }
            }
            
            if ( error == null ) error = new ActionMessage( ec.getKey(), ex.getMessage() );
            property = ec.getKey();
        }

        if ( _log.isDebugEnabled() ) _log.debug( "Handling exception", ex );

        // Store the exception
        request.setAttribute( Globals.EXCEPTION_KEY, ex );
        storeException( request, property, error, forward, ec.getScope() );

        return forward;
    }
    
    // TODO: this does nothing different than the Struts 1.2 version of the same method.  Remove this when we don't 
    // support Struts 1.1 anymore (or when Struts 1.1 support goes into a legacy version).
    protected void storeException( HttpServletRequest request, String property, ActionMessage error,
                                   ActionForward forward, String scope )
    {
        ActionMessages errors = new ActionMessages();
        errors.add( property, error );

        if ( "request".equals( scope ) )
        {
            request.setAttribute( Globals.ERROR_KEY, errors );
        }
        else
        {
            request.getSession().setAttribute( Globals.ERROR_KEY, errors );
        }
    }
}
