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

import javax.servlet.jsp.JspContext;

import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.databinding.datagrid.api.exceptions.CellDecoratorException;

/**
 * <p>
 * Abstract basee class used to render the contents of a data grid cell.  CellDecorators are used so that
 * code used to render the contents of a data grid cell can be chained together in order to compose
 * different rendering patterns.  For example, an HTML anchor and image decorator could be composed together
 * to create an image anchor renderer.  In addition, cell decoration can be used to display UI exposing
 * custom data grid features such as sort or filter UI on data grid header cells.
 * </p>
 * <p>
 * CellDecorators are intended to be <b>stateless</b>.  State required for rendering should be passed to
 * a CellDecorator using an instance of a {@link CellModel} class. 
 * </p>
 */
public abstract class CellDecorator {

    /**
     * This decorator can be optionally used by implementations to
     * render additional UI for the cell.
     */
    private CellDecorator _cellDecorator = null;

    /**
     * Default constructor.
     */
    public CellDecorator() {
    }

    /**
     * Constructor that takes a nested CellDecorator.
     *
     * @param cellDecorator the nested decorator which can optionally be used by implementations
     * to render additional UI for the cell.
     */
    public CellDecorator(CellDecorator cellDecorator) {
        this();
        _cellDecorator = cellDecorator;
    }

    /**
     * Get the nested decorator.
     *
     * @return the cell decorator if one has been provided.  <code>null</code> otherwise.
     */
    public CellDecorator getNestedDecorator() {
        return _cellDecorator;
    }

    /**
     * Set the nested cell decorator.
     *
     * @param cellDecorator the cell decorator.
     */
    public void setNestedDecorator(CellDecorator cellDecorator) {
        _cellDecorator = cellDecorator;
    }

    /**
     * This method is implemented by subclasses to provide decoration behavior.  The use of a nested CellDecorator
     * is left to specific implementations; it is possible that some implementations will ignore such
     * nested instances.
     *
     * @param jspContext the {@link JspContext} for the current page
     * @param appender the {@link AbstractRenderAppender} to which markup should be rendered
     * @param cellModel the {@link CellModel} JavaBean that contains
     * @throws CellDecoratorException an exception thrown when an error occurs running the decorator.
     */
    public abstract void decorate(JspContext jspContext, AbstractRenderAppender appender, CellModel cellModel)
            throws CellDecoratorException;
}
