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
package org.apache.beehive.netui.databinding.datagrid.runtime.model.style;

import org.apache.beehive.netui.databinding.datagrid.api.rendering.StyleModel;

/**
 *
 */
public class DefaultStyleModel
    extends StyleModel {

    private static final String CLASS_ROW_HEADER = "header";
    private static final String CLASS_ROW_FOOTER = "footer";
    private static final String CLASS_ROW_EVEN = "even";
    private static final String CLASS_ROW_ODD = "odd";
    private static final String CLASS_CELL_HEADER_SORTABLE = "sortable";
    private static final String CLASS_CELL_HEADER_SORTED = "sorted";
    private static final String CLASS_CELL_HEADER_FILTERED = "filtered";
    private static final String CLASS_CELL_DATA_SORTED = "sorted";
    private static final String CLASS_CELL_DATA_FILTERED = "filtered";

    public DefaultStyleModel() {
        this(null);
    }

    public DefaultStyleModel(String prefix) {
        super(prefix);
    }

    public String getTableClass() {
        return buildStyleClass(null);
    }

    public String getCaptionClass() {
        return buildStyleClass(null);
    }

    public String getTableHeadClass() {
        return buildStyleClass(null);
    }

    public String getTableFootClass() {
        return buildStyleClass(null);
    }

    public String getHeaderRowClass() {
        return buildStyleClass(CLASS_ROW_HEADER);
    }

    public String getFooterRowClass() {
        return buildStyleClass(CLASS_ROW_FOOTER);
    }

    public String getRowClass() {
        return buildStyleClass(CLASS_ROW_EVEN);
    }

    public String getAltRowClass() {
        return buildStyleClass(CLASS_ROW_ODD);
    }

    public String getDataCellClass() {
        return buildStyleClass(null);
    }

    public String getHeaderCellClass() {
        return buildStyleClass(null);
    }

    public String getHeaderCellSortableClass() {
        return buildStyleClass(CLASS_CELL_HEADER_SORTABLE);
    }

    public String getHeaderCellSortedClass() {
        return buildStyleClass(CLASS_CELL_HEADER_SORTED);
    }

    public String getHeaderCellFilteredClass() {
        return buildStyleClass(CLASS_CELL_HEADER_FILTERED);
    }

    public String getDataCellSortedClass() {
        return buildStyleClass(CLASS_CELL_DATA_SORTED);
    }

    public String getDataCellFilteredClass() {
        return buildStyleClass(CLASS_CELL_DATA_FILTERED);
    }
}
