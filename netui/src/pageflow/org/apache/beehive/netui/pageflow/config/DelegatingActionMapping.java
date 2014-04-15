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

import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * A Struts ActionMapping object that delegates to an ActionMapping in a different module.
 */
public class DelegatingActionMapping extends PageFlowActionMapping
{
    private ActionMapping _delegate;
    private String _delegateModulePath;
    private boolean _inheritLocalPaths;

    // values from the module config to override the delegate properties.
    private boolean _loginRequiredSet = false;
    private boolean _readonlySet = false;

    /**
     * Set the path prefix for the module where the delegate ActionMapping lives.
     */
    public void setDelegateModulePath(String delegateModulePath) {
        _delegateModulePath = delegateModulePath;
    }

    public void init(ServletContext servletContext)
    {
        ModuleConfig moduleConfig = InternalUtils.ensureModuleConfig(_delegateModulePath, servletContext);
        assert moduleConfig != null : "No ModuleConfig found for path " + _delegateModulePath;
        _delegate = (ActionMapping) moduleConfig.findActionConfig(getPath());

        // It's possible that an overloaded action method in the derived class has the
        // same unqualified action path, causing the path to be reset to a disambiguated
        // path that includes the form bean class type. However, in the base class module
        // config, the action path does not include the form bean class type. If the
        // action path didn't match any in the delegate, try the unqualified path. 
        if (_delegate == null) {
            _delegate = (ActionMapping) moduleConfig.findActionConfig(super.getUnqualifiedActionPath());
        }
        assert _delegate != null : "No ActionMapping with path " + getPath() + " in module " + _delegateModulePath;
    }

    /**
     * Get the prefix directory path that local paths in Forwards should be relative to.  This is only enabled if we're
     * inheriting local paths from a base page flow.
     */
    public String getLocalPathsRelativeTo() {
        return _inheritLocalPaths ? _delegateModulePath : null;
    }

    
    public String toString() {
        return "[delegate to " + _delegate + ']';
    }

    public void freeze() {
        configured = true;
    }

    /**
     * Tell whether local paths should be inherited from the base class.  This affects the value of
     * {@link #getLocalPathsRelativeTo()}.
     */
    public void setInheritLocalPaths(boolean inheritLocalPaths) {
        _inheritLocalPaths = inheritLocalPaths;
    }

    //
    // Attributes that may have been set to override the delegate
    //

    public String getRoles() {
        if (roles != null) {
            return roles;
        }
        return _delegate.getRoles();
    }

    //
    // Properties that may have been set to override the delegate
    //

    public boolean isLoginRequired() {
        if (_loginRequiredSet) {
            return super.isLoginRequired();
        }
        return _delegate instanceof PageFlowActionMapping && ((PageFlowActionMapping) _delegate).isLoginRequired();
    }

    public void setLoginRequired(boolean loginRequired) {
        _loginRequiredSet = true;
        super.setLoginRequired(loginRequired);
    }

    public boolean isReadonly() {
        if (_readonlySet) {
            return super.isReadonly();
        }
        return _delegate instanceof PageFlowActionMapping && ((PageFlowActionMapping) _delegate).isReadonly();
    }

    public void setReadonly(boolean readonly) {
        _readonlySet = true;
        super.setReadonly(readonly);
    }

    // If an action level forward does not exist for the delegate then
    // look for a global forward of the module config for this action
    // mapping. Then, if still not found, look in the module config of
    // the delegate.
    public ActionForward findForward(String forwardName) {
        ForwardConfig config = _delegate.findForwardConfig(forwardName);
        if (config == null) {
            config = getModuleConfig().findForwardConfig(forwardName);
        }
        if (config == null) {
            config = _delegate.getModuleConfig().findForwardConfig(forwardName);
        }
        return (ActionForward) config;
    }

    //
    // Everything below this point is simple delegation.
    //
    
    public String getUnqualifiedActionPath() {
        return _delegate instanceof PageFlowActionMapping ? ((PageFlowActionMapping) _delegate).getUnqualifiedActionPath() : null;
    }

    public String getUnqualifiedActionName() {
        return _delegate instanceof PageFlowActionMapping ? ((PageFlowActionMapping) _delegate).getUnqualifiedActionName() : null;
    }

    public boolean isPreventDoubleSubmit() {
        return _delegate instanceof PageFlowActionMapping && ((PageFlowActionMapping) _delegate).isPreventDoubleSubmit();
    }

    public boolean isSimpleAction() {
        return _delegate instanceof PageFlowActionMapping && ((PageFlowActionMapping) _delegate).isSimpleAction();
    }

    public boolean isOverloaded() {
        return _delegate instanceof PageFlowActionMapping && ((PageFlowActionMapping) _delegate).isOverloaded();
    }

    public Map getConditionalForwardsMap() {
        return _delegate instanceof PageFlowActionMapping ? ((PageFlowActionMapping) _delegate).getConditionalForwardsMap() : null;
    }

    public String getFormBeanMessageResourcesKey() {
        return _delegate instanceof PageFlowActionMapping ? ((PageFlowActionMapping) _delegate).getFormBeanMessageResourcesKey() : null;
    }

    public String getDefaultForward() {
        return _delegate instanceof PageFlowActionMapping ? ((PageFlowActionMapping) _delegate).getDefaultForward() : null;
    }

    public String[] findForwards() {
        return _delegate.findForwards();
    }

    public ActionForward getInputForward() {
        return _delegate.getInputForward();
    }

    public String getForward() {
        return _delegate.getForward();
    }

    public String getInclude() {
        return _delegate.getInclude();
    }

    public String getInput() {
        return _delegate.getInput();
    }

    public String getMultipartClass() {
        return _delegate.getMultipartClass();
    }

    public String getPrefix() {
        return _delegate.getPrefix();
    }

    public String[] getRoleNames() {
        return _delegate.getRoleNames();
    }

    public String getScope() {
        return _delegate.getScope();
    }

    public String getSuffix() {
        return _delegate.getSuffix();
    }

    public String getType() {
        return _delegate.getType();
    }

    public boolean getUnknown() {
        return _delegate.getUnknown();
    }

    public boolean getValidate() {
        return _delegate.getValidate();
    }

    public ExceptionConfig findExceptionConfig(String type) {
        return _delegate.findExceptionConfig(type);
    }

    public ExceptionConfig[] findExceptionConfigs() {
        return _delegate.findExceptionConfigs();
    }

    public ExceptionConfig findException(Class type) {
        return _delegate.findException(type);
    }

    public ForwardConfig findForwardConfig(String name) {
        return _delegate.findForwardConfig(name);
    }

    public ForwardConfig[] findForwardConfigs() {
        return _delegate.findForwardConfigs();
    }

}

