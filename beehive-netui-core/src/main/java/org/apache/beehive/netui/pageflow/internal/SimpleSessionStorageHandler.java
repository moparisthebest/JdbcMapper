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

import org.apache.beehive.netui.pageflow.handler.StorageHandler;
import org.apache.beehive.netui.pageflow.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.Collections;

/**
 * This storage handler simply puts/gets attributes in the session.  It does not do anything to support multiple
 * concurrent forwarded requests that are simultaneously modifying session data.
 * 
 * @see DeferredSessionStorageHandler
 */
public class SimpleSessionStorageHandler
        extends DefaultHandler
        implements StorageHandler
{
    public SimpleSessionStorageHandler( ServletContext servletContext )
    {
        init( null, null, servletContext );
    }

    public void setAttribute( RequestContext context, String attributeName, Object value )
    {
        ( ( HttpServletRequest ) context.getRequest() ).getSession().setAttribute( attributeName, value );
    }

    public void removeAttribute( RequestContext context, String attributeName )
    {
        HttpSession session = ( ( HttpServletRequest ) context.getRequest() ).getSession( false );
        if ( session != null ) session.removeAttribute( attributeName );
    }

    public Object getAttribute( RequestContext context, String attributeName )
    {
        HttpSession session = ( ( HttpServletRequest ) context.getRequest() ).getSession( false );
        return session != null ? session.getAttribute( attributeName ) : null;
    }

    public void ensureFailover( RequestContext context, String attributeName, Object value )
    {
        HttpServletRequest request = ( HttpServletRequest ) context.getRequest();
        AdapterManager.getServletContainerAdapter( getServletContext() ).ensureFailover( attributeName, value, request );
    }

    public boolean allowBindingEvent( Object event )
    {
        return true;
    }

    public void applyChanges(RequestContext context)
    {
    }
    
    public void dropChanges(RequestContext context)
    {
    }

    public Enumeration getAttributeNames(RequestContext context)
    {
        HttpSession session = ((HttpServletRequest) context.getRequest()).getSession(false);
        if (session != null) {
            return session.getAttributeNames();
        } else {
            return Collections.enumeration(Collections.emptyList());
        }
    }
}
