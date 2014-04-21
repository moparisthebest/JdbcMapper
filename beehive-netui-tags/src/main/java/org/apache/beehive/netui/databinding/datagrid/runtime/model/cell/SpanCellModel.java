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

import org.apache.beehive.netui.tags.rendering.SpanTag;
import org.apache.beehive.netui.tags.rendering.SpanTag.State;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellModel;

/**
 *
 */
public final class SpanCellModel
    extends CellModel {

    private static final SpanTag.State DEFAULT_SPAN_STATE = new SpanTag.State();

    private Object _value = null;
    private String _javascript = null;
    private SpanTag.State _spanState = new SpanTag.State();

    public Object getValue() {
        return _value;
    }

    public void setValue(Object value) {
        _value = value;
    }

    public State getSpanState() {
        return _spanState != null ? _spanState : DEFAULT_SPAN_STATE;
    }

    public void setSpanState(State spanState) {
        _spanState = spanState;
    }

    public String getJavascript() {
        return _javascript;
    }

    public void setJavascript(String javascript) {
        _javascript = javascript;
    }
}
