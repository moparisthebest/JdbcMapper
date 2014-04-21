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

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import java.util.List;

/**
 * <p>
 * JavaBean base class that creates HTML style class names used to render various HTML elements in the data grid.
 * </p>
 */
public abstract class StyleModel {

    private static final String DELIM = "-";
    private static final String SPACE = " ";
    private static final String EMPTY = "";

    private String _stylePrefix = null;

    /**
     * Default constructor.
     */
    public StyleModel() {
    }

    /**
     * Constructor that takes a style prefix string.  If a style prefix is provided, StyleModel subclasses
     * should use this as a prefix to any style names that are produced.
     *
     * @param stylePrefix the prefix to use for styles
     */
    public StyleModel(String stylePrefix) {
        _stylePrefix = stylePrefix;
    }

    /**
     * Set the style prefix.
     * @param stylePrefix the style prefix
     */
    public void setStylePrefix(String stylePrefix) {
        _stylePrefix = stylePrefix;
    }

    /**
     * Get the style prefix
     * @return the style prefix
     */
    public String getStylePrefix() {
        return _stylePrefix;
    }

    /**
     * Get the style class for an HTML table tag.
     * @return the style class
     */
    public abstract String getTableClass();

    /**
     * Get the style class for an HTML caption tag.
     * @return the style class
     */
    public abstract String getCaptionClass();

    /**
     * Get the style class for an HTML thead tag.
     * @return the style class
     */
    public abstract String getTableHeadClass();

    /**
     * Get the style class for an HTML tfoot tag.
     * @return the style class
     */
    public abstract String getTableFootClass();

    /**
     * Get the style class for an HTML tr tag rendered in the grid's header.
     * @return the style class
     */
    public abstract String getHeaderRowClass();

    /**
     * Get the style class for an HTML tr tag rendered in the grid's footer.
     * @return the style class
     */
    public abstract String getFooterRowClass();

    /**
     * Get the style class for an HTML tr tag rendered in a grid row.  This style class will be used on
     * even numbered rows.
     * @return the style class
     */
    public abstract String getRowClass();

    /**
     * Get the style class for an HTML tr tag rendered in a grid row.  This style class will be used on
     * odd numbered rows.
     * @return the style class
     */
    public abstract String getAltRowClass();

    /**
     * Get the style class for an HTML td tag rendered for a grid cell.
     * @return the style class
     */
    public abstract String getDataCellClass();

    /**
     * Get the style class for an HTML th tag rendered for a grid's header cell.
     * @return the style class
     */
    public abstract String getHeaderCellClass();

    /**
     * Get the style class for an HTML th tag for a grid's header cell whose data is sorted.
     * @return the style class
     */
    public abstract String getHeaderCellSortedClass();

    /**
     * Get the style class for an HTML th tag for a grid's header cell that is sortable.
     * @return the style class
     */
    public abstract String getHeaderCellSortableClass();

    /**
     * Get the style class for an HTML th tag for a grid's header cell that is filtered.
     * @return the style class
     */
    public abstract String getHeaderCellFilteredClass();

    /**
     * Get the style class for an HTML td tag for a grid cell that is sorted.
     * @return the style class
     */
    public abstract String getDataCellSortedClass();

    /**
     * Get the style class for an HTML td tag for a grid cell that is filtered.
     * @return the style class
     */
    public abstract String getDataCellFilteredClass();

    /**
     * Build the value of the HTML style class attribute from the {@link List} of style classes.  The style classes
     * are converted into a string in their list order.  For example, a list with contents:
     * <pre>
     *  foo,bar,baz
     * </pre>
     * will be convereted into a style class whose value is <code>foo,bar,baz</code>
     *
     * @param styleClasses the classes to render into a style class value
     * @return the string style class or an empty string if no style classes are provided
     */
    public String buildStyleClassValue(List/*<String>*/ styleClasses) {
        if(styleClasses == null)
            return EMPTY;

        boolean styleWritten = false;
        InternalStringBuilder buf = new InternalStringBuilder();
        for(int i = 0; i < styleClasses.size(); i++) {
            if(styleWritten)
                buf.append(SPACE);

            if(styleClasses.get(i) != null) {
                buf.append(styleClasses.get(i));
                styleWritten = true;
            }
        }

        if(!styleWritten)
            return null;
        else return buf.toString();
    }

    /**
     * Given a base style class name, this method adds a style prefix to produce a complete style class.
     * @param baseStyle the core style class name
     * @return the style class
     */
    protected String buildStyleClass(String baseStyle) {
        if(_stylePrefix != null)
            return prefix(baseStyle);
        else return baseStyle;
    }

    /* @todo: perf - could cache the style names once they've been produced */
    /**
     * Utility method to concatenate the given style class name and the style prefix.
     * @param style the core style name
     * @return the style class
     */
    private final String prefix(String style) {
        InternalStringBuilder sb = new InternalStringBuilder(16);
        sb.append(_stylePrefix);
        if(style != null) {
            sb.append(DELIM);
            sb.append(style);
        }
        return sb.toString();
    }
}
