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

import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.config.ConfigUtil;
import org.apache.beehive.netui.util.config.bean.PageFlowConfig;
import org.apache.beehive.netui.pageflow.PageFlowActionServlet;
import org.apache.beehive.netui.pageflow.PageFlowContextListener;
import org.apache.beehive.netui.pageflow.PageFlowConstants;

import javax.servlet.ServletContext;
import java.io.Serializable;

public class LegacySettings
        implements Serializable, PageFlowConstants
{
    private static final Logger _log = Logger.getInstance( LegacySettings.class );

    private static final String CONTEXT_ATTR = InternalConstants.ATTR_PREFIX + "_cache";

    public static final int serialVersionUID = 1;

    private boolean _secureForwards = false;
    private int _forwardOverflowCount;
    private int _nestingOverflowCount;


    public static LegacySettings get( ServletContext servletContext )
    {
        LegacySettings ls = ( LegacySettings ) servletContext.getAttribute( CONTEXT_ATTR );

        if ( ls == null )
        {
            if ( _log.isErrorEnabled() )
            {
                _log.error( "Page Flow ServletContext cache not initialized; either "
                            + PageFlowActionServlet.class.getName() + " must be the Struts action servlet, or "
                            + PageFlowContextListener.class.getName() + " must be registered as a listener in web.xml." );
            }

            //
            // We can initialize it now, but it's not good because many requests could conceivably be in this
            // code at the same time.
            //
            return init( servletContext );
        }

        return ls;
    }

    public static LegacySettings init( ServletContext servletContext )
    {
        LegacySettings cache = new LegacySettings( servletContext );
        servletContext.setAttribute( CONTEXT_ATTR, cache );
        return cache;
    }

    private void loadLegacySettings( ServletContext servletContext )
    {
        PageFlowConfig pageflowConfig = ConfigUtil.getConfig().getPageFlowConfig();
        assert pageflowConfig != null : "Received an invalid PageFlowConfig object";

        Integer forwardOverflowCount =
                loadLegacyParam( FORWARD_OVERFLOW_COUNT_PARAM, servletContext, "max-forwards-per-request" );
        if ( forwardOverflowCount != null )
        {
            _forwardOverflowCount = forwardOverflowCount.intValue();
        }
        else
        {
            // Why can't we read the default value from the XmlObjext?
            _forwardOverflowCount = pageflowConfig.getMaxForwardsPerRequest();
        }

        Integer nestingOverflowCount =
                loadLegacyParam( NESTING_OVERFLOW_COUNT_PARAM, servletContext, "max-nesting-stack-depth" );
        if ( nestingOverflowCount != null )
        {
            _nestingOverflowCount = nestingOverflowCount.intValue();
        }
        else
        {
            // Why can't we read the default value from the XmlObjext?
            _nestingOverflowCount = pageflowConfig.getMaxNestingStackDepth();
        }

        String doSecureForwards = servletContext.getInitParameter( SECURE_FORWARDS_PARAM );

        if ( doSecureForwards != null )
        {
            _log.warn( "Servlet context-param " + SECURE_FORWARDS_PARAM +
                       " is deprecated; use the ensure-secure-forwards element within pageflow-config in "
                       + InternalConstants.NETUI_CONFIG_PATH );
            _secureForwards = Boolean.valueOf( doSecureForwards ).booleanValue();
        }
        else
        {
            _secureForwards = pageflowConfig.isEnsureSecureForwards();
        }
        
        
    }
    private LegacySettings( ServletContext servletContext )
    {
        //
        // Try loading some settings (max-forwards-per-requst, max-nesting-stack-depth, ensure-secure-forwards) from
        // the deprecated locations first, then fall back to netui-config.xml.
        //
        loadLegacySettings( servletContext );
        
    }
    
    public boolean shouldDoSecureForwards()
    {
        return _secureForwards;
    }
    
    public int getForwardOverflowCount()
    {
        return _forwardOverflowCount;
    }
    
    public int getNestingOverflowCount()
    {
        return _nestingOverflowCount;
    }
    
    private static Integer loadLegacyParam( String paramName, ServletContext servletContext, String configElementName )
    {
        String strVal = servletContext.getInitParameter( paramName );
        
        if ( strVal != null )
        {
            _log.warn( "Servlet context-param " + paramName + "is deprecated; use the " + configElementName
                       + " element within pageflow-config in " + InternalConstants.NETUI_CONFIG_PATH );
            
            try
            {
                return Integer.valueOf( strVal );
            }
            catch ( NumberFormatException e )
            {
                _log.error( "Could not parse integer value from context-param " + paramName + '.' );
            }
        }
        
        return null;
    }
}
