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

package org.apache.beehive.controls.test.controls.beancontext;

import java.beans.Visibility;

/**
 */
public class VisibilityImpl implements Visibility {

    private boolean _canUseGui = false;
    private boolean _needsGui = false;

    public boolean needsGui() {
        return _needsGui;
    }

    public void dontUseGui() {
        _canUseGui = false;
    }

    public void okToUseGui() {
        _canUseGui = true;
    }

    public boolean avoidingGui() {
        return _needsGui && !_canUseGui;
    }

    public boolean getCanUseGui() {
        return _canUseGui;
    }

    public void setNeedsGui(boolean needsGui) {
        _needsGui = needsGui;
    }
}
