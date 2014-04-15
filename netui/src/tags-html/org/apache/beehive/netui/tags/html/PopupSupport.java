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
package org.apache.beehive.netui.tags.html;

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import org.apache.beehive.netui.pageflow.internal.InternalConstants;
import org.apache.beehive.netui.pageflow.scoping.ScopedRequest;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.tags.internal.ReturnActionViewRenderer;
import org.apache.beehive.netui.tags.javascript.CoreScriptFeature;
import org.apache.beehive.netui.tags.javascript.IScriptReporter;
import org.apache.beehive.netui.tags.javascript.ScriptRequestState;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

class PopupSupport
{
    private static final String VIEW_RENDERER_CLASS_NAME = ReturnActionViewRenderer.class.getName();
    private static final String ON_POPUP_DONE_FUNC = "Netui_OnPopupDone";
    private static final String POPUP_FUNC = "Netui_Popup";
    private static final String POPUP_WINDOW = "Netui_Window";

    private String _name = "";
    private HashMap _features;
    private boolean _replace = false;
    private String _onPopupDone;
    private String _popupFunc;
    private String _formName;

    public void setName(String name)
    {
        _name = name;
    }

    public void setToolbar(boolean toolbar)
    {
        putFeature("toolbar", toolbar);
    }

    public void setLocation(boolean location)
    {
        putFeature("location", location);
    }

    public void setDirectories(boolean directories)
    {
        putFeature("directories", directories);
    }

    public void setStatus(boolean status)
    {
        putFeature("status", status);
    }

    public void setMenubar(boolean menubar)
    {
        putFeature("menubar", menubar);
    }

    public void setScrollbars(boolean scrollbars)
    {
        putFeature("scrollbars", scrollbars);
    }

    public void setResizable(boolean resizable)
    {
        putFeature("resizable", resizable);
    }

    public void setWidth(int width)
    {
        putFeature("width", new Integer(width));
    }

    public void setHeight(int height)
    {
        putFeature("height", new Integer(height));
    }

    public void setLeft(int left)
    {
        putFeature("left", new Integer(left));
    }

    public void setTop(int top)
    {
        putFeature("top", new Integer(top));
    }

    public void setReplace(boolean replace)
    {
        _replace = replace;
    }

    public void setOnPopupDone(String onPopupDone)
    {
        _onPopupDone = onPopupDone;
    }

    public void setPopupFunc(String popupFunc)
    {
        _popupFunc = popupFunc;
    }

    /**
     * Sets whether the JavaScript function that opens the popup window should add data
     * from the form fields to the request. The form name is used in the JavaScript
     * function of onClick to identify the form object correctly. The JavaScript
     * will only be set up to pass the names and values for the form fields on the
     * request if the form name is set. 
     *
     * @param formName the name attribute of the form to pass field data from.
     *                 This must be the real/generated form name and not the
     *                 netui:form tagId attribute.
     */
    public void setFormName(String formName)
    {
        _formName = formName;
    }

    public String getOnClick(ServletRequest req, String url)
    {
        // Build up the string that's passed to javascript open() to specify window features.
        InternalStringBuilder features = new InternalStringBuilder();
        
        if (_features != null) {
            boolean firstOne = true;
            for (Iterator i = _features.entrySet().iterator(); i.hasNext();)
            {
                Map.Entry entry = (Map.Entry) i.next();
                if (!firstOne) {
                    features.append(',');
                }
                features.append(entry.getKey());
                features.append('=');
                features.append(entry.getValue());
                firstOne = false;
            }
        }

        String onClick = null;
        String popupWindow = getScopedFunctionName(req, POPUP_WINDOW);

        String popupFunc = (_popupFunc != null ? _popupFunc : POPUP_FUNC);
        if (_formName != null && _formName.length() > 0) {
            Object[] args = new Object[]{popupFunc, url, _name, features.toString(), Boolean.valueOf(_replace), popupWindow, _formName};
            onClick = ScriptRequestState.getString("popupSupportUpdateFormOnClick", args);
        }
        else {
            Object[] args = new Object[]{popupFunc, url, _name, features.toString(), Boolean.valueOf(_replace), popupWindow};
            onClick = ScriptRequestState.getString("popupSupportOnClick", args);
        }

        return onClick;
    }

    public void addParams(IUrlParams urlParams, ServletRequest request)
            throws JspException
    {
        urlParams.addParameter(InternalConstants.RETURN_ACTION_VIEW_RENDERER_PARAM, VIEW_RENDERER_CLASS_NAME, null);
        String onPopupDone = (_onPopupDone != null ? _onPopupDone : ON_POPUP_DONE_FUNC);
        urlParams.addParameter(ReturnActionViewRenderer.getCallbackParamName(), onPopupDone, null);
        
        ScopedRequest scopedRequest = ScopedServletUtils.unwrapRequest(request);
        if (scopedRequest != null) {
            urlParams.addParameter(ScopedServletUtils.SCOPE_ID_PARAM, scopedRequest.getScopeKey().toString(), null);
        }
    }

    public void writeScript(ServletRequest req, ScriptRequestState srs, IScriptReporter scriptReporter, AbstractRenderAppender results)
    {
        // Write the generic function for popping a window.
        srs.writeFeature(scriptReporter, results, CoreScriptFeature.POPUP_OPEN, true, false, new Object[]{POPUP_FUNC});

        // Write the callback that's triggered when the popup window is closing.
        srs.writeFeature(scriptReporter, results, CoreScriptFeature.POPUP_DONE, true, false, new Object[]{ON_POPUP_DONE_FUNC});

        // Write the initialization of the popup window variable.
        String popupWindow = getScopedFunctionName(req, POPUP_WINDOW);
        srs.writeFeature(scriptReporter, results, "popupSupportWindowVariable", new Object[]{popupWindow});
    }
    
    private static String getScopedFunctionName(ServletRequest req, String funcName)
    {
        ScopedRequest scopedRequest = ScopedServletUtils.unwrapRequest(req);
        return (scopedRequest != null ? funcName + '_' + scopedRequest.getScopeKey() : funcName);
    }

    private void putFeature(String featureName, boolean val)
    {
        putFeature(featureName, val ? new Integer(1) : new Integer(0));
    }

    private void putFeature(String featureName, Object val)
    {
        if (_features == null) {
            _features = new HashMap();
        }
        _features.put(featureName, val);
    }
}

