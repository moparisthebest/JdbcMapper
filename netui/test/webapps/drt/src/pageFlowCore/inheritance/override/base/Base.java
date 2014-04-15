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
package pageFlowCore.inheritance.override.base;

import java.util.Arrays;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.config.DelegatingActionMapping;
import org.apache.beehive.netui.pageflow.config.PageFlowActionMapping;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ModuleConfig;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    })
public class Base extends PageFlowController
{
    protected String _actionInfo;
    public String getActionInfo(){
        return _actionInfo;
    }
    public void setActionInfo(String value){
        _actionInfo = value;
    }

    protected void onCreate()
    {
        setActionInfo(findActionInfo());
    }

    protected String findActionInfo() {
        StringBuffer sb = new StringBuffer(1024);
        String[] actions = getActions();
        if (actions != null) {
            Arrays.sort( actions );
            for (int i = 0; i < actions.length; i++) {
                sb.append("\n<pre>\n");
                sb.append(getConfigData(actions[i]));
                sb.append("\n</pre>");
            }
        }
        return sb.toString();
    }

    protected String getConfigData(String actionName) {
        StringBuffer sb = new StringBuffer(1024);
        ModuleConfig mConfig = getModuleConfig();
        if (mConfig == null) return "\n    null ModuleConfig!";
        ActionConfig aConfig = mConfig.findActionConfig("/" + actionName);
        if (aConfig == null) return "\n    No ActionConfig: " + actionName;

        sb.append("\n    actionName = ").append(aConfig.getName());
        sb.append("\n    actionPath = ").append(aConfig.getPath());
        sb.append("\n    actionParameter = ").append(aConfig.getParameter());
        if (aConfig instanceof PageFlowActionMapping) {
            PageFlowActionMapping pfam = (PageFlowActionMapping) aConfig;
            sb.append("\n    unqualifiedActionPath = ").append(pfam.getUnqualifiedActionPath());
            sb.append("\n    formClass = ").append(pfam.getFormClass());
            sb.append("\n    preventDoubleSubmit = ").append(pfam.isPreventDoubleSubmit());
            sb.append("\n    readOnly = ").append(pfam.isReadonly());
            sb.append("\n    loginRequired = ").append(pfam.isLoginRequired());
            sb.append("\n    rolesAllowed = ").append(aConfig.getRoles());
            sb.append("\n    doValidation = ").append(aConfig.getValidate());
            if (aConfig instanceof DelegatingActionMapping) {
                DelegatingActionMapping delegate = (DelegatingActionMapping) aConfig;
                sb.insert(0, "(DelegatingActionMapping)");
                sb.append("\n    localPathsRelativeTo = ").append(delegate.getLocalPathsRelativeTo());
            } else {
                sb.insert(0, "(PageFlowActionMapping)");
            }
        }

        return sb.toString();
    }
}
