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

import org.apache.beehive.netui.tags.rendering.ImageTag;
import org.apache.beehive.netui.tags.rendering.ImageTag.State;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellModel;

/**
 *
 */
public final class ImageCellModel
    extends CellModel {

    private ImageTag.State _imageState = null;
    private String _javascript = null;

    public ImageTag.State getImageState() {
        if(_imageState == null)
            _imageState = new ImageTag.State();

        return _imageState;
    }

    public void setImageState(State imageState) {
        _imageState = imageState;
    }

    public String getJavascript() {
        return _javascript;
    }

    public void setJavascript(String javascript) {
        _javascript = javascript;
    }
}
