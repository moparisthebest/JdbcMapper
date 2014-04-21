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

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Base class for exceptions related to Page Flow managed objects.
 * 
 * @see PageFlowManagedObject
 */ 
public abstract class PageFlowManagedObjectException
        extends RuntimeException
{
    private PageFlowManagedObject _managedObject;
    private String _messageKeyPrefix;
    
    
    protected PageFlowManagedObjectException( PageFlowManagedObject object )
    {
        super();
        init( object );
    }
    
    protected PageFlowManagedObjectException( PageFlowManagedObject object, Throwable rootCause )
    {
        super( rootCause );
        init( object );
    }
    
    protected void init( PageFlowManagedObject object )
    {
        _managedObject = object;
        
        String className = getClass().getName();
        int lastDot = className.lastIndexOf( '.' );
        assert lastDot != -1;
        _messageKeyPrefix = "PageFlow_" + className.substring( lastDot + 1 );
    }
    
    /**
     * Get the related PageFlowManagedObject.
     * 
     * @return the {@link PageFlowManagedObject} associated with this exception.
     */ 
    public PageFlowManagedObject getManagedObject()
    {
        return _managedObject;
    }

    /**
     * Set the related PageFlowManagedObject.
     * 
     * @param managedObject the {@link PageFlowManagedObject} associated with this exception.
     */ 
    protected void setManagedObject( PageFlowManagedObject managedObject )
    {
        _managedObject = managedObject;
    }

    /**
     * Handle the error by writing a message to the response.
     * 
     * @param request the current HttpServletRequest
     * @param response the current HttpServletResponse
     */    
    void sendError( ServletRequest request, ServletResponse response, ServletContext servletContext,
                    int productionTimeErrorCode )
        throws IOException
    {
        InternalUtils.sendDevTimeError( _messageKeyPrefix, getMessageArgs(), null, productionTimeErrorCode,
                                        request, response, servletContext );
    }
    
    /**
     * Handle the error by writing a message to the response.
     * 
     * @param request the current HttpServletRequest
     * @param response the current HttpServletResponse
     */    
    public void sendError( ServletRequest request, ServletResponse response )
        throws IOException
    {
        InternalUtils.sendError( _messageKeyPrefix, getMessageArgs(), request, response, null,
                                 InternalUtils.avoidDirectResponseOutput( request ) );
    }
    
    /**
     * Print a formatted message.
     * 
     * @param writer a writer to which to print the formatted message.
     */ 
    public void printError( PrintWriter writer )
    {
        writer.println( Bundle.getString( _messageKeyPrefix + "_Page", getMessageArgs() ) );
    }

    public String getLocalizedMessage()
    {
        return Bundle.getString( _messageKeyPrefix + "_Message", getMessageArgs() );
    }
    
    public String getMessage()
    {
        InternalStringBuilder buf = new InternalStringBuilder();
        String[] parts = getMessageParts();
        Object[] args = getMessageArgs();

        assert parts.length > args.length : parts.length + ", " + args.length;
        
        for ( int i = 0; i < parts.length; ++i )
        {
            buf.append( parts[i] );
            
            if ( i < args.length )
            {
                buf.append( args[i] );
            }
        }
        
        return buf.toString();
    }
    
    protected abstract Object[] getMessageArgs();
    
    protected abstract String[] getMessageParts();
}
