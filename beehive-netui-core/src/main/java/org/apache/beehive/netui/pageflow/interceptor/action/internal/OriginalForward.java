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
package org.apache.beehive.netui.pageflow.interceptor.action.internal;

import org.apache.beehive.netui.pageflow.interceptor.action.InterceptorForward;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Iterator;
import java.io.Serializable;


public class OriginalForward
        extends InterceptorForward
{
    private static final Logger _log = Logger.getInstance( OriginalForward.class );
    
    private Map _savedAttrs;


    private static abstract class AttributeWrapper
            implements Serializable
    {
        public abstract Object get();
    }
    
    private static final class TransientAttributeWrapper
            extends AttributeWrapper
    {
        private transient Object _object;

        public TransientAttributeWrapper( Object object )
        {
            _object = object;
        }

        public Object get()
        {
            return _object;
        }
    }
    
    private static final class SerializableAttributeWrapper
            extends AttributeWrapper
    {
        private Object _object;

        public SerializableAttributeWrapper( Object object )
        {
            _object = object;
        }

        public Object get()
        {
            return _object;
        }
    }
    
    public OriginalForward( HttpServletRequest request )
    {
        super( request );
        saveRequestAttrs( request );
        
        // By default, we do restore the original query string.
        setRestoreQueryString( true );
    }

    private void saveRequestAttrs( HttpServletRequest request )
    {
        _savedAttrs = new HashMap();
    
        for ( Enumeration e = request.getAttributeNames(); e.hasMoreElements(); )
        {
            String attrName = ( String ) e.nextElement();
            Object attrVal = request.getAttribute( attrName ); 
        
            if ( attrVal instanceof Serializable )
            {
                _savedAttrs.put( attrName, new SerializableAttributeWrapper( attrVal ) );
            }
            else
            {
                _savedAttrs.put( attrName, new TransientAttributeWrapper( attrVal ) );
            }
        } 
        
        setQueryString( request.getQueryString() );
    }

    public void rehydrateRequest( HttpServletRequest request )
    {
        //
        // Restore the request attributes.
        //
        if ( _savedAttrs != null )
        {
            for ( Iterator i = _savedAttrs.entrySet().iterator(); i.hasNext(); )
            {
                Map.Entry entry = ( Map.Entry ) i.next();
                String attrName = ( String ) entry.getKey();
                if ( request.getAttribute( attrName ) == null )
                {
                    Object value = ( ( AttributeWrapper ) entry.getValue() ).get();
                    if ( value != null ) request.setAttribute( attrName, value );
                }
            }
        }
        
        //
        // Restore the query string.
        //
        if ( doesRestoreQueryString() )
        {
            String queryString = getQueryString();
            
            if ( queryString != null && queryString.length() > 0 )
            {
                assert queryString.charAt( 0 ) == '?';
                String path = getPath();
                if ( path.indexOf( '?' ) != -1 )
                {
                    path += '&' + queryString.substring( 1 );
                }
                else
                {
                    path += queryString;
                }
                
                setPath( path );
            }
        }
    }
}
