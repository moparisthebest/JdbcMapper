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
import javax.servlet.http.HttpServletResponse;

/**
 * Interface for controller classes that resolve actions to URIs.
 * 
 * @deprecated This is a legacy interface that is implemented by {@link PageFlowController}, but unused in the framework.
 */ 
public interface ActionResolver
{
    /**
     * Get the URI for addressing this object.
     * 
     * @return a String that is the URI, relative to the webapp root, for addressing this object.
     */
    public String getURI();
    
    /**
     * Get the Struts module path associated with this ActionResolver.
     */ 
    public String getModulePath();

    /**
     * Called on this object for non-lookup (refresh) requests.
     */ 
    public void refresh( HttpServletRequest request, HttpServletResponse response );
    
    /**
     * Tell whether this ActionResolver is a {@link PageFlowController}.
     * 
     * @return <code>true</code> if this is a {@link PageFlowController}.
     */ 
    public boolean isPageFlow();
}
