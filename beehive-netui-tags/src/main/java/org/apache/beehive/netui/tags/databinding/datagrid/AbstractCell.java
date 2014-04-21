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

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.ServletException;

import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellModel;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.StringBuilderRenderAppender;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.tags.html.FormatTag.Formatter;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * <p>
 * Abstract base class for JSP tags that render data grid cells.  This class provides support to
 * subclasses in several areas:
 * <ul>
 * <li>formatting -- this class accepts instances of {@link Formatter} which can optionally be used
 * by a subclass to perform formatting on content that is written to a rendered page</li>
 * <li>applying attributes</li>
 * <li>applying state attributes to {@link AbstractHtmlState} instances used by subclasses.
 * </ul>
 * </p>
 */
public abstract class AbstractCell
    extends AbstractDataGridHtmlTag {

    private static final Logger LOGGER = Logger.getInstance(AbstractCell.class);

    /* todo: switch onto ConstantRendering.NBSP */
    private static final String EMPTY_CELL = "&nbsp;";

    /**
     * Add a {@link Formatter}.  Subclasses can optionally use the support formatting; formatters
     * are added to the {@link CellModel} associated with an instance of the subclass.
     * @param formatter the formatter to add
     */
    public void addFormatter(Formatter formatter) {
        internalGetCellModel().addFormatter(formatter);
    }

    /**
     * Indicate that a formatter has reported an error so the formatter should output it's body text.
     */
    public void formatterHasError() {
        /* todo: error reporting */
    }

    /**
     * <p>
     * This method implements the rendering process for data grid cells.  When the data grid's
     * rendering state is <b>not</b> {@link DataGridTagModel#RENDER_STATE_START}, this tag processes
     * its body.  The tag performs the following steps in order:
     * <ol>
     * <li>The tag invokes its {@link #applyAttributes()} method to allow subclasses to apply attributes
     * to their {@link CellModel} instances at a well known time.  Any errors in attribute checking
     * should be thrown here.</li>
     * <li>The tag adds the {@link CellModel} associated with the data grid to the
     * {@link javax.servlet.jsp.JspContext} under the key <code>cellModel</code>.</li>
     * <li>Rendering is performed by invoking
     * {@link #renderCell(org.apache.beehive.netui.tags.rendering.AbstractRenderAppender)}. If content is
     * rendered when the body of the tag is rendered, it is written to the output stream.
     * </li>
     * <li>The tag removes the {@link CellModel} instance.  If an exception is thrown after the
     * {@link CellModel} is added to the {@link javax.servlet.jsp.JspContext}, it the cell model
     * will still be removed from the JspContext.</li>
     * </ol>
     * </p>
     * @throws JspException
     * @throws IOException
     */
    public void doTag()
        throws JspException, IOException {

        DataGridTagModel dataGridModel = DataGridUtil.getDataGridTagModel(getJspContext());
        if(dataGridModel == null) {
            String s = Bundle.getString("Tags_DataGrid_MissingDataGridModel", new Object[]{getTagName()});
            throw new JspException(s);
        }

        int gridRenderState = dataGridModel.getRenderState();

        /* RENDER_STATE_START is a no-op for cells */
        if(gridRenderState == DataGridTagModel.RENDER_STATE_START) {
            return;
        }
        /*
        otherwise, the CellModel associated with this tag
        needs to be fetched from the <cell> tag for the current
        iteration
        */
        else {
            CellModel model = internalGetCellModel();
            model.setDataGridTagModel(dataGridModel);

            applyAttributes();

            try {
                DataGridUtil.putCellModel(getJspContext(), model);

                InternalStringBuilder content = new InternalStringBuilder();
                AbstractRenderAppender appender = new StringBuilderRenderAppender(content);

                renderCell(appender);

                if(content != null && content.length() > 0)
                    getJspContext().getOut().println(content.toString());
            }
            finally {
                DataGridUtil.removeCellModel(getJspContext());
            }
        }

        return;
    }

    /**
     * <p>
     * Abstract method implemented by subclasses.  Implementers should return the {@link CellModel} associated
     * with the UI that is being rendered by the JSP tag.
     * </p>
     * @return the cell's {@link CellModel}
     */
    protected abstract CellModel internalGetCellModel();

    /**
     * <p>
     * Abstract method implemented by subclasses to perform cell-specific rendering.
     * </p>
     * @param appender the {@link AbstractRenderAppender} to which any output should be rendered
     * @throws IOException
     * @throws JspException
     */
    protected abstract void renderCell(AbstractRenderAppender appender)
        throws IOException, JspException;

    /**
     * Utility method usable by subclasses that renders an HTML &amp;nbsp; to represent an empty HTML table cell.
     * @param appender the {@link AbstractRenderAppender} to which any output should be rendered
     */
    protected void renderEmptyCell(AbstractRenderAppender appender) {
        appender.append(EMPTY_CELL);
    }

    /**
     * Utility method invoked during tag rendering.  Subclasses should place attribute validation logic
     * here.
     * @throws JspException if application of attributes fails
     */
    protected void applyAttributes()
        throws JspException {
    }

    /**
     * <p>
     * Add an HTML state attribute to a {@link AbstractHtmlState} object.  This method performs
     * checks on common attributes and sets their values on the state object or throws an exception.
     * </p>
     * <p>
     * For the HTML tags it is not legal to set the <code>id</code> or <code>name</code> attributes.
     * In addition, the base tag does
     * not allow facets to be set.  If the attribute is legal it will be added to the
     * general expression map stored in the <code>AbstractHtmlState</code> of the tag.
     * </p>
     *
     * @param state the state object to which attributes are appliedn
     * @param name the name of an attribute
     * @param value the value of the attribute
     * @throws JspException when an error occurs setting the attribute on the state object
     */
    protected final void addStateAttribute(AbstractHtmlState state, String name, String value)
            throws JspException {

        // validate the name attribute, in the case of an error simply return.
        if(name == null || name.length() <= 0) {
            String s = Bundle.getString("Tags_AttributeNameNotSet");
            throw new JspException(s);
        }

        // it's not legal to set the id or name attributes this way
        if(name.equals(HtmlConstants.ID) || name.equals(HtmlConstants.NAME)) {
            String s = Bundle.getString("Tags_AttributeMayNotBeSet", new Object[]{name});
            throw new JspException(s);
        }

        // if there is a style or class we will let them override the base
        if(name.equals(HtmlConstants.CLASS)) {
            state.styleClass = value;
            return;
        }
        else if(name.equals(HtmlConstants.STYLE)) {
            state.style = value;
            return;
        }

        state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, name, value);
    }
}
