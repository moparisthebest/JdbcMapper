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
package org.apache.beehive.netui.databinding.datagrid.runtime.model.cell;

import java.util.HashMap;

import org.apache.beehive.netui.tags.rendering.AnchorTag;
import org.apache.beehive.netui.tags.rendering.AnchorTag.State;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellModel;

/**
 *
 */
public class AnchorCellModel
    extends CellModel {

    private String _linkName = null;
    private Object _value = null;
    private String _href = null;
    private String _scopeId = null;
    private String _action = null;
    private String _javascript = null;
    private HashMap _params = null;
    private AnchorTag.State _anchorState = new AnchorTag.State();

    public Object getValue() {
        return _value;
    }

    public void setValue(Object value) {
        _value = value;
    }

    public String getHref() {
        return _href;
    }

    public void setHref(String href) {
        _href = href;
    }

    public String getScopeId() {
        return _scopeId;
    }

    public void setScopeId(String scopeId) {
        _scopeId = scopeId;
    }

    public String getAction() {
        return _action;
    }

    public void setAction(String action) {
        _action = action;
    }

    public HashMap getParams() {
        if(_params == null)
            _params = new HashMap();

        return _params;
    }

    public void setParams(HashMap params) {
        _params = params;
    }

    public State getAnchorState() {
        return _anchorState;
    }

    public void setAnchorState(State anchorState) {
        _anchorState = anchorState;
    }

    public String getLinkName() {
        return _linkName;
    }

    public void setLinkName(String linkName) {
        _linkName = linkName;
    }

    public String getJavascript() {
        return _javascript;
    }

    public void setJavascript(String javascript) {
        _javascript = javascript;
    }
}
