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
package org.apache.beehive.netui.core.factory;

import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.ServletContext;
import java.io.Serializable;

/**
 * Base class for ServletContext-scoped factories.
 */ 
public abstract class Factory
        implements Serializable
{
    private transient ServletContext _servletContext;
    private FactoryConfig _config;

    /**
     * Called after this factory has been created and initialized.
     */ 
    protected void onCreate()
    {
    }
    
    void init( ServletContext servletContext, FactoryConfig config )
    {
        _servletContext = servletContext;
        _config = config;
    }
    
    /**
     * Called to reinitialize this instance, most importantly after it has been serialized/deserialized.
     * 
     * @param servletContext the current ServletContext.
     */ 
    protected void reinit( ServletContext servletContext )
    {
        _servletContext = servletContext;
    }
    
    /**
     * Get the current ServletContext.
     */ 
    protected ServletContext getServletContext()
    {
        return _servletContext;
    }

    /**
     * Get the configuration object (containing custom properties) that is associated with this factory.
     */ 
    protected FactoryConfig getConfig()
    {
        return _config;
    }
}
