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
package org.apache.beehive.netui.tags.databinding.datagrid;

import javax.servlet.jsp.JspContext;

import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellModel;

/**
 * Package protected class that provides utility methods for managing adding and removing
 * {@link DataGridTagModel} and {@link CellModel} instances in the {@link JspContext}.
 */
final class DataGridUtil {

    private static final String KEY_GRID_MODEL = "dataGrid";
    private static final String KEY_CELL_MODEL = "cell";

    /* do not construct */
    private DataGridUtil() {}

    /**
     * Get the {@link DataGridTagModel} instance from the {@link JspContext} using the
     * {@link #KEY_GRID_MODEL} as the key.
     * @param jspContext the jsp context
     * @return the {@link DataGridTagModel} if one is found
     */
    public static DataGridTagModel getDataGridTagModel(JspContext jspContext) {
        Object model = jspContext.getAttribute(KEY_GRID_MODEL);
        assert model != null ? model instanceof DataGridTagModel : true;
        return (DataGridTagModel)model;
    }

    /**
     * Put the {@link DataGridTagModel} in the {@link JspContext} using the {@link #KEY_GRID_MODEL}
     * as the key.
     * @param jspContext the jsp context
     * @param dataGridTagModel the {@link DataGridTagModel}
     */
    public static void putDataGridTagModel(JspContext jspContext, DataGridTagModel dataGridTagModel) {
        jspContext.setAttribute(KEY_GRID_MODEL, dataGridTagModel);
    }

    /**
     * Remove a {@link DataGridTagModel} from the {@link JspContext}
     * @param jspContext the jsp context
     */
    public static void removeDataGridTagModel(JspContext jspContext) {
        jspContext.removeAttribute(KEY_GRID_MODEL);
    }

    /**
     * Get the {@link CellModel} instance from the {@link JspContext} using the
     * {@link #KEY_CELL_MODEL} as the key.
     * @param jspContext the jsp context
     * @return the {@link CellModel} if one is found
     */
    public static CellModel getCellModel(JspContext jspContext) {
        Object model = jspContext.getAttribute(KEY_CELL_MODEL);
        assert model != null ? model instanceof CellModel : true;
        return (CellModel)model;
    }

    /**
     * Put the {@link CellModel} in the {@link JspContext} using the {@link #KEY_CELL_MODEL}
     * as the key.
     * @param jspContext the jsp context
     * @param cellModel the {@link CellModel}
     */
    public static void putCellModel(JspContext jspContext, CellModel cellModel) {
        jspContext.setAttribute(KEY_CELL_MODEL, cellModel);
    }

    /**
     * Remove a {@link CellModel} from the {@link JspContext}
     * @param jspContext the jsp context
     */
    public static void removeCellModel(JspContext jspContext) {
        jspContext.removeAttribute(KEY_CELL_MODEL);
    }
}
