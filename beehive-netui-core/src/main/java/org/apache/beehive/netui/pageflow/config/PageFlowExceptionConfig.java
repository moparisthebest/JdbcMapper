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
package org.apache.beehive.netui.pageflow.config;

import org.apache.struts.config.ExceptionConfig;
import org.apache.beehive.netui.pageflow.internal.PageFlowExceptionHandler;


/**
 * Class to handle our extensions to the Struts &lt;exception&gt; element.
 */
public class PageFlowExceptionConfig extends ExceptionConfig
{
    private boolean _isHandlerMethod;
    private String _defaultMessage;
    private boolean _isPathContextRelative;
    private boolean _readonly;
    
    private static final String DEFAULT_HANDLER_CLASS = PageFlowExceptionHandler.class.getName();
    

    public PageFlowExceptionConfig()
    {
        // Our default handler is PageFlowExceptionHandler
        super.setHandler( DEFAULT_HANDLER_CLASS );
    }
    
    public boolean isHandlerMethod()
    {
        return _isHandlerMethod;
    }

    public void setIsHandlerMethod( boolean handlerMethod )
    {
        _isHandlerMethod = handlerMethod;
    }

    public String getDefaultMessage()
    {
        return _defaultMessage;
    }

    public void setDefaultMessage( String defaultMessage )
    {
        _defaultMessage = defaultMessage;
    }

    public boolean isPathContextRelative()
    {
        return _isPathContextRelative;
    }

    public void setIsPathContextRelative( boolean pathContextRelative )
    {
        _isPathContextRelative = pathContextRelative;
    }

    public boolean isReadonly()
    {
        return _readonly;
    }

    public void setReadonly( boolean readonly )
    {
        _readonly = readonly;
    }

    /**
     * Get a prefix directory path that all Forward local paths should be relative to.  By default this is
     * <code>null</code>, which means that there is no forced prefix path.
     */
    public String getLocalPathsRelativeTo()
    {
        return null;
    }
}
