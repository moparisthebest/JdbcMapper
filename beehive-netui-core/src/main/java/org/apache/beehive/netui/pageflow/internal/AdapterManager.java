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

import org.apache.beehive.netui.pageflow.DefaultServletContainerAdapter;
import org.apache.beehive.netui.pageflow.ServletContainerAdapter;
import org.apache.beehive.netui.pageflow.adapter.Adapter;
import org.apache.beehive.netui.pageflow.adapter.AdapterContext;
import org.apache.beehive.netui.util.internal.DiscoveryUtils;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.ServletContext;


/**
 * @exclude
 */
public class AdapterManager
{
    private static final Logger _log = Logger.getInstance( AdapterManager.class );
    
    private static final String SERVLET_CONTAINER_ADAPTER_ATTR = InternalConstants.ATTR_PREFIX + "servletAdapter";
    private static final String SERVLET_CONTAINER_ADAPTER_PROP = "beehive.servletcontaineradapter";

    public static ServletContainerAdapter getServletContainerAdapter( ServletContext servletContext )
    {
        ServletContainerAdapter adapter =
                ( ServletContainerAdapter ) servletContext.getAttribute( SERVLET_CONTAINER_ADAPTER_ATTR );
        
        if ( adapter == null )
        {
            if ( _log.isErrorEnabled() )
            {
                _log.error( "ServletContainerAdapter manager not initialized correctly." );
            }
            
            //
            // We can initialize it now, but it's not good because many requests could conceivably be in this
            // code at the same time.
            //
            return initServletContext( servletContext );
        }
        
        return adapter;
    }
    
    public static ServletContainerAdapter initServletContext( ServletContext servletContext )
    {
        ServletContainerAdapter servletContainerAdapter = createServletContainerAdapter( servletContext );
        servletContext.setAttribute( SERVLET_CONTAINER_ADAPTER_ATTR, servletContainerAdapter );
        return servletContainerAdapter;
    }

    // TODO: this method could move to a more general place.
    private static Adapter tryAdapter( Class adapterClass, Object externalContext )
    {
        try
        {
            Adapter sa = ( Adapter ) adapterClass.newInstance();

            try
            {
                AdapterContext context = new AdapterContext( externalContext );
                
                if ( sa.accept( context ) )
                {
                    _log.info( "Adapter " + adapterClass.getName() + " accepted." );
                    sa.setContext( context );
                    return sa;
                }
                else
                {
                    _log.info( "Adapter " + adapterClass.getName() + " is present but did not accept." );
                }
            }
            catch ( Exception e )
            {
                _log.error( adapterClass.getName() + ".accept() threw an exception.", e );
            }
            catch ( LinkageError e )
            {
                _log.error( adapterClass.getName() + ".accept() caused a linkage error and may be out of date.", e );
            }
        }
        catch ( InstantiationException e )
        {
            _log.error( "Could not create instance of Adapter class " + adapterClass.getName(), e );
        }
        catch ( IllegalAccessException e )
        {
            _log.error( "Could not create instance of Adapter class " + adapterClass.getName(), e );
        }
        catch ( Exception e )
        {
            _log.error( "Error creating instance of Adapter class " + adapterClass.getName(), e );
        }

        return null;
    }

    private static ServletContainerAdapter createServletContainerAdapter( ServletContext servletContext )
    {
        String adapterClassName = System.getProperty( SERVLET_CONTAINER_ADAPTER_PROP );

        if ( adapterClassName != null )
        {
            Class adapterClass =
                    DiscoveryUtils.loadImplementorClass( adapterClassName, ServletContainerAdapter.class );

            if ( adapterClass != null )
            {
                ServletContainerAdapter sa = 
                        ( ServletContainerAdapter ) tryAdapter( adapterClass, servletContext );
                if ( sa != null ) return sa;
            }
        }
        
        Class[] classes = DiscoveryUtils.getImplementorClasses( ServletContainerAdapter.class );
        
        for ( int i = 0; i < classes.length; i++ )
        {
            ServletContainerAdapter sa = ( ServletContainerAdapter ) tryAdapter( classes[i], servletContext );
            if ( sa != null ) return sa;
        }
        
        _log.info( "No ServletContainerAdapter specified or discovered; using " + DefaultServletContainerAdapter.class );
        ServletContainerAdapter sa =
                new DefaultServletContainerAdapter()
                {
                    public boolean accept( AdapterContext context )
                    {
                        return true;
                    }
                };
        sa.setContext( new AdapterContext( servletContext ) );
        return sa;
    }
}
