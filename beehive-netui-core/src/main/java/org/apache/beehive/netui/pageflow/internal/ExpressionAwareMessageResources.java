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

import org.apache.struts.util.MessageResources;
import org.apache.struts.Globals;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Locale;

/**
 * Message resources extension that knows how to evaluate JSP 2.0-style expressions (set in the message keys) that are
 * prefixed with a special indicator string.
 */ 
public class ExpressionAwareMessageResources
        extends MessageResources
{
    private static final Logger _log = Logger.getInstance( ExpressionAwareMessageResources.class );
    
    private MessageResources _delegate;
    private Object _formBean;
    private transient HttpServletRequest _request;
    private transient ServletContext _servletContext;

    public ExpressionAwareMessageResources( Object formBean, HttpServletRequest request, ServletContext servletContext )
    {
        super( defaultFactory, null, true );
        _formBean = formBean;
        _request = request;
        _servletContext = servletContext;
        
        // There is no message resources delegate.  Look in the "global" Struts location in the Servlet context.
        _delegate = (MessageResources) servletContext.getAttribute(Globals.MESSAGES_KEY);

    }
    
    public ExpressionAwareMessageResources( MessageResources delegate, Object formBean, HttpServletRequest request,
                                            ServletContext servletContext )
    {
        super( delegate.getFactory(), delegate.getConfig(), delegate.getReturnNull() );
        _delegate = delegate;
        _formBean = formBean;
        _request = request;
        _servletContext = servletContext;
    }

    public String getMessage( Locale locale, String key )
    {
        if ( key.startsWith( InternalConstants.MESSAGE_IS_EXPRESSION_PREFIX ) )
        {
            String messageExpr = key.substring( InternalConstants.MESSAGE_IS_EXPRESSION_PREFIX_LENGTH );
        
            try
            {
                return InternalExpressionUtils.evaluateMessage( messageExpr, _formBean, _request, _servletContext );
            }
            catch ( Exception e )
            {
                _log.error( "Could not evaluate message expression " + messageExpr, e );
            }
                
            return null;
        }
        
        return _delegate != null ? _delegate.getMessage( locale, key ) : null;
    }
    
    public String getMessage( Locale locale, String key, Object args[] )
    {
        for ( int i = 0; i < args.length; i++ )
        {
            Object arg = args[i];
            
            if ( arg instanceof String )
            {
                String argStr = ( String ) arg;
                
                if ( argStr.startsWith( InternalConstants.MESSAGE_IS_EXPRESSION_PREFIX ) )
                {
                    String argExpr = argStr.substring( InternalConstants.MESSAGE_IS_EXPRESSION_PREFIX_LENGTH );
                
                    try
                    {
                        args[i] =
                            InternalExpressionUtils.evaluateMessage( argExpr, _formBean, _request, _servletContext );
                    }
                    catch ( Exception e )
                    {
                        _log.error( "Could not evaluate message arg expression " + argExpr, e );
                    }
                }
            }
        }
        
        return super.getMessage( locale, key, args );
    }

    protected void setFormBean( Object formBean )
    {
        _formBean = formBean;
    }

    public static void update( MessageResources resources, Object formBean )
    {
        if ( resources instanceof ExpressionAwareMessageResources )
        {
            ( ( ExpressionAwareMessageResources ) resources ).setFormBean( formBean );
        }
    }
}


