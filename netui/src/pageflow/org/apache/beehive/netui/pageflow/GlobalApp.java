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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Base class for user-defined global state and fallback controller logic.  A specific instance of
 * this class (based on the Global.app/Global.java source file in the "global" package) is created and stored in
 * the user session before any {@link PageFlowController} is created, and unless it is explicitly
 * removed from the session, it is not freed until the session is invalidated.
 * <p>
 * Actions that are not handled by the current {@link PageFlowController} "bubble" up to the
 * current GlobalApp instance, which gets a chance to handle them in user-defined action methods.
 * Uncaught exceptions also bubble through the GlobalApp instance, which has a chance to catch
 * them.
 * <p>
 * Data in the current GlobalApp instance can be accessed by databinding tags using the <code>globalApp</code> scope.
 * 
 * @deprecated Use a {@link SharedFlowController} instead.
 * @see PageFlowController
 */
public class GlobalApp
        extends SharedFlowController
{
    /**
     * Get the URI for the webapp.
     * @return "/".
     */
    public String getURI()
    {
        return "/";
    }
    
    public String getDisplayName()
    {
        return GLOBALAPP_SOURCEFILE_NAME;
    }
    
    /**
     * @deprecated Only page flows ({@link PageFlowController}s) can be nested.
     */ 
    public boolean isNestable()
    {
        return false;
    }

    /**
     * Store information about the most recent action invocation.
     */ 
    void savePreviousActionInfo( ActionForm form, HttpServletRequest request, ActionMapping mapping,
                                 ServletContext servletContext )
    {
        // Do nothing here -- Global.app actions do not 'count' as actions in the current page flow, unlike shared
        // flow actions.  This is basically deprecated behavior.
    }
}