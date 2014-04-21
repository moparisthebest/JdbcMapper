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

import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.config.ActionConfig;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;

import javax.servlet.ServletContext;

/**
 * A Struts ExceptionConfig object that delegates to an ExceptionConfig in a different module.
 */
public class DelegatingExceptionConfig extends PageFlowExceptionConfig
{
    private ExceptionConfig _delegate;
    private String _delegateModulePath;
    private String _delegateActionPath;
    private boolean _inheritLocalPaths;

    /**
     * Set the path prefix for the module where the delegate ExceptionConfig lives.
     */
    public void setDelegateModulePath(String delegateModulePath) {
        _delegateModulePath = delegateModulePath;
    }

    /**
     * Set the path for the action where the delegate ExceptionConfig lives.  This will be <code>null</code> if the
     * delegate ExceptionConfig lives at the module level.
     */
    public void setDelegateActionPath(String delegateActionPath) {
        _delegateActionPath = delegateActionPath;
    }

    /**
     * Get the ModuleConfig where the delegate ExceptionConfig lives.
     */
    public ModuleConfig getDelegateModuleConfig(ServletContext servletContext) {
        return InternalUtils.ensureModuleConfig(_delegateModulePath, servletContext);
    }
    
    public void init(ServletContext servletContext)
    {
        ModuleConfig moduleConfig = getDelegateModuleConfig(servletContext);
        assert moduleConfig != null : "No ModuleConfig found for path " + _delegateModulePath;
        
        if (_delegateActionPath != null) {
            ActionConfig actionConfig = moduleConfig.findActionConfig(_delegateActionPath);
            assert actionConfig != null : "No action config found for path " + _delegateActionPath;
            _delegate = actionConfig.findExceptionConfig(getType());
            assert _delegate != null : "No ExceptionConfig with type " + getType() + " on action " + _delegateActionPath
                                      + " in module " + _delegateModulePath;
        } else {
            _delegate = moduleConfig.findExceptionConfig(getType());
            assert _delegate != null : "No ExceptionConfig with type " + getType() + " in module " + _delegateModulePath;
        }
    }

    public String toString() {
        return "[delegate to " + _delegate + ']';
    }

    /**
     * Get the prefix directory path that local paths in Forwards should be relative to.  This is only enabled if we're
     * inheriting local paths from a base page flow.
     */
    public String getLocalPathsRelativeTo() {
        return _inheritLocalPaths ? _delegateModulePath : null;
    }

    /**
     * Tell whether local paths should be inherited from the base class.  This affects the value of
     * {@link #getLocalPathsRelativeTo()}.
     */
    public void setInheritLocalPaths(boolean inheritLocalPaths) {
        _inheritLocalPaths = inheritLocalPaths;
    }
    
    //
    // Everything below this point is simple delegation.
    //

    public boolean isHandlerMethod() {
        return _delegate instanceof PageFlowExceptionConfig && ((PageFlowExceptionConfig)_delegate).isHandlerMethod();
    }
    
    public String getDefaultMessage() {
        return _delegate instanceof PageFlowExceptionConfig ? ((PageFlowExceptionConfig)_delegate).getDefaultMessage() : null;
    }

    public boolean isPathContextRelative() {
        return _delegate instanceof PageFlowExceptionConfig && ((PageFlowExceptionConfig)_delegate).isPathContextRelative();
    }

    public boolean isReadonly() {
        return _delegate instanceof PageFlowExceptionConfig && ((PageFlowExceptionConfig)_delegate).isReadonly();
    }

    public String getBundle() {
        return _delegate.getBundle();
    }

    public String getHandler() {
        return _delegate.getHandler();
    }

    public String getKey() {
        return _delegate.getKey();
    }

    public String getPath() {
        return _delegate.getPath();
    }

    public String getScope() {
        return _delegate.getScope();
    }
}
