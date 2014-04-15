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
package mockportal;

import org.apache.beehive.netui.core.urls.MutableURI;
import org.apache.beehive.netui.core.urls.URLType;
import org.apache.beehive.netui.pageflow.PageFlowConstants;
import org.apache.beehive.netui.pageflow.internal.DefaultURLRewriter;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.LinkedHashMap;

public class MockPortalUrlRewriter extends DefaultURLRewriter
{
    public void rewriteURL( ServletContext servletContext, ServletRequest request,
                            ServletResponse response, MutableURI url, URLType type, boolean needsToBeSecure )
    {
        if ( url != null && url.getPath().startsWith( "/" ) )
        {
            String path = url.getPath();

            if ( path.endsWith( PageFlowConstants.ACTION_EXTENSION ) )
            {
                String portletID = ( String ) request.getAttribute( "portletID" );
                
                if ( portletID != null )
                {
                    //
                    // If there's a jpfScopeID param on the URL, then return the URL untouched.
                    //
                    if ( url.getParameter( ScopedServletUtils.SCOPE_ID_PARAM ) != null )
                    {
                        return;
                    }

                    Map params = url.getParameters();
                    url.setQuery( null );
                    makeActionURL( servletContext, request, url );

                    if ( params != null )
                    {
                        Map scopedParams = new LinkedHashMap(); // try to preserve order for the test

                        for ( Iterator i = params.entrySet().iterator(); i.hasNext(); )
                        {
                            Map.Entry entry = ( Map.Entry ) i.next();

                            String name = ( String ) entry.getKey();
                            String scopedName = getNamePrefix( servletContext, request, name ) + name;
                            scopedParams.put( scopedName, entry.getValue() );
                        }

                        url.addParameters( scopedParams, true );
                    }

                    return;
                }
            }
        }

        super.rewriteURL( servletContext, request, response, url, type, needsToBeSecure );
    }

    /**
     * Return the form action converted into an action mapping path.
     */
    public String getActionMappingName( ServletContext servletContext, ServletRequest request, String action )
    {
        return InternalUtils.getActionMappingName( action );
    }

    
    /**
     * Return a scoping prefix for the tag name
     */
    public String getNamePrefix( ServletContext servletContext, ServletRequest request, String name )
    {
        String portletID = ( String ) request.getAttribute( "portletID" );

        if ( portletID != null )
        {
            return portletID;
        }
        
        return "";
    }
        
     private void makeActionURL( ServletContext servletContext, ServletRequest request, MutableURI url )
     {
         String path = url.getPath();
         int actionStart = path.lastIndexOf( '/' );
         int actionEnd = path.lastIndexOf( PageFlowConstants.ACTION_EXTENSION );
         String action = path.substring( actionStart + 1, actionEnd );
         url.setPath( ( String ) request.getAttribute( "repostURL" ) );
         url.addParameter( getNamePrefix( servletContext, request, "altAction" ) + "altAction", action, true );
         url.addParameter( getNamePrefix( servletContext, request, "_submit") + "_submit", "true", true );
     }
        
}

