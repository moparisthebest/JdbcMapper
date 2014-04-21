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
package org.apache.beehive.netui.databinding.datagrid.api.rendering;

import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.jsp.JspException;

import org.apache.beehive.netui.tags.html.FormatTag.Formatter;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * The CellModel is the base class for JavaBean objects that are used to configure the rendering of a CellDecorator.
 * A CellModel exposes primitive services and state that can be used by {@link CellDecorator}s during rendering.
 * </p>
 */
public class CellModel {

    private static final Logger LOGGER = Logger.getInstance(CellModel.class);

    private DataGridTagModel _dataGridTagModel;
    private ArrayList/*<Formatter>*/ _formatters;

    /**
     * Get the {@link DataGridTagModel} which is associated with the data grid tag that contains this
     * cell.
     *
     * @return the {@link DataGridTagModel} for this cell.  Inside a valid data grid, this method should
     * not return <code>null</code>.
     */
    public DataGridTagModel getDataGridTagModel() {
        return _dataGridTagModel;
    }

    /**
     * Set the {@link DataGridTagModel} for this cell.
     *
     * @param dataGridTagModel the new {@link DataGridTagModel} value.
     */
    public void setDataGridTagModel(DataGridTagModel dataGridTagModel) {
        _dataGridTagModel = dataGridTagModel;
    }

    /**
     * Add a {@link Formatter} which can be used to format an Object for rendering.  Many
     * {@link Formatter} instances can be registered and will be executed in the order in
     * which they were added.  This method is provided as a service to CellModel subclasses;
     * the use of formatters can vary based on the implementation of a {@link CellDecorator}.
     *
     * @param formatter the {@link Formatter} to add
     */
    public void addFormatter(Formatter formatter) {
        if(_formatters == null)
            _formatters = new ArrayList/*<Formatter>*/();

        _formatters.add(formatter);
    }

    /**
     * Format an {@link Object} value.  This method is used to apply a chain of formatters to some
     * value.  It will return <code>null</code> if the provided value is null; in this case, it
     * is up to the caller to provide an appropriate default value.
     *
     * @param value the {@link Object} to format
     * @return If the <code>value</code> is null, return <code>null</code>.   If there are no registered
     * {@link Formatter} instances, return {@link Object#toString()} for the <code>value</code> parameter.
     * Otherwisee, return the formatted value.
     */
    public String formatText(Object value) {
        if(value == null)
            return null;

        if(_formatters == null)
            return value.toString();

        Object formatted = value;
        for(int i = 0; i < _formatters.size(); i++) {
            assert _formatters.get(i) instanceof Formatter :
                    "Found invalid formatter type \"" +
                    (_formatters.get(i) != null ? _formatters.get(i).getClass().getName() : "null") + "\"";

            Formatter formatter = (Formatter)_formatters.get(i);
            assert formatter != null;
            try {
                formatted = formatter.format(formatted);
            }
            catch(JspException e) {
                /* todo: error reporting */
                LOGGER.error(Bundle.getErrorString("CellModel_FormatterThrewException", new Object[]{formatter.getClass().getName(), e}), e);
            }
        }

        assert formatted != null;
        return formatted.toString();
    }
}
