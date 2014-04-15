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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * An ActionResolver that submits actions to a Struts module.
 * 
 * @deprecated This class will be removed without replacement in the next version.
 */
public class StrutsModule
        implements ActionResolver
{
    private String _modulePath;

    /**
     * Construct on the given Struts module path.
     * 
     * @param modulePath the Struts module path.
     */ 
    public StrutsModule( String modulePath )
    {
        // The path for the root module is "".
        assert modulePath.length() == 0 || modulePath.startsWith( "/" ) : modulePath;
        _modulePath = modulePath;
    }

    /**
     * Get the URI for this Struts module.
     * 
     * @return a String URI that is the Struts module path, relative to the web application root.
     */ 
    public String getURI()
    {
        return _modulePath;
    }

    public String getModulePath()
    {
        return _modulePath;
    }
    
    /**
     * Tell whether this is a {@link PageFlowController}.
     * 
     * @return <code>false</code>.
     */ 
    public boolean isPageFlow()
    {
        return false;
    }
    
    public void refresh( HttpServletRequest request, HttpServletResponse response )
    {
    }
}
