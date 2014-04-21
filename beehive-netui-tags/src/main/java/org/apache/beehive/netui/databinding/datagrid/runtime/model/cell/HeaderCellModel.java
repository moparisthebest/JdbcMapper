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

import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellModel;

/**
 *
 */
public class HeaderCellModel
    extends CellModel {

    private Object _value = null;
    private String _bodyContent = null;

    private String _scopeId = null;

    private boolean _disableSortRendering = false;
    private String _sortExpression = null;
    private String _sortAction = null;
    private String _sortHref = null;

    private boolean _disableFilterRendering = false;
    private String _filterExpression = null;
    private String _filterAction = null;
    private String _filterHref = null;

    public String getScopeId() {
        return _scopeId;
    }

    public void setScopeId(String scopeId) {
        _scopeId = scopeId;
    }

    public String getSortAction() {
        return _sortAction;
    }

    public void setSortAction(String sortAction) {
        _sortAction = sortAction;
    }

    public String getSortHref() {
        return _sortHref;
    }

    public void setSortHref(String sortHref) {
        _sortHref = sortHref;
    }

    public boolean isSortable() {
        return _sortExpression != null;
    }

    public boolean isDisableSortRendering() {
        return _disableSortRendering;
    }

    public void setDisableSortRendering(boolean disableSortRendering) {
        _disableSortRendering = disableSortRendering;
    }

    public String getSortExpression() {
        return _sortExpression;
    }

    public void setSortExpression(String sortExpression) {
        _sortExpression = sortExpression;
    }

    public boolean isDisableFilterRendering() {
        return _disableFilterRendering;
    }

    public void setDisableFilterRendering(boolean disableFilterRendering) {
        _disableFilterRendering = disableFilterRendering;
    }

    public String getFilterExpression() {
        return _filterExpression;
    }

    public void setFilterExpression(String filterExpression) {
        _filterExpression = filterExpression;
    }

    public String getFilterAction() {
        return _filterAction;
    }

    public void setFilterAction(String filterAction) {
        _filterAction = filterAction;
    }

    public String getFilterHref() {
        return _filterHref;
    }

    public void setFilterHref(String filterHref) {
        _filterHref = filterHref;
    }

    public boolean isFilterable() {
        return _filterExpression != null;
    }

    public String getBodyContent() {
        return _bodyContent;
    }

    public void setBodyContent(String bodyContent) {
        _bodyContent = bodyContent;
    }

    public void setValue(Object value) {
        _value = value;
    }

    public Object getValue() {
        return _value;
    }
}
