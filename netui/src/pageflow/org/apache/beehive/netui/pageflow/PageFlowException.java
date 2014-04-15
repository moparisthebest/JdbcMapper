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

import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Base class for PageFlow-related Exceptions.
 */ 
public abstract class PageFlowException
        extends PageFlowManagedObjectException
{
    private String _actionName;
    
    
    protected PageFlowException( String actionName, FlowController fc )
    {
        super( fc );
        init( actionName );
    }
    
    protected PageFlowException( String actionName, FlowController fc, Throwable cause )
    {
        super( fc, cause );
        init( actionName );
    }
    
    protected void init( String actionName )
    {
        _actionName = actionName != null && actionName.length() > 0 && actionName.charAt(0) == '/'
                      ? actionName.substring( 1 )
                      : actionName;
    }
    
    /**
     * Get the related FlowController.
     * 
     * @return the {@link FlowController} associated with this exception.
     */ 
    public FlowController getFlowController()
    {
        return ( FlowController ) getManagedObject();
    }
    
    /**
     * Get the name of the related FlowController.
     * 
     * @return the class name of the {@link FlowController} associated with this exception.
     */ 
    public String getFlowControllerURI()
    {
        FlowController flowController = getFlowController();
        return flowController != null ? flowController.getDisplayName() : null;
    }
    
    /**
     * Get the name of the action associated with this exception.
     * 
     * @return a String that is the name of the action associated with this exception.
     */ 
    public String getActionName()
    {
        return _actionName;
    }
    
    /**
     * Tell whether the root cause may be session expiration in cases where the requested session ID is different than
     * the actual session ID; if <code>true</code>, then a {@link SessionExpiredException} will be thrown instead of
     * this one in these situations.
     */ 
    public abstract boolean causeMayBeSessionExpiration();
}
